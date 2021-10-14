package org.tupol.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EitherArraySpec extends AnyWordSpec with Matchers {

  import org.tupol.utils.EitherOps._
  import org.tupol.utils.EitherUtils._

  "flattening" when {
    "we pass empty sequence of Either should return a Right of an empty sequence" in {
      flatten(Array.empty[Either[Int, Int]]) shouldBe Right(Nil)
      Array.empty[Either[Int, Int]].flattenEithers shouldBe Right(Nil)
    }
    "we pass a list of Left should return the last Left " in {
      flatten(Array(Left(0), Left(1))) shouldBe Left(1)
      Array[Either[Int, Int]](Left(0), Left(1)).flattenEithers shouldBe Left(1)
    }
    "we pass a list of Left and Right should return the last Left" in {
      flatten(Array(Right(10), Left(0), Right(20), Left(1))) shouldBe Left(1)
      Array(Right(10), Left(0), Right(20), Left(1)).flattenEithers shouldBe Left(1)
    }
    "we pass a list of a singe Right should return the Right of Array of a single value" in {
      flatten(Array(Right(10))) === Right(Array(10))
      Array[Either[Int, Int]](Right(10)).flattenEithers === Right(Array(10))
    }
    "we pass a list of Right should return the Right of Array of values" in {
      flatten(Array(Right(10), Right(20))) === Right(Array(10, 20))
      Array[Either[Int, Int]](Right(10), Right(20)).flattenEithers === Right(Array(10, 20))
    }
  }
  "separating" when {
    "we pass empty array of Either should return a Right of an empty array" in {
      separate(Array.empty[Either[Int, Int]]) === (Nil, Nil)
      Array.empty[Either[Int, Int]].separate === (Nil, Nil)
    }
    "we pass a array of a single Left should return a left array of a single value" in {
      separate(Array(Left(0))) === (Array(0), Nil)
      Array[Either[Int, Int]](Left(0)).separate === (Array(0), Nil)
    }
    "we pass a array of Left should return a left array of left values" in {
      separate(Array(Left(0), Left(1))) === (Array(0, 1), Nil)
      Array[Either[Int, Int]](Left(0), Left(1)).separate === (Array(0, 1), Nil)
    }
    "we pass a array of a single Right should return a right array of a single value" in {
      separate(Array(Right(0))) === (Nil, Array(0))
      Array[Either[Int, Int]](Right(0)).separate === (Nil, Array(0))
    }
    "we pass a array of Right should return a right array of right values" in {
      separate(Array(Right(0), Right(1))) === (Nil, Array(0, 1))
      Array[Either[Int, Int]](Right(0), Right(1)).separate === (Nil, Array(0, 1))
    }
    "we pass a array of Eithers should return a left array of left values and a right array of right values" in {
      separate(Array(Right(0), Left(0), Right(1), Left(1), Right(2))) === (Array(0, 1), Array(0, 1, 2))
      Array[Either[Int, Int]](Right(0), Left(0), Right(1), Left(1), Right(2)).separate === (Array(0, 1), Array(0, 1, 2))
    }
  }
  "extracting the left values" when {
    "we pass empty sequence of Either should return an empty sequence" in {
      lefts(Array.empty[Either[Int, Int]]) shouldBe Nil
      Array[Either[Int, Int]]().lefts shouldBe Nil
    }
    "we pass a list of a single Left should return a sequence of a single value" in {
      lefts(Array(Left(0))) shouldBe Array(0)
      Array[Either[Int, Int]](Left(0)).lefts shouldBe Array(0)
    }
    "we pass a list of Left should return a sequence of left values" in {
      lefts(Array(Left(0), Left(1))) shouldBe Array(0, 1)
      Array[Either[Int, Int]](Left(0), Left(1)).lefts shouldBe Array(0, 1)
    }
    "we pass a list of a single Right should return an empty sequence" in {
      lefts(Array(Right(0))) shouldBe Nil
      Array[Either[Int, Int]](Right(0)).lefts shouldBe Nil
    }
    "we pass a list of Right should return an empty sequence" in {
      lefts(Array(Right(0), Right(1))) shouldBe Nil
      Array[Either[Int, Int]](Right(0), Right(1)).lefts shouldBe Nil
    }
    "we pass a list of Eithers should return a sequence of left values" in {
      lefts(Array(Right(0), Left(0), Right(1), Left(1), Right(2))) shouldBe Array(0, 1)
      Array(Right(0), Left(0), Right(1), Left(1), Right(2)).lefts shouldBe Array(0, 1)
    }
  }
  "extracting the right values" when {
    "we pass empty sequence of Either should return an empty sequence" in {
      rights(Array.empty[Either[Int, Int]]) shouldBe Nil
      Array[Either[Int, Int]]().rights shouldBe Nil
    }
    "we pass a list of a single Right should return a sequence of a single value" in {
      rights(Array(Right(0))) shouldBe Array(0)
      Array[Either[Int, Int]](Right(0)).rights shouldBe Array(0)
    }
    "we pass a list of Right should return a sequence of right values" in {
      rights(Array(Right(0), Right(1))) shouldBe Array(0, 1)
      Array[Either[Int, Int]](Right(0), Right(1)).rights shouldBe Array(0, 1)
    }
    "we pass a list of a single Left should return an empty sequence" in {
      rights(Array(Left(0))) shouldBe Nil
      Array[Either[Int, Int]](Left(0)).rights shouldBe Nil
    }
    "we pass a list of Left should return an empty sequence" in {
      rights(Array(Left(0), Left(1))) shouldBe Nil
      Array[Either[Int, Int]](Left(0), Left(1)).rights shouldBe Nil
    }
    "we pass a list of Eithers should return a sequence of right values" in {
      rights(Array(Right(0), Left(0), Right(1), Left(1), Right(2))) shouldBe Array(0, 1, 2)
      Array(Right(0), Left(0), Right(1), Left(1), Right(2)).rights shouldBe Array(0, 1, 2)
    }
  }
}
