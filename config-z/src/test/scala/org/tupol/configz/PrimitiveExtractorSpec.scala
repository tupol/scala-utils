package org.tupol.configz

import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scalaz.{ Failure => ZFailure }

import java.sql.{ Date, Timestamp }
import java.time.{ Duration, LocalDate, LocalDateTime }

class PrimitiveExtractorSpec extends AnyFunSuite with Matchers {

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

  test("Extracting a String") {
    val config = ConfigFactory.parseString("""
                                             |property: "10h"
                                           """.stripMargin)
    val actual = config.extract[String]("property").get
    actual shouldEqual "10h"
  }

  test("Extracting a true Boolean") {
    val config = ConfigFactory.parseString("""
                                             |property: "true"
                                           """.stripMargin)
    val actual = config.extract[Boolean]("property").get
    actual shouldEqual true
  }

  test("Extracting a false Boolean") {
    val config = ConfigFactory.parseString("""
                                             |property: false
                                           """.stripMargin)
    val actual = config.extract[Boolean]("property").get
    actual shouldEqual false
  }

  test("Extracting an unknown Boolean fails") {
    val config = ConfigFactory.parseString("""
                                             |property: whaaat
                                           """.stripMargin)
    val actual = config.extract[Boolean]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting an Int") {
    val config = ConfigFactory.parseString("""
                                             |property: 123123123
                                           """.stripMargin)
    val actual = config.extract[Int]("property").get
    actual shouldEqual 123123123
  }

  test("Extracting an Int fails") {
    val config = ConfigFactory.parseString("""
                                             |property: whaaat
                                           """.stripMargin)
    val actual = config.extract[Int]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a Long") {
    val config = ConfigFactory.parseString("""
                                             |property: 123123123
                                           """.stripMargin)
    val actual = config.extract[Long]("property").get
    actual shouldEqual 123123123L
  }

  test("Extracting a Long fails") {
    val config = ConfigFactory.parseString("""
                                             |property: whaaat
                                           """.stripMargin)
    val actual = config.extract[Long]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a Double") {
    val config = ConfigFactory.parseString("""
                                             |property: 123123123
                                           """.stripMargin)
    val actual = config.extract[Double]("property").get
    actual shouldEqual 123123123L
  }

  test("Extracting a Double fails") {
    val config = ConfigFactory.parseString("""
                                             |property: whaaat
                                           """.stripMargin)
    val actual = config.extract[Double]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a Duration") {
    val config = ConfigFactory.parseString("""
                                             |property: "10h"
                                           """.stripMargin)
    val actual = config.extract[Duration]("property").get
    actual shouldEqual Duration.ofHours(10)
  }

  test("Extracting a Duration fails") {
    val config = ConfigFactory.parseString("""
                                             |property: "x"
                                           """.stripMargin)
    val actual = config.extract[Date]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a Date") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-23"
                                           """.stripMargin)
    val actual = config.extract[Date]("property").get
    actual shouldEqual Date.valueOf("2018-12-23")
  }

  test("Extracting a Date fails") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-44"
                                           """.stripMargin)
    val actual = config.extract[Date]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a Timestamp with millis") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-23 10:23:59.001"
                                           """.stripMargin)
    val actual = config.extract[Timestamp]("property").get
    actual shouldEqual Timestamp.valueOf("2018-12-23 10:23:59.001")
  }

  test("Extracting a Timestamp no millis") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-23 10:23:59"
                                           """.stripMargin)
    val actual = config.extract[Timestamp]("property").get
    actual shouldEqual Timestamp.valueOf("2018-12-23 10:23:59")
  }

  test("Extracting a Timestamp fails") {
    val config = ConfigFactory.parseString("""
                                             |property: "abc"
                                           """.stripMargin)
    val actual = config.extract[Timestamp]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a LocalDate") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-23"
                                           """.stripMargin)
    val actual = config.extract[LocalDate]("property").get
    actual shouldEqual LocalDate.of(2018, 12, 23)
  }

  test("Extracting a LocalDate fails") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-44"
                                           """.stripMargin)
    val actual = config.extract[LocalDate]("property")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extracting a LocalDateTime") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-23T10:23:59"
                                           """.stripMargin)
    val actual = config.extract[LocalDateTime]("property").get
    actual shouldEqual LocalDateTime.of(2018, 12, 23, 10, 23, 59)
  }

  test("Extracting a LocalDateTime fails") {
    val config = ConfigFactory.parseString("""
                                             |property: "2018-12-44 10:23:59"
                                           """.stripMargin)
    val actual = config.extract[LocalDateTime]("property")
    actual shouldBe a[ZFailure[_]]
  }

}
