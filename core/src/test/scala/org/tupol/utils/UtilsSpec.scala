package org.tupol.utils

import org.scalatest.{ FunSuite, Matchers }

class UtilsSpec extends FunSuite with Matchers {

  test("timeCode should return the approximate length of the computation in millis") {

    val expectedResultMillis   = 10
    val expectedDurationMillis = 200
    val (result, duration) = timeCode {
      Thread.sleep(expectedDurationMillis);
      10
    }
    result shouldBe expectedResultMillis
    Math.abs(duration.toMillis - expectedDurationMillis) / expectedDurationMillis < 0.1 shouldBe true
  }

}
