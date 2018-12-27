package org.tupol.utils.config

import com.typesafe.config.ConfigException.BadValue
import org.scalatest.{ FunSuite, Matchers }
import org.tupol.utils.config.Extractor.rangeExtractor.parseStringToRange

class StringToRangeSpec extends FunSuite with Matchers {

  test("commons#parseStringToRange - creates sequence for a single int value") {
    val i = 1
    val input = s"$i"
    val output = parseStringToRange(input, "somePath")

    output.head shouldBe i
    output.last shouldBe i
  }

  test("commons#parseStringToRange - creates full sequence") {
    val i = 1
    val input = s"${i}, ${i + 10}, 1"
    val output = parseStringToRange(input, "somePath")

    output.head shouldBe i
    output.last shouldBe i + 10
  }

  test("commons#parseStringToRange - throws BadValue for start values greater than stop values") {
    val i = 1
    val input = s"${i + 1}, $i, 1"
    val ex = intercept[BadValue] {
      parseStringToRange(input, "somePath")
    }
    ex.isInstanceOf[BadValue]
  }

  test("commons#parseStringToRange - throws BadValue for negative steps") {
    val i = 1
    val input = s"$i, ${i + 1}, -1"
    val ex = intercept[BadValue] {
      parseStringToRange(input, "somePath")
    }
    ex.isInstanceOf[BadValue]
  }

  test("commons#parseStringToRange - throws BadValue for any malformed string") {
    val input = "sdfsdf"
    val ex = intercept[BadValue] {
      parseStringToRange(input, "somePath")
    }
    ex.isInstanceOf[BadValue]
  }

}
