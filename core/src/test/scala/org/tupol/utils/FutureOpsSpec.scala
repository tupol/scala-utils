package org.tupol.utils

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ FunSuite, Matchers }

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

class FutureOpsSpec extends FunSuite with Matchers with ScalaFutures {

  import org.tupol.utils.implicits._

  implicit val ec = scala.concurrent.ExecutionContext.global

  test("Success future.logSuccess") {
    val logger = ArrayBuffer[Int]()
    whenReady(Future.successful(1).logSuccess(message => logger.append(message))) { result =>
      logger should contain theSameElementsAs Seq(1)
    }
  }

  test("Failure future.logSuccess") {
    val logger = ArrayBuffer[Int]()
    val ex     = new Exception("")
    Future.failed[Int](ex).logSuccess(message => logger.append(message)).failed.futureValue shouldBe ex
    logger.isEmpty shouldBe true
  }

  test("Success future.logFailure") {
    val logger = ArrayBuffer[String]()
    whenReady(Future.successful(1).logFailure(throwable => logger.append(throwable.getMessage))) { result =>
      logger.isEmpty shouldBe true
    }
  }

  test("Failure future.logFailure") {
    val logger = ArrayBuffer[String]()
    val ex     = new Exception("1")
    Future.failed[Int](ex).logFailure(throwable => logger.append(throwable.getMessage)).failed.futureValue shouldBe ex
    logger should contain theSameElementsAs ArrayBuffer("1")

  }

  test("Success future.log") {
    val loggerSuccess = ArrayBuffer[Int]()
    val loggerFailure = ArrayBuffer[String]()
    whenReady(
      Future
        .successful(1)
        .log(message => loggerSuccess.append(message), throwable => loggerFailure.append(throwable.getMessage))
    ) { result =>
      loggerSuccess should contain theSameElementsAs Seq(1)
      loggerFailure.isEmpty shouldBe true
    }
  }

  test("Failure future.log") {
    val loggerSuccess = ArrayBuffer[Int]()
    val loggerFailure = ArrayBuffer[String]()
    val ex            = new Exception("1")
    Future
      .failed[Int](ex)
      .log(message => loggerSuccess.append(message), throwable => loggerFailure.append(throwable.getMessage))
      .failed
      .futureValue shouldBe ex
    loggerSuccess.isEmpty shouldBe true
    loggerFailure should contain theSameElementsAs ArrayBuffer("1")
  }

  test("Success future.mapFailure") {
    def wrap(t: Throwable) = new IllegalArgumentException("message", t)
    whenReady(Future.successful(1).mapFailure(wrap)) { result =>
      result shouldBe 1
    }
  }

  test("Failure future.mapFailure") {
    def wrap(t: Throwable) = new IllegalArgumentException("message", t)
    val ex                 = new Exception("1")
    Future.failed[Int](ex).mapFailure(wrap).failed.futureValue shouldBe an[IllegalArgumentException]
    Future.failed[Int](ex).mapFailure(wrap).failed.futureValue.getCause shouldBe ex
  }

}
