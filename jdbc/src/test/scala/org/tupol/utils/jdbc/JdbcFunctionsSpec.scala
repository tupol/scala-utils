package org.tupol.utils.jdbc

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.tupol.utils.jdbc.extractors.RowExtractor

import java.sql.{ Connection, ResultSet, SQLException }
import scala.util.{ Failure, Success, Try }

object JdbcFunctionsSpec {

  implicit object DummyExtractor extends RowExtractor[String] {
    override def extract(rs: ResultSet): Try[String] =
      rs.extract[String]("it does not matter")
  }
}

class JdbcFunctionsSpec extends AnyWordSpec with Matchers with MockedResultSet {

  import JdbcFunctionsSpec._

  "update" should {
    "return successfully" in {
      val conn = mock[java.sql.Connection]
      val stmt = mock[java.sql.Statement]
      stmt.executeUpdate(*[String]) answers 123
      conn.createStatement answers stmt
      update(conn, "some sql stmt") shouldBe Success(123)
    }
    "return a SQLException if the statement can not be created due to a SqlException" in {
      val conn      = mock[java.sql.Connection]
      val exception = new SQLException("Something went wrong")
      conn.createStatement throws exception
      update(conn, "some sql stmt") shouldBe Failure(exception)
    }
    "return an Exception if the statement can not be created due to a non-SqlException" in {
      val conn      = mock[java.sql.Connection]
      val exception = new Exception("Something went wrong")
      conn.createStatement throws exception
      update(conn, "some sql stmt") shouldBe Failure(exception)
    }
    "return a SQLException if the statement can not be executed due to a SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.Statement]
      val exception = new SQLException("Something went wrong")
      stmt.executeUpdate(*[String]) throws exception
      conn.createStatement answers stmt
      update(conn, "some sql stmt") shouldBe Failure(exception)
    }
    "return an Exception if the statement can not be executed due to a non-SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.Statement]
      val exception = new Exception("Something went wrong")
      stmt.executeUpdate(*[String]) throws exception
      conn.createStatement answers stmt
      update(conn, "some sql stmt") shouldBe Failure(exception)
    }
  }

  "query" should {
    "return successfully" in {
      val conn = mock[java.sql.Connection]
      val stmt = mock[java.sql.Statement]
      val rs   = mock[java.sql.ResultSet]
      rs.next answers true andThenAnswer false
      rs.getString(*[String]) answers ((_: String) => "some_value")
      stmt.executeQuery(*[String]) answers rs
      conn.createStatement answers stmt
      query(conn, "some sql stmt") shouldBe Success(List("some_value"))
    }

    "return a SQLException if the statement can not be created due to a SqlException" in {
      val conn      = mock[java.sql.Connection]
      val exception = new SQLException("Something went wrong")
      conn.createStatement throws exception
      query(conn, "some sql stmt") shouldBe Failure(exception)
    }
    "return an Exception if the statement can not be created due to a non-SqlException" in {
      val conn      = mock[java.sql.Connection]
      val exception = new Exception("Something went wrong")
      conn.createStatement throws exception
      query(conn, "some sql stmt") shouldBe Failure(exception)
    }
    "return a SQLException if the statement can not be executed due to a SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.Statement]
      val exception = new SQLException("Something went wrong")
      stmt.executeQuery(*[String]) throws exception
      conn.createStatement answers stmt
      query(conn, "some sql stmt") shouldBe Failure(exception)
    }
    "return an Exception if the statement can not be executed due to a non-SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.Statement]
      val exception = new Exception("Something went wrong")
      stmt.executeQuery(*[String]) throws exception
      conn.createStatement answers stmt
      query(conn, "some sql stmt") shouldBe Failure(exception)
    }
  }

  "queryCallable" should {
    "return successfully" in {
      val conn = mock[java.sql.Connection]
      val stmt = mock[java.sql.CallableStatement]
      val rs   = mock[java.sql.ResultSet]
      rs.next answers true andThenAnswer false
      rs.getString(*[String]) answers ((_: String) => "some_value")
      stmt.executeQuery() answers rs
      conn.prepareCall(*) answers stmt
      queryCallable(conn, "some sql stmt", Seq(1, "2").map(SqlParam.fromAny)) shouldBe Success(List("some_value"))
      rs.close wasCalled once
      stmt.close wasCalled once
      conn.close wasNever called
      stmt.setInt(1, 1) wasCalled once
      stmt.setString(2, "2") wasCalled once

    }

    "return a SQLException if the statement can not be created due to a SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.CallableStatement]
      val exception = new SQLException("Something went wrong")
      conn.prepareCall(*) throws exception
      queryCallable(conn, "some sql stmt", Seq()) shouldBe Failure(exception)
      stmt.close wasNever called
      conn.close wasNever called
    }
    "return an Exception if the statement can not be created due to a non-SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.CallableStatement]
      val exception = new Exception("Something went wrong")
      conn.prepareCall(*) throws exception
      queryCallable(conn, "some sql stmt", Seq()) shouldBe Failure(exception)
      stmt.close wasNever called
      conn.close wasNever called
    }
    "return a SQLException if the statement can not be executed due to a SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.CallableStatement]
      val exception = new SQLException("Something went wrong")
      stmt.executeQuery() throws exception
      conn.prepareCall(*) answers stmt
      queryCallable(conn, "some sql stmt", Seq()) shouldBe Failure(exception)
      stmt.close wasCalled once
      conn.close wasNever called
    }
    "return an Exception if the statement can not be executed due to a non-SqlException" in {
      val conn      = mock[java.sql.Connection]
      val stmt      = mock[java.sql.CallableStatement]
      val rs        = mock[java.sql.ResultSet]
      val exception = new Exception("Something went wrong")
      stmt.executeQuery() throws exception
      conn.prepareCall(*) answers stmt
      queryCallable(conn, "some sql stmt", Seq()) shouldBe Failure(exception)
      rs.close wasNever called
      stmt.close wasCalled once
      conn.close wasNever called
    }
  }

  "commitClose" should {
    "succeeds" in {
      val con = mock[Connection]
      commitClose(con) shouldBe Success(())
      con.commit wasCalled once
      con.close wasCalled once
    }
    "fails" in {
      val con = mock[Connection]
      val ex  = new SQLException()
      con.commit() throws ex
      commitClose(con) shouldBe Failure(ex)
      con.close wasCalled once
    }
  }

  "rollbackClose" should {
    "succeeds" in {
      val con = mock[Connection]
      rollbackClose(con) shouldBe Success(())
      con.rollback wasCalled once
      con.close wasCalled once
    }
    "fails" in {
      val con = mock[Connection]
      val ex  = new SQLException()
      con.rollback throws ex
      rollbackClose(con) shouldBe Failure(ex)
      con.close wasCalled once
    }
  }
}
