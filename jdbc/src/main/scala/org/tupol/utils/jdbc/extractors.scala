package org.tupol.utils.jdbc

import java.sql.{ ResultSet, SQLException }
import java.util.UUID
import scala.util.{ Failure, Success, Try }

object extractors {

  /** `ResultSet` named column types extractor. */
  trait Extractor[T] {

    /** Extract typed date out of a `ResultSet` column. */
    def extract(rs: ResultSet, col: String): Try[T]

    /** Extract typed date out of a `ResultSet` column. */
    def extract(rs: ResultSet, index: Int): Try[T]
  }
  object Extractor {

    case class UnexpectedNullValue(col: String)
        extends SQLException(
          s"""Column "$col" contains a null value. Consider extracting it with extract[Option[T]]($col)."""
        )

    private def tryGetAndFailWhenNull[T](rs: ResultSet, colLabel: String, getter: => T): Try[T] =
      for {
        nullableValule <- Try(getter)
        isNull          = Try(rs.wasNull()).getOrElse(false) // Interesting question: Should we fail if this call fails?
        value          <- (Option(nullableValule), isNull) match {
                            case (Some(x), false) => Success(x)
                            case (_, _)           => Failure[T](UnexpectedNullValue(colLabel))
                          }
      } yield value

    implicit object StringExtractor    extends Extractor[String]             {
      def extract(rs: ResultSet, col: String): Try[String] = tryGetAndFailWhenNull(rs, col, rs.getString(col))
      def extract(rs: ResultSet, col: Int): Try[String]    = tryGetAndFailWhenNull(rs, col.toString, rs.getString(col))
    }
    implicit object BoolExtractor      extends Extractor[Boolean]            {
      def extract(rs: ResultSet, col: String): Try[Boolean] = tryGetAndFailWhenNull(rs, col, rs.getBoolean(col))
      def extract(rs: ResultSet, col: Int): Try[Boolean]    = tryGetAndFailWhenNull(rs, col.toString, rs.getBoolean(col))
    }
    implicit object ByteExtractor      extends Extractor[Byte]               {
      def extract(rs: ResultSet, col: String): Try[Byte] = tryGetAndFailWhenNull(rs, col, rs.getByte(col))
      def extract(rs: ResultSet, col: Int): Try[Byte]    = tryGetAndFailWhenNull(rs, col.toString, rs.getByte(col))
    }
    implicit object BytesExtractor     extends Extractor[Array[Byte]]        {
      def extract(rs: ResultSet, col: String): Try[Array[Byte]] = tryGetAndFailWhenNull(rs, col, rs.getBytes(col))
      def extract(rs: ResultSet, col: Int): Try[Array[Byte]]    = tryGetAndFailWhenNull(rs, col.toString, rs.getBytes(col))
    }
    implicit object ShortExtractor     extends Extractor[Short]              {
      def extract(rs: ResultSet, col: String): Try[Short] = tryGetAndFailWhenNull(rs, col, rs.getShort(col))
      def extract(rs: ResultSet, col: Int): Try[Short]    = tryGetAndFailWhenNull(rs, col.toString, rs.getShort(col))
    }
    implicit object IntExtractor       extends Extractor[Int]                {
      def extract(rs: ResultSet, col: String): Try[Int] = tryGetAndFailWhenNull(rs, col, rs.getInt(col))
      def extract(rs: ResultSet, col: Int): Try[Int]    = tryGetAndFailWhenNull(rs, col.toString, rs.getInt(col))
    }
    implicit object LongExtractor      extends Extractor[Long]               {
      def extract(rs: ResultSet, col: String): Try[Long] = tryGetAndFailWhenNull(rs, col, rs.getLong(col))
      def extract(rs: ResultSet, col: Int): Try[Long]    = tryGetAndFailWhenNull(rs, col.toString, rs.getLong(col))
    }
    implicit object FloatExtractor     extends Extractor[Float]              {
      def extract(rs: ResultSet, col: String): Try[Float] = tryGetAndFailWhenNull(rs, col, rs.getFloat(col))
      def extract(rs: ResultSet, col: Int): Try[Float]    = tryGetAndFailWhenNull(rs, col.toString, rs.getFloat(col))
    }
    implicit object DoubleExtractor    extends Extractor[Double]             {
      def extract(rs: ResultSet, col: String): Try[Double] = tryGetAndFailWhenNull(rs, col, rs.getDouble(col))
      def extract(rs: ResultSet, col: Int): Try[Double]    = tryGetAndFailWhenNull(rs, col.toString, rs.getDouble(col))
    }
    implicit object DateExtractor      extends Extractor[java.sql.Date]      {
      def extract(rs: ResultSet, col: String): Try[java.sql.Date] = tryGetAndFailWhenNull(rs, col, rs.getDate(col))
      def extract(rs: ResultSet, col: Int): Try[java.sql.Date]    =
        tryGetAndFailWhenNull(rs, col.toString, rs.getDate(col))
    }
    implicit object TimeExtractor      extends Extractor[java.sql.Time]      {
      def extract(rs: ResultSet, col: String): Try[java.sql.Time] = tryGetAndFailWhenNull(rs, col, rs.getTime(col))
      def extract(rs: ResultSet, col: Int): Try[java.sql.Time]    =
        tryGetAndFailWhenNull(rs, col.toString, rs.getTime(col))
    }
    implicit object TimestampExtractor extends Extractor[java.sql.Timestamp] {
      def extract(rs: ResultSet, col: String): Try[java.sql.Timestamp] =
        tryGetAndFailWhenNull(rs, col, rs.getTimestamp(col))
      def extract(rs: ResultSet, col: Int): Try[java.sql.Timestamp]    =
        tryGetAndFailWhenNull(rs, col.toString, rs.getTimestamp(col))
    }
    implicit object UuidExtractor      extends Extractor[UUID]               {
      def extract(rs: ResultSet, col: String): Try[UUID] =
        tryGetAndFailWhenNull(rs, col, UUID.fromString(rs.getObject(col).toString))
      def extract(rs: ResultSet, col: Int): Try[UUID]    =
        tryGetAndFailWhenNull(rs, col.toString, UUID.fromString(rs.getObject(col).toString))
    }
    implicit def OptionExtractor[T](implicit extractor: Extractor[T]): Extractor[Option[T]] =
      new Extractor[Option[T]] {
        override def extract(rs: ResultSet, col: String): Try[Option[T]] = {
          val result = extractor.extract(rs, col).map(Option(_))
          val isNull = Try(rs.wasNull()).getOrElse(false) // Interesting question: Should we fail if this call fails?
          if (isNull) Success(None) else result
        }
        override def extract(rs: ResultSet, col: Int): Try[Option[T]] = {
          val result = extractor.extract(rs, col).map(Option(_))
          val isNull = Try(rs.wasNull()).getOrElse(false) // Interesting question: Should we fail if this call fails?
          if (isNull) Success(None) else result
        }
      }
    implicit def EitherExtractor[A, B](implicit
        aExtractor: Extractor[A],
        bExtractor: Extractor[B]
    ): Extractor[Either[A, B]] =
      new Extractor[Either[A, B]] {
        override def extract(rs: ResultSet, col: String): Try[Either[A, B]] =
          aExtractor.extract(rs, col).map(Left(_)) orElse
            bExtractor.extract(rs, col).map(Right(_))
        override def extract(rs: ResultSet, col: Int): Try[Either[A, B]]    =
          aExtractor.extract(rs, col).map(Left(_)) orElse
            bExtractor.extract(rs, col).map(Right(_))
      }
  }

  /** `ResultSet` typed structures extractor. */
  trait RowExtractor[T] {

    /** Extract typed structures out of a `ResultSet`. */
    def extract(rs: ResultSet): Try[T]
  }
  object RowExtractor {
    implicit def OptionExtractor[T](implicit extractor: RowExtractor[T]): RowExtractor[Option[T]] =
      new RowExtractor[Option[T]] {
        override def extract(rs: ResultSet): Try[Option[T]] = {
          val result = extractor.extract(rs).map(Option(_))
          val isNull = Try(rs.wasNull()).getOrElse(false) // Interesting question: Should we fail if this call fails?
          if (isNull) Success(None) else result
        }
      }
    implicit def EitherExtractor[A, B](implicit
        aExtractor: RowExtractor[A],
        bExtractor: RowExtractor[B]
    ): RowExtractor[Either[A, B]]                                                                 =
      new RowExtractor[Either[A, B]] {
        override def extract(rs: ResultSet): Try[Either[A, B]] =
          aExtractor.extract(rs).map(Left(_)) orElse
            bExtractor.extract(rs).map(Right(_))
      }
  }

}
