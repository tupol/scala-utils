/*
MIT License

Copyright (c) 2018 Tupol (github.com/tupol)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package org.tupol

import com.typesafe.config.ConfigException.{BadValue, Missing}
import com.typesafe.config.{Config, ConfigObject}
import org.tupol.utils.implicits._
import scalaz.syntax.validation._
import scalaz.{NonEmptyList, ValidationNel}

import java.sql.{Date, Timestamp}
import java.time.{Duration, LocalDate, LocalDateTime}
import java.util
import scala.annotation.implicitNotFound
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
 * This package holds a framework for extracting configuration objects out of configuration files, using the
 * Typesafe Config library.
 * Alternative solution: PureConfig
 */
package object configz {

  /**
   * An extractor is a bit of code that knows how to extract a value of type T
   * from a Typesafe {{Config}} instance.
   *
   * @tparam T the type that this implementation can extract for us.
   */
  trait Extractor[T] {

    val EmptyPath = "\"\""

    /**
     * Extract an object instance of type `T` from the given config at the given path.
     * @param config the Typesafe Config instance.
     * @param path the path at which the value resides.
     * @return the value that was retrieved.
     */
    def extract(config: Config, path: String): Try[T]
    def extract(config: Config): Try[T] = extract(config.atPath(EmptyPath), EmptyPath)

  }

  /**
   * Implementors of this trait how to construct a class of type T from a Typesafe Config instance.
   * @tparam T, the type of class this Configurator knows how to validate/construct.
   */
  trait Configurator[T] extends Extractor[T] {
    protected def validationNel(config: Config): ValidationNel[Throwable, T]
    override def extract(config: Config, path: String): Try[T] =
      Try(config.getConfig(path)).flatMap(validationNel(_))
  }

  /**
   * Adds the extract method to a Typesafe Config instance, allowing us to request values from it like so:
   * {{config.extract[Double]("double")}} or {{config.extract[Option[Range]]("range")}}
   *
   * @param config the Typesafe Config instance to operate on.
   */
  implicit class RichConfig(config: Config) {
    def extract[T: Extractor](path: String): ValidationNel[Throwable, T] =
      tryExtraction(Extractor[T].extract(config, path))

    private def tryExtraction[T](extract: => Try[T]): ValidationNel[Throwable, T] = extract match {
      case Success(value)                => value.success
      case Failure(exception: Throwable) => exception.failureNel
    }

    def extract[T: Extractor]: ValidationNel[Throwable, T] = tryExtraction(Extractor[T].extract(config))
    def validatePath(path: String): ValidationNel[Throwable, String] =
      if (config.hasPath(path)) path.successNel
      else (new Missing(path): Throwable).failureNel
  }

  /**
   * Add a convenience method to convert an Exception to a NonEmptyList[Throwable].
   * @param throwable
   */
  implicit class ThrowableOps(val throwable: Throwable) extends AnyVal {
    def toNel: NonEmptyList[Throwable] = NonEmptyList(throwable)
  }

  /**
   * Implicit conversion from ValidationNel[Exception,T] to Try[T]. Using this construct allows
   * us to keep the Validation logic contained in configuration code, keeping the rest of our code scalaz agnostic.
   */
  import scala.language.implicitConversions
  implicit def validationNelToTry[E <: Throwable, T](validation: ValidationNel[E, T]): Try[T] = validation match {
    case scalaz.Failure(exceptions) => Failure(ConfigurationException(exceptions.list.toList))
    case scalaz.Success(value)      => Success(value)
  }

  /**
   * Encapsulates all configuration exceptions that occurred while trying to map
   * the data from a Typesafe Config object onto a case class.
   */
  case class ConfigurationException(errors: Seq[Throwable]) extends Exception {
    override def toString = s"ConfigurationException[${getMessage()}]"

    override def getMessage: String =
      ("Invalid configuration. Please check the issue(s) listed bellow:" +:
        errors.map(error => s"- ${error.getMessage}")).mkString("\n")
  }

  object Extractor {

    @implicitNotFound("No compatible Extractor in scope for type ${T}")
    def apply[T](implicit T: Extractor[T]): Extractor[T] = T

    implicit val stringExtractor = new Extractor[String] {
      def extract(config: Config, path: String): Try[String] = Try(config.getString(path))
    }

    implicit val booleanExtractor = new Extractor[Boolean] {
      def extract(config: Config, path: String): Try[Boolean] = Try(config.getBoolean(path))
    }

    implicit val characterExtractor = new Extractor[Character] {
      def extract(config: Config, path: String): Try[Character] = Try(config.getString(path).charAt(0))
    }

    implicit val intExtractor = new Extractor[Int] {
      def extract(config: Config, path: String): Try[Int] = Try(config.getInt(path))
    }

    implicit val longExtractor = new Extractor[Long] {
      def extract(config: Config, path: String): Try[Long] = Try(config.getLong(path))
    }

    implicit val doubleExtractor = new Extractor[Double] {
      def extract(config: Config, path: String): Try[Double] = Try(config.getDouble(path))
    }

    implicit val durationExtractor = new Extractor[Duration] {
      def extract(config: Config, path: String): Try[Duration] = Try(config.getDuration(path))
    }

    /** Expected format: <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code> */
    implicit val timestampExtractor = new Extractor[Timestamp] {
      def extract(config: Config, path: String): Try[Timestamp] = Try(Timestamp.valueOf(config.getString(path)))
    }

    /** Expected format: <code>yyyy-[m]m-[d]d</code>*/
    implicit val dateExtractor = new Extractor[Date] {
      def extract(config: Config, path: String): Try[Date] = Try(Date.valueOf(config.getString(path)))
    }

    implicit val localDateExtractor = new Extractor[LocalDate] {
      def extract(config: Config, path: String): Try[LocalDate] = Try(LocalDate.parse(config.getString(path)))
    }

    implicit val localDateTimeExtractor = new Extractor[LocalDateTime] {
      def extract(config: Config, path: String): Try[LocalDateTime] = Try(LocalDateTime.parse(config.getString(path)))
    }

    implicit val anyListExtractor = new Extractor[Seq[Any]] {
      def extract(config: Config, path: String): Try[Seq[Any]] =
        Try(Seq(config.getAnyRefList(path).asScala: _*))
    }

    implicit val stringListExtractor = new Extractor[Seq[String]] {
      def extract(config: Config, path: String): Try[Seq[String]] =
        Try(Seq(config.getStringList(path).asScala: _*))
    }

    implicit val booleanListExtractor = new Extractor[Seq[Boolean]] {
      def extract(config: Config, path: String): Try[Seq[Boolean]] =
        Try(Seq(config.getBooleanList(path).asScala: _*).map(_.booleanValue()))
    }

    implicit val intListExtractor = new Extractor[Seq[Int]] {
      def extract(config: Config, path: String): Try[Seq[Int]] =
        Try(Seq(config.getIntList(path).asScala: _*).map(_.intValue()))
    }

    implicit val longListExtractor = new Extractor[Seq[Long]] {
      def extract(config: Config, path: String): Try[Seq[Long]] =
        Try(Seq(config.getLongList(path).asScala: _*).map(_.longValue()))
    }

    implicit val doubleListExtractor = new Extractor[Seq[Double]] {
      def extract(config: Config, path: String): Try[Seq[Double]] =
        Try(Seq(config.getDoubleList(path).asScala: _*).map(_.doubleValue))
    }

    implicit val configListExtractor = new Extractor[Seq[Config]] {
      def extract(config: Config, path: String): Try[Seq[Config]] =
        Try(Seq(config.getConfigList(path).asScala.map(_.withOnlyPath(path)): _*))
    }

    /**
     * Extract a list of composite types for every T that there is an extractor defined for
     * @param extractor an implicit Extractor[T] that needs to be in scope
     * @tparam T the extracted value
     * @return A Seq(T) if we can extract a valid property of the given type or throw an Exception
     */
    implicit def listExtractor[T](implicit extractor: Extractor[T]): Extractor[Seq[T]] = new Extractor[Seq[T]] {
      override def extract(config: Config, path: String): Try[Seq[T]] = {
        def fromObjects =
          Seq(config.getObjectList(path).asScala: _*)
            .map(co => extractor.extract(co.toConfig.atPath(path), path)).allOkOrFail
        def fromConfigs =
          Seq(config.getConfigList(path).asScala: _*)
            .map(co => extractor.extract(co.atPath(path), path)).allOkOrFail
        def fromList =
          Seq(config.getList(path).asScala: _*)
            .map(co => extractor.extract(co.atPath(path), path)).allOkOrFail
        (fromList orElse fromConfigs orElse fromObjects).map(_.toSeq)
      }
    }

    implicit val anyMapExtractor = new Extractor[Map[String, Any]] {
      def extract(config: Config, path: String): Try[Map[String, Any]] = {
        def fromObject(o: ConfigObject): Set[(String, Any)] =
          Set(
            config
              .getObject(path)
              .entrySet
              .asScala
              .map(e => (e.getKey, e.getValue.unwrapped.asInstanceOf[Any]))
              .toSeq: _*
          )
        def fromObjects: Seq[(String, Any)] = (config.getObjectList(path).asScala).flatMap(fromObject(_))
        def fromList: Seq[(String, Any)] = Seq(config.getList(path).asScala: _*).flatMap { co =>
          val kv = co.unwrapped.asInstanceOf[util.HashMap[_, _]].asScala.toSeq
          kv.map { case (k, v) => (k.toString, v.asInstanceOf[Any]) }
        }
        (Try(fromList) orElse Try(fromObject(config.getObject(path))) orElse Try(fromObjects)).map(_.toMap)
      }
    }

    implicit val stringMapExtractor = new Extractor[Map[String, String]] {
      def extract(config: Config, path: String): Try[Map[String, String]] =
        anyMapExtractor.extract(config, path).map(_.mapValues(_.toString))
    }

    implicit val booleanMapExtractor = new Extractor[Map[String, Boolean]] {
      def extract(config: Config, path: String): Try[Map[String, Boolean]] =
        stringMapExtractor.extract(config, path).map(_.mapValues(_.toBoolean))
    }

    implicit val intMapExtractor = new Extractor[Map[String, Int]] {
      def extract(config: Config, path: String): Try[Map[String, Int]] =
        stringMapExtractor.extract(config, path).map(_.mapValues(_.toInt))
    }

    implicit val longMapExtractor = new Extractor[Map[String, Long]] {
      def extract(config: Config, path: String): Try[Map[String, Long]] =
        stringMapExtractor.extract(config, path).map(_.mapValues(_.toLong))
    }

    implicit val doubleMapExtractor = new Extractor[Map[String, Double]] {
      def extract(config: Config, path: String): Try[Map[String, Double]] =
        stringMapExtractor.extract(config, path).map(_.mapValues(_.toDouble))
    }

    /**
     * Extract a Map with String keys and composite value types for every T that there is an extractor defined for
     * @param extractor an implicit Extractor[T] that needs to be in scope
     * @tparam T the extracted value
     * @return A Seq(T) if we can extract a valid property of the given type or throw an Exception
     */
    implicit def mapExtractor[T](implicit extractor: Extractor[T]): Extractor[Map[String, T]] =
      new Extractor[Map[String, T]] {
        def extract(config: Config, path: String): Try[Map[String, T]] = {
          def fromObject(o: ConfigObject): Try[Map[String, T]] =
              config
                .getObject(path)
                .entrySet
                .asScala
                .map(e => extractor.extract(e.getValue.atPath(e.getKey), e.getKey).map((e.getKey, _)))
                .allOkOrFail
                .map(_.toMap)

          def fromObjects: Try[Map[String, T]] = Try(config.getObjectList(path).asScala).flatMap(obs => obs.map(fromObject(_)).allOkOrFail.map(_.flatten.toMap))
          (fromObject(config.getObject(path)) orElse fromObjects)
        }
      }

    implicit val rangeExtractor = new Extractor[Range] {

      /**
       * Construct a Range from a comma separated list of ints.
       *
       * Case 1: A sequence of ints
       * - first int: 'from'
       * - second int: 'to' (bigger than from)
       * - third int: 'step' a positive number
       *
       * Case 2: A single int
       *
       * @param value -> start,stop,step or just an int value
       * @param path -> optional path for this property
       * @return
       */
      def parseStringToRange(value: String, path: String = "unknown"): Try[Range] =
        Try(value.split(",").map(_.trim.toInt).toSeq) match {
          case Success(start +: stop +: _ +: Nil) if start > stop =>
            Failure(new BadValue(path, "The start should be smaller than the stop."))
          case Success(_ +: _ +: step +: Nil) if step < 0 =>
            Failure( new BadValue(path, "The step should be a positive number."))
          case Success(start +: stop +: step +: Nil) =>
            Success(start to stop by step)
          case Success(v +: Nil) =>
            Success(v to v)
          case _ =>
            Failure(new BadValue(
              path,
              "The input should contain either an integer or a comma separated list of 3 integers."
            ))
        }
      def extract(config: Config, path: String): Try[Range] = parseStringToRange(config.getString(path), path)
    }

    /**
     * Extract an Option[T] for every T that we've defined an extractor for.
     *
     * @param extractor an implicit Extractor[T] that needs to be in scope
     * @tparam T the extracted value
     * @return A Some(T) if we can extract a valid property of the given type or a None otherwise.
     */
    implicit def optionExtractor[T](implicit extractor: Extractor[T]): Extractor[Option[T]] = new Extractor[Option[T]] {
      override def extract(config: Config, path: String): Try[Option[T]] = extractor.extract(config, path) match {
        case Success(value) => Success(Some(value))
        case Failure(_)     => Success(None)
      }
    }

    /**
     * Extract an Either[A, B] for every A or B that we've defined an extractor for.
     *
     * @param leftExtractor an implicit Extractor[T] that needs to be in scope
     * @param rightExtractor an implicit Extractor[T] that needs to be in scope
     * @tparam A the extracted Left value
     * @tparam B the extracted Right value
     * @return A Left(A) if we could extract the A type, or a Right(B) if we could extract the right type or throw an Exception
     */
    implicit def eitherExtractor[A, B](
      implicit leftExtractor: Extractor[A],
      rightExtractor: Extractor[B]
    ): Extractor[Either[A, B]] = new Extractor[Either[A, B]] {
      override def extract(config: Config, path: String): Try[Either[A, B]] =
        (leftExtractor.extract(config, path)).map(Left(_)) orElse (rightExtractor.extract(config, path)).map(Right(_))
    }

  }

}
