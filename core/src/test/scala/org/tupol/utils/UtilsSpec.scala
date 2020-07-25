package org.tupol.utils

import org.scalatest.{ FunSuite, Matchers }

class UtilsSpec extends FunSuite with Matchers {

  test("timeCode should return the approximate length of the computation in millis") {

    val expectedResult   = 10
    val expectedDuration = 200
    val (result, duration) = timeCode {
      Thread.sleep(expectedDuration);
      10
    }
    result shouldBe expectedResult
    Math.abs(duration - expectedDuration) / expectedDuration < 0.1 shouldBe true
  }

}
