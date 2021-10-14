package org.tupol.configz

import com.typesafe.config.ConfigException.BadValue
import org.scalacheck.Prop.forAll
import org.scalacheck.{ Gen, Properties }
import org.scalatest.matchers.should.Matchers
import org.tupol.configz.Extractor.rangeExtractor.parseStringToRange

class StringToRangeProp extends Properties("Range") with Matchers {

  val positiveInts = for (n <- Gen.choose(0, 100)) yield n
  val negativeInts = for (n <- Gen.choose(-100, -1)) yield n

  property("commons#parseStringToRange - creates sequence for a single int value") = forAll(positiveInts) { (i: Int) =>
    val input  = s"$i"
    val output = parseStringToRange(input, "somePath").get

    output.head == i
    output.last == i
  }

  property("commons#parseStringToRange - creates sequence for a fully defined range") = forAll(positiveInts) {
    (i: Int) =>
      val input  = s"${i}, ${i + 10}, 1"
      val output = parseStringToRange(input, "somePath").get
      output.head == i
      output.last == i + 10
  }

  property("commons#parseStringToRange - throws BadValue for start values greater than stop values") =
    forAll(positiveInts) { (i: Int) =>
      val input = s"${i + 1}, $i, 1"
      val ex = intercept[BadValue] {
        parseStringToRange(input, "somePath").get
      }
      ex.isInstanceOf[BadValue]
    }

  property("commons#parseStringToRange - throws BadValue for negative steps") = forAll(positiveInts, negativeInts) {
    (i: Int, s: Int) =>
      val input = s"$i, ${i + 1}, $s"
      val ex = intercept[BadValue] {
        parseStringToRange(input, "somePath").get
      }
      ex.isInstanceOf[BadValue]
  }

  property("commons#parseStringToRange - throws BadValue for any string") = forAll { (input: String) =>
    val badInput = s"_;@#* $input;@#*&"
    val ex = intercept[BadValue] {
      parseStringToRange(input, "somePath").get
    }
    ex.isInstanceOf[BadValue]
  }

}
