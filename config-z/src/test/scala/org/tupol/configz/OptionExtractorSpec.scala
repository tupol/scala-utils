package org.tupol.configz

import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scalaz.syntax.applicative._

import scala.collection.JavaConverters._
import scala.util.{ Success, Try }

class OptionExtractorSpec extends AnyFunSuite with Matchers {

  case class ComplexExample(
      char: Character,
      str: String,
      bool: Boolean,
      dbl: Double,
      in: Int,
      lng: Long,
      optChar: Option[Character],
      optStr: Option[String],
      optBool: Option[Boolean],
      optDouble: Option[Double],
      optInt: Option[Int],
      optLong: Option[Long]
  )

  test("Missing optional values should not result in error") {
    val complexInstance                       = ComplexExample('*', "string", false, 0.9d, 23, 12L, None, None, None, None, None, None)
    val config                                = ConfigFactory.parseMap(
      Map("char" -> "*", "str" -> "string", "bool" -> "false", "dbl" -> "0.9", "in" -> "23", "lng" -> "12").asJava
    )
    val validationResult: Try[ComplexExample] =
      config.extract[Character]("char") |@| config.extract[String]("str") |@|
        config.extract[Boolean]("bool") |@| config.extract[Double]("dbl") |@|
        config.extract[Int]("in") |@| config.extract[Long]("lng") |@|
        config.extract[Option[Character]]("optchar") |@| config.extract[Option[String]]("optstr") |@|
        config.extract[Option[Boolean]]("optbool") |@| config.extract[Option[Double]]("optdbl") |@|
        config.extract[Option[Int]]("optin") |@| config.extract[Option[Long]]("optlng") apply ComplexExample.apply

    validationResult shouldEqual Success(complexInstance)
  }

  test("Optional values should be parsed when present") {
    val complexInstance                       = ComplexExample(
      '*',
      "string",
      false,
      0.9d,
      23,
      12L,
      Some('*'),
      Some("string"),
      Some(false),
      Some(0.9d),
      Some(23),
      Some(12L)
    )
    val config                                = ConfigFactory.parseMap(
      Map(
        "char"    -> "*",
        "str"     -> "string",
        "bool"    -> "false",
        "dbl"     -> "0.9",
        "in"      -> "23",
        "lng"     -> "12",
        "optchar" -> "*",
        "optstr"  -> "string",
        "optbool" -> "false",
        "optdbl"  -> "0.9",
        "optin"   -> "23",
        "optlng"  -> "12"
      ).asJava
    )
    val validationResult: Try[ComplexExample] =
      config.extract[Character]("char") |@| config.extract[String]("str") |@| config
        .extract[Boolean]("bool") |@| config.extract[Double]("dbl") |@|
        config.extract[Int]("in") |@| config.extract[Long]("lng") |@| config.extract[Option[Character]](
        "optchar") |@| config
        .extract[Option[String]]("optstr") |@|
        config.extract[Option[Boolean]]("optbool") |@| config.extract[Option[Double]]("optdbl") |@| config
        .extract[Option[Int]]("optin") |@| config.extract[Option[Long]]("optlng") apply ComplexExample.apply

    validationResult shouldEqual Success(complexInstance)
  }

  case class SimpleExample(char: Character, bool: Boolean, in: Int)

  test("Missing non optional values should result in a Failure") {
    val config                               = ConfigFactory.parseMap(Map("char" -> "*", "in" -> "23").asJava)
    val validationResult: Try[SimpleExample] =
      config.extract[Character]("char") |@| config.extract[Boolean]("bool") |@| config
        .extract[Int]("in") apply SimpleExample.apply

    validationResult.failed.map(_.getMessage).get should include("No configuration setting found for key 'bool'")
  }

  test("Non optional values of the wrong type should result in a Failure") {
    val config                               = ConfigFactory.parseMap(Map("char" -> "*", "bool" -> "nee", "in" -> "234,34").asJava)
    val validationResult: Try[SimpleExample] =
      config.extract[Character]("char") |@| config.extract[Boolean]("bool") |@| config
        .extract[Int]("in") apply SimpleExample.apply
    validationResult.failed.map(_.getMessage).get should include(
      "hardcoded value: bool has type STRING rather than BOOLEAN"
    )
    validationResult.failed.map(_.getMessage).get should include(
      "hardcoded value: in has type STRING rather than NUMBER"
    )
  }

}
