package org.tupol.utils.jdbc

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.sql.{Connection, PreparedStatement}
import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.UUID

class SqlParamSpec extends AnyWordSpec with Matchers with MockedResultSet {

  "SqlParam.set" should {
    val stmt = mock[PreparedStatement]
    "setBoolean" in {
      SqlParam(true).set(stmt, 0)
      stmt.setBoolean(0, true) wasCalled once
    }
    "setShort" in {
      SqlParam(1.toShort).set(stmt, 0)
      stmt.setShort(0, 1) wasCalled once
    }
    "setInt" in {
      SqlParam(1).set(stmt, 0)
      stmt.setInt(0, 1) wasCalled once
    }
    "setLong" in {
      SqlParam(1L).set(stmt, 0)
      stmt.setLong(0, 1) wasCalled once
    }
    "setFloat" in {
      SqlParam(1.1f).set(stmt, 0)
      stmt.setFloat(0, 1.1f) wasCalled once
    }
    "setDouble" in {
      SqlParam(1.1).set(stmt, 0)
      stmt.setDouble(0, 1.1) wasCalled once
    }
    "setDate" in {
      val param = LocalDate.of(1970, 12, 30)
      SqlParam(param).set(stmt, 0)
      stmt.setDate(0, java.sql.Date.valueOf(param)) wasCalled once
    }
    "setTime" in {
      val param = LocalTime.of(23, 59, 59)
      SqlParam(param).set(stmt, 0)
      stmt.setTime(0, java.sql.Time.valueOf(param)) wasCalled once
    }
    "setTimestamp" in {
      val param = LocalDateTime.of(1970, 12, 30, 23, 59, 59)
      SqlParam(param).set(stmt, 0)
      stmt.setTimestamp(0, java.sql.Timestamp.valueOf(param)) wasCalled once
    }
    "setByte" in {
      SqlParam(1.toByte).set(stmt, 0)
      stmt.setByte(0, 1.toByte) wasCalled once
    }
    "setBytes" in {
      SqlParam("1".getBytes).set(stmt, 0)
      stmt.setBytes(0, "1".getBytes) wasCalled once
    }
    "setArray" in {
      val con = mock[Connection]
      val arr = mock[java.sql.Array]
      con.createArrayOf(*, *) returns arr
      stmt.getConnection returns con
      SqlParam(Seq(1, 2, 3)).set(stmt, 0)
      stmt.setArray(0, *) wasCalled once
    }
    "setString" in {
      SqlParam("1").set(stmt, 0)
      stmt.setString(0, "1") wasCalled once
    }
    "setString for undefined types" in {
      val param = UUID.randomUUID()
      SqlParam.fromAny(param).set(stmt, 0)
      stmt.setString(0, param.toString) wasCalled once
    }
  }

  "SqlParam.from" should {
    val stmt = mock[PreparedStatement]
    "setBoolean" in {
      SqlParam.fromAny(true) shouldBe SqlParam(true)
    }
    "setShort" in {
      SqlParam.fromAny(1.toShort) shouldBe SqlParam(1.toShort)
    }
    "setInt" in {
      SqlParam.fromAny(1) shouldBe SqlParam(1)
    }
    "setLong" in {
      SqlParam.fromAny(1L) shouldBe SqlParam(1L)
    }
    "setFloat" in {
      SqlParam.fromAny(1.1f) shouldBe SqlParam(1.1f)
    }
    "setDouble" in {
      SqlParam.fromAny(1.1) shouldBe SqlParam(1.1)
    }
    "setDate" in {
      val param = LocalDate.of(1970, 12, 30)
      SqlParam.fromAny(param) shouldBe SqlParam(param)
    }
    "setTime" in {
      val param = LocalTime.of(23, 59, 59)
      SqlParam.fromAny(param) shouldBe SqlParam(param)
    }
    "setTimestamp" in {
      val param = LocalDateTime.of(1970, 12, 30, 23, 59, 59)
      SqlParam.fromAny(param) shouldBe SqlParam(param)
    }
    "setByte" in {
      SqlParam.fromAny(1.toByte) shouldBe SqlParam(1.toByte)
    }
    "setBytes" in {
      SqlParam.fromAny("1".getBytes) === SqlParam("1".getBytes)
    }
    "setArray" in {
      val con = mock[Connection]
      val arr = mock[java.sql.Array]
      con.createArrayOf(*, *) returns arr
      stmt.getConnection returns con
      SqlParam.fromAny(Seq(1, 2, 3)) === SqlParam(Seq(1, 2, 3))
    }
    "setString" in {
      SqlParam.fromAny("1") shouldBe SqlParam("1")
    }
    "setString for undefined types" in {
      val param = UUID.randomUUID()
      SqlParam.fromAny(param) shouldBe SqlParam(param.toString)
    }
  }

}
