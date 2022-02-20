package org.tupol.utils.jdbc

import java.sql.{ ResultSet, SQLException, Timestamp }
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.{ ArgumentMatchersSugar, IdiomaticMockito }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{ Success, Try }

class RowExtractorsSpec extends AnyWordSpec with Matchers with IdiomaticMockito with ArgumentMatchersSugar {

  import extractors._
  import RowExtractorsSpec._

  val COL1 = "col1"
  val COL2 = "col2"
  val COL3 = "col3"

  val someClassReference  = SomeClass("other random string", Int.MaxValue, UUID.randomUUID())
  val otherClassReference = OtherClass(Int.MaxValue, "some random string")

  implicit val someClassExtractor = new RowExtractor[SomeClass] {
    def extract(rs: ResultSet): Try[SomeClass] =
      for {
        col1 <- rs.extract[String](COL1)
        col2 <- rs.extract[Int](COL2)
        col3 <- rs.extract[UUID](COL3)
      } yield SomeClass(col1, col2, col3)
  }

  implicit val otherClassExtractor = new RowExtractor[OtherClass] {
    def extract(rs: ResultSet): Try[OtherClass] =
      for {
        col1 <- rs.extract[Int](COL1)
        col2 <- rs.extract[String](COL2)
      } yield OtherClass(col1, col2)
  }

  "extract a SomeClass" should {
    "return successfully the SomeClass" in {
      resultSetSomeClassOk.extract[SomeClass].get === someClassReference
    }
    "fail if a column is null" in {
      resultSetSomeClassBadUUID.extract[SomeClass].isFailure shouldBe true
    }
    "fail if all columns are null" in {
      resultSetNull.extract[SomeClass].isFailure shouldBe true
    }
    "fail if there was an exception extracting the data" in {
      resultSetExceptions.extract[SomeClass].isFailure shouldBe true
    }
  }
  "extract an optional of SomeClass" should {
    "return Some of SomeClass if there is enough data" in {
      resultSetSomeClassOk.extract[Option[SomeClass]] === Success(someClassReference)
    }
    "return None of SomeClass if all the data is null" in {
      resultSetNull.extract[Option[SomeClass]] shouldBe Success(None)
    }
    "fail if there is a missing column" in {
      resultSetSomeClassBadUUID.extract[Option[SomeClass]].isFailure shouldBe true
    }
    "fail if there are exceptions" in {
      resultSetExceptions.extract[Option[SomeClass]].isFailure shouldBe true
    }
  }
  "extract Either a SomeClass or an OtherClass" should {
    "return Left of SomeClass if there is enough data" in {
      resultSetSomeClassOk.extract[Either[SomeClass, OtherClass]] === Success(Left(someClassReference))
    }
    "return Right of OtherClass if there is enough data and no IntegrityBlockDBLevel data" in {
      resultSetOtherClassOk.extract[Either[SomeClass, OtherClass]] === Success(Right(otherClassReference))
    }
    "fail if there is just OtherClass data which is missing a column" in {
      resultSetOtherClassMissingCol2.extract[Either[SomeClass, OtherClass]].isFailure shouldBe true
    }
    "fail if everything is null" in {
      resultSetNull.extract[Either[SomeClass, OtherClass]].isFailure shouldBe true
    }
    "fail if there are exceptions" in {
      resultSetExceptions.extract[Either[SomeClass, OtherClass]].isFailure shouldBe true
    }
  }

  val testBytes         = "abc".getBytes
  val testTimestamp     = Timestamp.valueOf(LocalDateTime.of(1970, 12, 31, 23, 59, 58, 57))
  val testUUID          = UUID.randomUUID()
  val testString        = "some other content"
  val OTHER_CONTENT_COL = "other_content_col"

  def resultSetSomeClassOk: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.getString(COL1) answers someClassReference.col1
    resultSet.getInt(COL2) answers someClassReference.col2
    resultSet.getObject(COL3) answers someClassReference.col3
  }
  def resultSetSomeClassBadUUID: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.getString(COL1) answers someClassReference.col1
    resultSet.getInt(COL2) answers someClassReference.col2
    resultSet.getObject(COL3) answers "bad_uuid"
  }
  def resultSetOtherClassOk: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.getInt(COL1) answers otherClassReference.col1
    resultSet.getString(COL2) answers otherClassReference.col2
  }
  def resultSetOtherClassMissingCol2: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.wasNull answers false andThenAnswer true
    resultSet.getInt(COL1) answers otherClassReference.col1
    resultSet.getString(COL2) answers null.asInstanceOf[String]
  }
  def resultSetExceptions: ResultSet = {
    val resultSet = mock[ResultSet]
    val exception = new SQLException("SQLException")
    resultSet.getInt(*[String]) throws exception
    resultSet.getString(*[String]) throws exception
    resultSet.getObject(*[String]) throws exception
    resultSet
  }
  def resultSetNull: ResultSet = {
    val resultSet = mock[ResultSet]
    resultSet.wasNull answers true andThenAnswer true andThenAnswer true
    resultSet.getInt(*[String]) answers null.asInstanceOf[Int]
    resultSet.getString(*[String]) answers null.asInstanceOf[String]
    resultSet.getObject(*[String]) answers null.asInstanceOf[Object]
    resultSet
  }
}
object RowExtractorsSpec {
  case class SomeClass(col1: String, col2: Int, col3: UUID)
  case class OtherClass(col1: Int, col2: String)
}
