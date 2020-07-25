package org.tupol.utils

import org.scalatest.{ Matchers, WordSpec }

class EitherOpsSpec extends WordSpec with Matchers {

  import org.tupol.utils.implicits._

  "mapping left " when {
    "we pass a Left value applies the mapping function" in {
      Left[Int, Int](0).mapLeft(_ + 1) shouldBe Left(1)
    }
    "we pass a Right value returns the Right value" in {
      Right[Int, Int](0).mapLeft(_ + 1) shouldBe Right(0)
    }
  }
  "mapping either " when {
    "we pass a Left value applies the left mapping function to the Left" in {
      Left[Int, Int](0).mapEither(_ + 1, _ - 1) shouldBe Left(1)
    }
    "we pass a Right value applies the right mapping function to the Right" in {
      Right[Int, Int](0).mapEither(_ + 1, _ - 1) shouldBe Right(-1)
    }
  }
}
