package org.tupol.configz

import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class StringToRangeExtractorSpec extends AnyFunSuite with Matchers {

  test("Extract a Range of a single value should be successful") {
    val config = ConfigFactory.parseString("""
                                             |range: "1"
                                           """.stripMargin)
    val actual = config.extract[Range]("range").get
    actual shouldBe Seq(1)
  }

  test("Extract a Range of multiple values should be successful") {
    val config = ConfigFactory.parseString("""
                                             |range: "1, 3, 1"
                                           """.stripMargin)
    val actual = config.extract[Range]("range").get
    actual shouldBe Seq(1, 2, 3)
  }

}
