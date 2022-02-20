package org.tupol.utils.jdbc


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.sql.ResultSet
import java.util.UUID
import scala.util.Success

class ResultSetOpsExtractColSpec extends AnyWordSpec with Matchers with MockedResultSet {

  "extract a string" should {
    "return successfully a String" in {
      resultSet.extract[String](string_col) shouldBe Success(string_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[String](string_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[String](string_col).isFailure shouldBe true
    }
  }
  "extract a boolean" should {
    "return successfully a true value" in {
      resultSet.extract[Boolean](true_col) shouldBe Success(true_val)
    }
    "return successfully a false" in {
      resultSet.extract[Boolean](false_col) shouldBe Success(false_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Boolean](true_col).isFailure shouldBe true
      resultSetNull.extract[Boolean](false_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Boolean](true_col).isFailure shouldBe true
      resultSetExceptions.extract[Boolean](false_col).isFailure shouldBe true
    }
  }
  "extract an byte" should {
    "return successfully a Byte" in {
      resultSet.extract[Byte](byte_col) shouldBe Success(byte_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Byte](byte_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Byte](byte_col).isFailure shouldBe true
    }
  }
  "extract an array of bytes" should {
    "return successfully an Array[Byte]" in {
      resultSet.extract[Array[Byte]](bytes_col) shouldBe Success(bytes_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Array[Byte]](bytes_col).isFailure shouldBe true

    }
    "return a failure" in {
      resultSetExceptions.extract[Array[Byte]](bytes_col).isFailure shouldBe true
    }
  }
  "extract a short" should {
    "return successfully a Short" in {
      resultSet.extract[Short](short_col) shouldBe Success(short_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Short](short_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Short](short_col).isFailure shouldBe true
    }
  }
  "extract an int" should {
    "return successfully an Int" in {
      resultSet.extract[Int](int_col) shouldBe Success(int_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Int](int_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Int](int_col).isFailure shouldBe true
    }
  }
  "extract a long" should {
    "return successfully a Long" in {
      resultSet.extract[Long](long_col) shouldBe Success(long_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Long](long_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Long](long_col).isFailure shouldBe true
    }
  }
  "extract a float" should {
    "return successfully a Float" in {
      resultSet.extract[Float](float_col) shouldBe Success(float_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Float](float_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Float](float_col).isFailure shouldBe true
    }
  }
  "extract a double" should {
    "return successfully a Double" in {
      resultSet.extract[Double](double_col) shouldBe Success(double_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[Double](double_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Double](double_col).isFailure shouldBe true
    }
  }
  "extract a date" should {
    "return successfully a Double" in {
      resultSet.extract[java.sql.Date](date_col) shouldBe Success(date_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[java.sql.Date](date_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[java.sql.Date](date_col).isFailure shouldBe true
    }
  }
  "extract a time" should {
    "return successfully a Time" in {
      resultSet.extract[java.sql.Time](time_col) shouldBe Success(time_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[java.sql.Time](time_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[java.sql.Time](time_col).isFailure shouldBe true
    }
  }
  "extract a timestamp" should {
    "return successfully a Timestamp" in {
      resultSet.extract[java.sql.Timestamp](timestamp_col) shouldBe Success(timestamp_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[java.sql.Timestamp](timestamp_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[java.sql.Timestamp](timestamp_col).isFailure shouldBe true
    }
  }
  "extract a UUID" should {
    "return successfully the UUID" in {
      resultSet.extract[UUID](uuid_col) shouldBe Success(uuid_val)
    }
    "fail if the value is null" in {
      resultSetNull.extract[UUID](uuid_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[UUID](uuid_col).isFailure shouldBe true
      resultSetExceptions.extract[UUID](non_uuid_col).isFailure shouldBe true
    }
  }
  "extract an option of UUID" should {
    "return successfully Some UUID" in {
      resultSet.extract[Option[UUID]](uuid_col) shouldBe Success(Some(uuid_val))
    }
    "return successfully None" in {
      resultSetNull.extract[Option[UUID]](uuid_col) shouldBe Success(None)
    }
    "return a failure" in {
      resultSetExceptions.extract[Option[UUID]](uuid_col).isFailure shouldBe true
      resultSetExceptions.extract[Option[UUID]](non_uuid_col).isFailure shouldBe true
    }
  }
  "extract Either an UUID or a String" should {
    "return successfully Left UUID" in {
      resultSet.extract[Either[UUID, Int]](uuid_col) shouldBe Success(Left(uuid_val))
    }
    "return successfully Right String" in {
      resultSet.extract[Either[UUID, Int]](int_col) shouldBe Success(Right(int_val))
    }
    "return successfully None" in {
      resultSetNull.extract[Either[UUID, Int]](uuid_col).isFailure shouldBe true
      resultSetNull.extract[Either[UUID, Int]](int_col).isFailure shouldBe true
    }
    "return a failure" in {
      resultSetExceptions.extract[Either[UUID, Int]](uuid_col).isFailure shouldBe true
      resultSetExceptions.extract[Either[UUID, Int]](int_col).isFailure shouldBe true
    }
  }
  "convert a ResultSet to an Iterator[ResultSet]" should {
    "return an empty Iterator if the ResultSet is empty" in {
      val rs = mock[ResultSet]
      rs.next answers false
      rs.toIterator.isEmpty shouldBe true
    }
    "return an Iterator of one element if the ResultSet has one element" in {
      val rs = mock[ResultSet]
      rs.next answers true andThenAnswer false
      rs.getString(*[String]) answers "a1"
      val result = rs.toIterator.toSeq
      result.length shouldBe 1
      result.map(_.extract[String]("")) shouldBe Seq(Success("a1"))
    }
    "return an Iterator of one element if the ResultSet has two elements" in {
      val rs = mock[ResultSet]
      rs.next answers true andThenAnswer true andThenAnswer false
      rs.getString(*[String]) answers "a1" andThenAnswer "a2"
      val result = rs.toIterator.toSeq
      result.length shouldBe 2
      result.map(_.extract[String]("")) shouldBe Seq(Success("a1"), Success("a2"))
    }
  }
}
