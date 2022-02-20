package org.tupol.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ProductOpsSpec extends AnyWordSpec with Matchers {

  import org.tupol.utils.ProductOps._

  "Product of Option[_] isEmpty" should {
    "return true" when {
      "all properties are None" in {
        TestProductIsEmpty().isEmpty shouldBe true
      }
    }
    "return false" when {
      "at least one property is not None" in {
        TestProductIsEmpty(val1 = Some(1)).isEmpty shouldBe false
      }
      "at least one property is not an Option" in {
        TestProductIsNotEmpty(val1 = 1).isEmpty shouldBe false
      }
    }
  }
}

case class TestProductIsEmpty(val1: Option[Int] = None, val2: Option[String] = None)
case class TestProductIsNotEmpty(val1: Int, val2: Option[String] = None)
