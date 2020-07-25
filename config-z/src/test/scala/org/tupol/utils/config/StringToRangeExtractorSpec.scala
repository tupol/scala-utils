package org.tupol.utils.configz

import com.typesafe.config.ConfigException.BadValue
import com.typesafe.config.{ Config, ConfigFactory }
import org.tupol.utils.configz.Extractor.rangeExtractor.parseStringToRange
import org.scalatest.{ FunSuite, Matchers }
import scalaz.syntax.applicative._
import scalaz.{ ValidationNel, Failure => ZFailure }

class StringToRangeExtractorSpec extends FunSuite with Matchers {

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
