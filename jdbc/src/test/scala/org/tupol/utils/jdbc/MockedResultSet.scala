package org.tupol.utils.jdbc

import org.mockito.{ ArgumentMatchersSugar, IdiomaticMockito }

import java.sql.{ Date, ResultSet, SQLException, Time, Timestamp }
import java.time.{ LocalDate, LocalDateTime, LocalTime }
import java.util.UUID

trait MockedResultSet extends IdiomaticMockito with ArgumentMatchersSugar {

  val (string_col, string_col_idx, string_val)          = ("string_col", 1, "some simple string")
  val (byte_col, byte_col_idx, byte_val)                = ("byte_col", 2, Byte.MaxValue)
  val (bytes_col, bytes_col_idx, bytes_val)             = ("bytes_col", 3, "abc".getBytes)
  val (short_col, short_col_idx, short_val)             = ("short_col", 4, Short.MaxValue)
  val (int_col, int_col_idx, int_val)                   = ("int_col", 5, Int.MaxValue)
  val (long_col, long_col_idx, long_val)                = ("long_col", 6, Long.MaxValue)
  val (true_col, true_col_idx, true_val)                = ("true_col", 7, true)
  val (false_col, false_col_idx, false_val)             = ("false_col", 8, false)
  val (float_col, float_col_idx, float_val)             = ("float_col", 9, Float.MaxValue)
  val (double_col, double_col_idx, double_val)          = ("double_col", 10, Double.MaxValue)
  val (date_col, date_col_idx, date_val)                = ("date_col", 11, java.sql.Date.valueOf(LocalDate.of(1970, 12, 31)))
  val (time_col, time_col_idx, time_val)                = ("time_col", 12, java.sql.Time.valueOf(LocalTime.of(23, 59, 48)))
  val (timestamp_col, timestamp_col_idx, timestamp_val) =
    ("timestamp_col", 13, java.sql.Timestamp.valueOf(LocalDateTime.of(date_val.toLocalDate, time_val.toLocalTime)))
  val (uuid_col, uuid_col_idx, uuid_val)                = ("uuid_col", 14, UUID.randomUUID())
  val (non_uuid_col, non_uuid_col_idx, non_uuid_val)    = ("non_uuid_col", 15, "")

  val sqlException = new SQLException("SQLException")

  def resultSet: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.getString(string_col) answers string_val
    resultSet.getByte(byte_col) answers byte_val
    resultSet.getBytes(bytes_col) answers bytes_val
    resultSet.getShort(short_col) answers short_val
    resultSet.getInt(int_col) answers int_val
    resultSet.getLong(long_col) answers long_val
    resultSet.getFloat(float_col) answers float_val
    resultSet.getDouble(double_col) answers double_val
    resultSet.getBoolean(true_col) answers true_val
    resultSet.getBoolean(false_col) answers false_val
    resultSet.getDate(date_col) answers date_val
    resultSet.getTime(time_col) answers time_val
    resultSet.getTimestamp(timestamp_col) answers timestamp_val
    resultSet.getObject(uuid_col) answers uuid_val
    resultSet.getObject(non_uuid_col) answers non_uuid_val

    resultSet.getString(string_col_idx) answers string_val
    resultSet.getByte(byte_col_idx) answers byte_val
    resultSet.getBytes(bytes_col_idx) answers bytes_val
    resultSet.getShort(short_col_idx) answers short_val
    resultSet.getInt(int_col_idx) answers int_val
    resultSet.getLong(long_col_idx) answers long_val
    resultSet.getFloat(float_col_idx) answers float_val
    resultSet.getDouble(double_col_idx) answers double_val
    resultSet.getBoolean(true_col_idx) answers true_val
    resultSet.getBoolean(false_col_idx) answers false_val
    resultSet.getDate(date_col_idx) answers date_val
    resultSet.getTime(time_col_idx) answers time_val
    resultSet.getTimestamp(timestamp_col_idx) answers timestamp_val
    resultSet.getObject(uuid_col_idx) answers uuid_val
    resultSet.getObject(non_uuid_col_idx) answers non_uuid_val

  }
  def resultSetExceptions: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.getString(*[String]) throws sqlException
    resultSet.getByte(*[String]) throws sqlException
    resultSet.getBytes(*[String]) throws sqlException
    resultSet.getShort(*[String]) throws sqlException
    resultSet.getInt(*[String]) throws sqlException
    resultSet.getLong(*[String]) throws sqlException
    resultSet.getFloat(*[String]) throws sqlException
    resultSet.getDouble(*[String]) throws sqlException
    resultSet.getBoolean(*[String]) throws sqlException
    resultSet.getDate(*[String]) throws sqlException
    resultSet.getTime(*[String]) throws sqlException
    resultSet.getTimestamp(*[String]) throws sqlException
    resultSet.getObject(*[String]) throws sqlException

    resultSet.getString(*[Int]) throws sqlException
    resultSet.getByte(*[Int]) throws sqlException
    resultSet.getBytes(*[Int]) throws sqlException
    resultSet.getShort(*[Int]) throws sqlException
    resultSet.getInt(*[Int]) throws sqlException
    resultSet.getLong(*[Int]) throws sqlException
    resultSet.getFloat(*[Int]) throws sqlException
    resultSet.getDouble(*[Int]) throws sqlException
    resultSet.getBoolean(*[Int]) throws sqlException
    resultSet.getDate(*[Int]) throws sqlException
    resultSet.getTime(*[Int]) throws sqlException
    resultSet.getTimestamp(*[Int]) throws sqlException
    resultSet.getObject(*[Int]) throws sqlException
  }
  def resultSetNull: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.wasNull answers true andThenAnswer true // Useful for Either extractor
    resultSet.getString(*[String]) answers null.asInstanceOf[String]
    resultSet.getByte(*[String]) answers null.asInstanceOf[Byte]
    resultSet.getBytes(*[String]) answers null.asInstanceOf[Array[Byte]]
    resultSet.getShort(*[String]) answers null.asInstanceOf[Short]
    resultSet.getInt(*[String]) answers null.asInstanceOf[Int]
    resultSet.getLong(*[String]) answers null.asInstanceOf[Long]
    resultSet.getFloat(*[String]) answers null.asInstanceOf[Float]
    resultSet.getDouble(*[String]) answers null.asInstanceOf[Double]
    resultSet.getBoolean(*[String]) answers null.asInstanceOf[Boolean]
    resultSet.getDate(*[String]) answers null.asInstanceOf[Date]
    resultSet.getTime(*[String]) answers null.asInstanceOf[Time]
    resultSet.getTimestamp(*[String]) answers null.asInstanceOf[Timestamp]
    resultSet.getObject(*[String]) answers null.asInstanceOf[Object]

    resultSet.getString(*[Int]) answers null.asInstanceOf[String]
    resultSet.getByte(*[Int]) answers null.asInstanceOf[Byte]
    resultSet.getBytes(*[Int]) answers null.asInstanceOf[Array[Byte]]
    resultSet.getShort(*[Int]) answers null.asInstanceOf[Short]
    resultSet.getInt(*[Int]) answers null.asInstanceOf[Int]
    resultSet.getLong(*[Int]) answers null.asInstanceOf[Long]
    resultSet.getFloat(*[Int]) answers null.asInstanceOf[Float]
    resultSet.getDouble(*[Int]) answers null.asInstanceOf[Double]
    resultSet.getBoolean(*[Int]) answers null.asInstanceOf[Boolean]
    resultSet.getDate(*[Int]) answers null.asInstanceOf[Date]
    resultSet.getTime(*[Int]) answers null.asInstanceOf[Time]
    resultSet.getTimestamp(*[Int]) answers null.asInstanceOf[Timestamp]
    resultSet.getObject(*[Int]) answers null.asInstanceOf[Object]
  }
}
