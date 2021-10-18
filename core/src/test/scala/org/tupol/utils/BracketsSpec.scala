package org.tupol.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

import scala.util.{Failure, Success}

class BracketsSpec extends AnyFunSuite with Matchers {

  test("Bracket.auto should be successful if everything goes well and the resource should be closed") {
    val expectedResult      = 111
    var closedFlag: Boolean = false
    trait Resource extends AutoCloseable {
      override def close(): Unit = closedFlag = true
    }
    def code(r: Resource) = expectedResult
    val result            = Bracket.auto(new Resource {})(code)
    result shouldBe a[Success[_]]
    result.get shouldBe expectedResult
    closedFlag shouldBe true
  }

  test("Bracket.auto should be successful if everything goes well, even if the resource fails closing") {
    val expectedResult      = 111
    var closedFlag: Boolean = false
    class ResourceException extends Exception("")
    trait Resource extends AutoCloseable {
      override def close(): Unit = throw new ResourceException
    }
    def code(r: Resource) = expectedResult
    val result            = Bracket.auto(new Resource {})(code)
    result shouldBe a[Success[_]]
    result.get shouldBe expectedResult
    closedFlag shouldBe false
  }

  test("Bracket.auto should fail if the code fails and the resource should be closed") {
    var closedFlag: Boolean = false
    trait Resource extends AutoCloseable {
      override def close(): Unit = closedFlag = true
    }
    class CodeException extends Exception("")
    def code(r: Resource) = throw new CodeException
    val result            = Bracket.auto(new Resource {})(code)
    result shouldBe a[Failure[_]]
    a[CodeException] should be thrownBy result.get
    closedFlag shouldBe true
  }

}
