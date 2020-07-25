package org.tupol.utils.tmp

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ FunSuite, Matchers }

import scala.collection.mutable.ArrayBuffer
import scala.util.{ Failure, Success, Try }

class TryOpsSpec extends FunSuite with Matchers with ScalaFutures {

  import org.tupol.utils.implicits._

  test("Success.logSuccess") {
    val logger = ArrayBuffer[Int]()
    Success(1).logSuccess(message => logger.append(message))
    logger should contain theSameElementsAs Seq(1)
  }

  test("Failure.logSuccess") {
    val logger = ArrayBuffer[Int]()
    Failure[Int](new Exception("")).logSuccess(message => logger.append(message))
    logger.isEmpty shouldBe true
  }

  test("Success.logFailure") {
    val logger = ArrayBuffer[String]()
    Success(1).logFailure(throwable => logger.append(throwable.getMessage))
    logger.isEmpty shouldBe true
  }

  test("Failure.logFailure") {
    val logger = ArrayBuffer[String]()
    Failure[Int](new Exception("1")).logFailure(throwable => logger.append(throwable.getMessage))
    logger should contain theSameElementsAs Seq("1")
  }

  test("Success.log") {
    val loggerSuccess = ArrayBuffer[Int]()
    val loggerFailure = ArrayBuffer[String]()
    Success(1).log(message => loggerSuccess.append(message), throwable => loggerFailure.append(throwable.getMessage))
    loggerSuccess should contain theSameElementsAs Seq(1)
    loggerFailure.isEmpty shouldBe true
  }

  test("Failure.log") {
    val loggerSuccess = ArrayBuffer[Int]()
    val loggerFailure = ArrayBuffer[String]()
    Failure[Int](new Exception("1"))
      .log(message => loggerSuccess.append(message), throwable => loggerFailure.append(throwable.getMessage))
    loggerSuccess.isEmpty shouldBe true
    loggerFailure should contain theSameElementsAs Seq("1")
  }

  test("Success.mapFailure") {
    def wrap(t: Throwable) = new IllegalArgumentException("message", t)
    Success(1).mapFailure(wrap) shouldBe Success(1)
  }

  test("Failure.mapFailure") {
    def wrap(t: Throwable) = new IllegalArgumentException("message", t)
    an[IllegalArgumentException] shouldBe thrownBy(Failure[Int](new Exception("1")).mapFailure(wrap).get)
  }

  test("Success.asFuture") {
    Success(123).asFuture.futureValue shouldBe 123
  }

  test("Failure.asFuture") {
    val t = new IllegalArgumentException("message")
    Failure(t).asFuture.failed.futureValue shouldBe t
  }

  test("Seq.allOkOrFail yields a Success for a list of a single element") {
    val result = Seq(Success(1)).allOkOrFail
    result shouldBe a[Success[_]]
    result.get should contain theSameElementsAs (Seq(1))
  }

  test("Seq.allOkOrFail yields a Success") {
    val result = Seq(Success(1), Success(2)).allOkOrFail
    result shouldBe a[Success[_]]
    result.get shouldBe (Seq(1, 2))
  }

  test("Seq.allOkOrFail for an empty list yields a Success") {
    val result = Seq[Try[Int]]().allOkOrFail
    result shouldBe a[Success[_]]
    result.get.isEmpty shouldBe true
  }

  test("Seq.allOkOrFail yields the last Failure") {
    val ex1    = new Exception("ex1")
    val ex2    = new Exception("ex2")
    val result = Seq(Success(1), Failure[Int](ex1), Success(2), Failure[Int](ex2)).allOkOrFail
    result shouldBe Failure(ex2)
  }

  test("Array.allOkOrFail yields a Success") {
    val result = Array(Try(1), Try(2)).allOkOrFail
    result shouldBe a[Success[_]]
    result.get shouldBe (Seq(1, 2))
  }

  test("Seq.collectOkOrFail yields a Success for a list of a single element") {
    val result = Seq(Success(1)).collectOkOrFail
    result shouldBe a[Success[_]]
    result.get should contain theSameElementsAs (Seq(1))
  }

  test("Seq.collectOkOrFail yields a Success") {
    val result = Seq(Success(1), Success(2)).collectOkOrFail
    result shouldBe a[Success[_]]
    result.get shouldBe (Seq(1, 2))
  }

  test("Seq.collectOkOrFail for an empty list yields a Success") {
    val result = Seq[Try[Int]]().collectOkOrFail
    result shouldBe a[Success[_]]
    result.get.isEmpty shouldBe true
  }

  test("Seq.collectOkOrFail for a mix of Success and Failure yields a Success") {
    val ex1    = new Exception("ex1")
    val ex2    = new Exception("ex2")
    val result = Seq(Success(1), Failure[Int](ex1), Success(2), Failure[Int](ex2)).collectOkOrFail
    result shouldBe a[Success[_]]
    result.get shouldBe (Seq(1, 2))
  }

  test("Seq.collectOkOrFail yields a Failure") {
    val ex1    = new Exception("ex1")
    val ex2    = new Exception("ex2")
    val result = Seq(Failure[Int](ex1), Failure[Int](ex2)).collectOkOrFail
    result shouldBe a[Failure[_]]
  }

  test("Array.collectOkOrFail yields a Success") {
    val result = Array(Try(1), Try(2)).collectOkOrFail
    result shouldBe a[Success[_]]
    result.get shouldBe Seq(1, 2)
  }

  test("Array.collectOkOrFail for a mix of Success and Failure yields a Success") {
    val ex1    = new Exception("ex1")
    val ex2    = new Exception("ex2")
    val result = Array(Success(1), Failure[Int](ex1), Success(2), Failure[Int](ex2)).collectOkOrFail
    result shouldBe a[Success[_]]
    result.get shouldBe Seq(1, 2)
  }

  test("Array.collectOkOrFail yields a Failure") {
    val ex1 = new Exception("ex1")
    val ex2 = new Exception("ex2")
    // Interesting behavior for Array[Failure[_]]; for later study
    val result = Array(Try[Any](throw ex1), Try[Any](throw ex2)).collectOkOrFail
    result shouldBe a[Failure[_]]
  }

}
