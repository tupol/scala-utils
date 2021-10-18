package org.tupol.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EitherTraversableSpec extends AnyWordSpec with Matchers {

  import org.tupol.utils.EitherUtils._
  import org.tupol.utils.implicits._

  "flattening" when {
    "we pass empty sequence of Either should return a Right of an empty sequence" in {
      flatten(Seq()) shouldBe Right(Nil)
      Seq().flattenEithers shouldBe Right(Nil)
    }
    "we pass a list of Left should return the last Left " in {
      flatten(Seq(Left(0), Left(1))) shouldBe Left(1)
      Seq(Left(0), Left(1)).flattenEithers shouldBe Left(1)
    }
    "we pass a list of Left and Right should return the last Left" in {
      flatten(Seq(Right(10), Left(0), Right(20), Left(1))) shouldBe Left(1)
      Seq(Right(10), Left(0), Right(20), Left(1)).flattenEithers shouldBe Left(1)
    }
    "we pass a list of a singe Right should return the Right of Seq of a single value" in {
      flatten(Seq(Right(10))) shouldBe Right(Seq(10))
      Seq(Right(10)).flattenEithers shouldBe Right(Seq(10))
    }
    "we pass a list of Right should return the Right of Seq of values" in {
      flatten(Seq(Right(10), Right(20))) shouldBe Right(Seq(10, 20))
      Seq(Right(10), Right(20)).flattenEithers shouldBe Right(Seq(10, 20))
    }
  }
  "separating" when {
    "we pass empty sequence of Either should return a Right of an empty sequence" in {
      separate(Seq()) shouldBe (Nil, Nil)
      Seq().separate shouldBe (Nil, Nil)
    }
    "we pass a list of a single Left should return a left sequence of a single value" in {
      separate(Seq(Left(0))) shouldBe (Seq(0), Nil)
      Seq(Left(0)).separate shouldBe (Seq(0), Nil)
    }
    "we pass a list of Left should return a left sequence of left values" in {
      separate(Seq(Left(0), Left(1))) shouldBe (Seq(0, 1), Nil)
      Seq(Left(0), Left(1)).separate shouldBe (Seq(0, 1), Nil)
    }
    "we pass a list of a single Right should return a right sequence of a single value" in {
      separate(Seq(Right(0))) shouldBe (Nil, Seq(0))
      Seq(Right(0)).separate shouldBe (Nil, Seq(0))
    }
    "we pass a list of Right should return a right sequence of right values" in {
      separate(Seq(Right(0), Right(1))) shouldBe (Nil, Seq(0, 1))
      Seq(Right(0), Right(1)).separate shouldBe (Nil, Seq(0, 1))
    }
    "we pass a list of Eithers should return a left sequence of left values and a right sequence of right values" in {
      separate(Seq(Right(0), Left(0), Right(1), Left(1), Right(2))) shouldBe (Seq(0, 1), Seq(0, 1, 2))
      Seq(Right(0), Left(0), Right(1), Left(1), Right(2)).separate shouldBe (Seq(0, 1), Seq(0, 1, 2))
    }
  }
  "extracting the left values" when {
    "we pass empty sequence of Either should return an empty sequence" in {
      lefts(Seq()) shouldBe Nil
      Seq().lefts shouldBe Nil
    }
    "we pass a list of a single Left should return a sequence of a single value" in {
      lefts(Seq(Left(0))) shouldBe Seq(0)
      Seq(Left(0)).lefts shouldBe Seq(0)
    }
    "we pass a list of Left should return a sequence of left values" in {
      lefts(Seq(Left(0), Left(1))) shouldBe Seq(0, 1)
      Seq(Left(0), Left(1)).lefts shouldBe Seq(0, 1)
    }
    "we pass a list of a single Right should return an empty sequence" in {
      lefts(Seq(Right(0))) shouldBe Nil
      Seq(Right(0)).lefts shouldBe Nil
    }
    "we pass a list of Right should return an empty sequence" in {
      lefts(Seq(Right(0), Right(1))) shouldBe Nil
      Seq(Right(0), Right(1)).lefts shouldBe Nil
    }
    "we pass a list of Eithers should return a sequence of left values" in {
      lefts(Seq(Right(0), Left(0), Right(1), Left(1), Right(2))) shouldBe Seq(0, 1)
      Seq(Right(0), Left(0), Right(1), Left(1), Right(2)).lefts shouldBe Seq(0, 1)
    }
  }
  "extracting the right values" when {
    "we pass empty sequence of Either should return an empty sequence" in {
      rights(Seq()) shouldBe Nil
      Seq().rights shouldBe Nil
    }
    "we pass a list of a single Right should return a sequence of a single value" in {
      rights(Seq(Right(0))) shouldBe Seq(0)
      Seq(Right(0)).rights shouldBe Seq(0)
    }
    "we pass a list of Right should return a sequence of right values" in {
      rights(Seq(Right(0), Right(1))) shouldBe Seq(0, 1)
      Seq(Right(0), Right(1)).rights shouldBe Seq(0, 1)
    }
    "we pass a list of a single Left should return an empty sequence" in {
      rights(Seq(Left(0))) shouldBe Nil
      Seq(Left(0)).rights shouldBe Nil
    }
    "we pass a list of Left should return an empty sequence" in {
      rights(Seq(Left(0), Left(1))) shouldBe Nil
      Seq(Left(0), Left(1)).rights shouldBe Nil
    }
    "we pass a list of Eithers should return a sequence of right values" in {
      rights(Seq(Right(0), Left(0), Right(1), Left(1), Right(2))) shouldBe Seq(0, 1, 2)
      Seq(Right(0), Left(0), Right(1), Left(1), Right(2)).rights shouldBe Seq(0, 1, 2)
    }
  }
}
