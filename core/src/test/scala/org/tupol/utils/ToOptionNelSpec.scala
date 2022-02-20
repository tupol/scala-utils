package org.tupol.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ToOptionNelSpec extends AnyWordSpec with Matchers {

  import org.tupol.utils.CollectionOps._

  "Map.toOptionNel" should {
    "return None for an empty seq" in {
      Map().toOptionNel shouldBe None
    }
    "return Some list for a non-empty seq" in {
      val map    = Map((1, 10), (2, 20), (3, 30))
      val result = map.toOptionNel
      result shouldBe a[Some[_]]
      result.get should contain theSameElementsAs map
    }
  }

  "Seq.toOptionNel" should {
    "return None for an empty seq" in {
      Seq().toOptionNel shouldBe None
    }
    "return Some list for a non-empty seq" in {
      val seq    = Seq(1, 5, 2)
      val result = seq.toOptionNel
      result shouldBe a[Some[_]]
      result.get should contain theSameElementsAs seq
    }
  }

  "Set.toOptionNel" should {
    "return None for an empty set" in {
      Set().toOptionNel shouldBe None
    }
    "return Some list for a non-empty set" in {
      val set    = Set(1, 5, 2)
      val result = set.toOptionNel
      result shouldBe a[Some[_]]
      result.get should contain theSameElementsAs set
    }
  }

  "Traversable.toOptionNel" should {
    "return None for an empty traversable" in {
      Traversable().toOptionNel shouldBe None
    }
    "return Some list for a non-empty traversable" in {
      val col    = Traversable(1, 5, 2)
      val result = col.toOptionNel
      result shouldBe a[Some[_]]
      result.get should contain theSameElementsAs col
    }
  }

  "Vector.toOptionNel" should {
    "return None for an empty traversable" in {
      Vector().toOptionNel shouldBe None
    }
    "return Some list for a non-empty traversable" in {
      val col    = Vector(1, 5, 2)
      val result = col.toOptionNel
      result shouldBe a[Some[_]]
      result.get should contain theSameElementsAs col
    }
  }

  "Iterable.toOptionNel" should {
    "return None for an empty traversable" in {
      Iterable().toOptionNel shouldBe None
    }
    "return Some list for a non-empty traversable" in {
      val col    = Iterable(1, 5, 2)
      val result = col.toOptionNel
      result shouldBe a[Some[_]]
      result.get should contain theSameElementsAs col
    }
  }

  "Iterator.toOptionNel" should {
    "return None for an empty traversable" in {
      Iterator().toOptionNel shouldBe None
    }
    "return Some list for a non-empty traversable" in {
      val col    = Seq(1, 5, 2)
      val result = col.toIterator.toOptionNel
      result shouldBe a[Some[_]]
      result.get.toSeq should contain theSameElementsAs col
    }
  }

}
