package org.tupol.utils.jdbc

import java.sql.PreparedStatement
import java.time.{ LocalDate, LocalDateTime, LocalTime }

import scala.util.Try

/** A `SqlParam` is a typed parameter that know how to set itself into a `PreparedStatement` */
abstract class SqlParam[V](val value: V) {
  def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement]
}
object SqlParam {
  case class StringSqlParam(override val value: String) extends SqlParam[String](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setString(index, value); stmt }
  }
  case class BooleanSqlParam(override val value: Boolean) extends SqlParam[Boolean](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setBoolean(index, value); stmt }
  }
  case class ByteSqlParam(override val value: Byte) extends SqlParam[Byte](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setByte(index, value); stmt }
  }
  case class ShortSqlParam(override val value: Short) extends SqlParam[Short](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setShort(index, value); stmt }
  }
  case class IntSqlParam(override val value: Int) extends SqlParam[Int](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setInt(index, value); stmt }
  }
  case class LongSqlParam(override val value: Long) extends SqlParam[Long](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setLong(index, value); stmt }
  }
  case class FloatSqlParam(override val value: Float) extends SqlParam[Float](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setFloat(index, value); stmt }
  }
  case class DoubleSqlParam(override val value: Double) extends SqlParam[Double](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setDouble(index, value); stmt }
  }
  case class LocalDateSqlParam(override val value: LocalDate) extends SqlParam[LocalDate](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] =
      Try { stmt.setDate(index, java.sql.Date.valueOf(value)); stmt }
  }
  case class LocalTimeSqlParam(override val value: LocalTime) extends SqlParam[LocalTime](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] =
      Try { stmt.setTime(index, java.sql.Time.valueOf(value)); stmt }
  }
  case class LocalDateTimeSqlParam(override val value: LocalDateTime) extends SqlParam[LocalDateTime](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] =
      Try { stmt.setTimestamp(index, java.sql.Timestamp.valueOf(value)); stmt }
  }
  case class ArraySqlParam(override val value: Array[Byte]) extends SqlParam[Array[Byte]](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] = Try { stmt.setBytes(index, value); stmt }
  }
  case class IterableSqlParam(override val value: Iterable[_]) extends SqlParam[Iterable[_]](value) {
    def set(stmt: PreparedStatement, index: Int): Try[PreparedStatement] =
      Try { stmt.setArray(index, stmt.getConnection.createArrayOf("varchar", value.map(_.toString).toArray)); stmt }
  }

  def apply(value: String): SqlParam[String]               = StringSqlParam(value)
  def apply(value: Boolean): SqlParam[Boolean]             = BooleanSqlParam(value)
  def apply(value: Byte): SqlParam[Byte]                   = ByteSqlParam(value)
  def apply(value: Short): SqlParam[Short]                 = ShortSqlParam(value)
  def apply(value: Int): SqlParam[Int]                     = IntSqlParam(value)
  def apply(value: Long): SqlParam[Long]                   = LongSqlParam(value)
  def apply(value: Float): SqlParam[Float]                 = FloatSqlParam(value)
  def apply(value: Double): SqlParam[Double]               = DoubleSqlParam(value)
  def apply(value: LocalDate): SqlParam[LocalDate]         = LocalDateSqlParam(value)
  def apply(value: LocalTime): SqlParam[LocalTime]         = LocalTimeSqlParam(value)
  def apply(value: LocalDateTime): SqlParam[LocalDateTime] = LocalDateTimeSqlParam(value)
  def apply(value: Array[Byte]): SqlParam[Array[Byte]]     = ArraySqlParam(value)
  def apply(value: Iterable[_]): SqlParam[Iterable[_]]     = IterableSqlParam(value)

  /** Create a `SqlParam` out of an unknown type. This is useful to map collections of mixed types.
   * For any any know type one should refer to the default `SqlParam` constructors. */
  def fromAny(value: Any): SqlParam[_] =
    value match {
      case x: String        => SqlParam(x)
      case x: Boolean       => SqlParam(x)
      case x: Byte          => SqlParam(x)
      case x: Short         => SqlParam(x)
      case x: Int           => SqlParam(x)
      case x: Long          => SqlParam(x)
      case x: Float         => SqlParam(x)
      case x: Double        => SqlParam(x)
      case x: LocalDate     => SqlParam(x)
      case x: LocalTime     => SqlParam(x)
      case x: LocalDateTime => SqlParam(x)
      case x: Array[Byte]   => SqlParam(x)
      case x: Iterable[_]   => SqlParam(x)
      case _                => StringSqlParam(value.toString) // Not a great idea, but this is the legacy behavior
    }
}
