
package org.tupol.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Success => TrySuccess, Failure => TryFailure}

class ResultSpec extends AnyFlatSpec with Matchers {

  "Success" should "map correctly" in {
    Result.success(1).map(_ + 1) shouldBe Result.success(2)
  }

  it should "flatMap correctly" in {
    Result.success(1).flatMap(x => Result.success(x + 1)) shouldBe Result.success(2)
    Result.success(1).flatMap(_ => Result.error("error")) shouldBe Result.error("error")
    (for {
      s <- Result.success(1)
      if s > 1
      s <- Result.success(2)

    } yield s) shouldBe Result.success(1)
    (for {
      s <- Result.success(1)
      if s > 0
      r <- Result.error("error")

    } yield s) shouldBe Result.error("error")
  }

  it should "fold correctly" in {
    Result.success(1).fold(
      error => s"error: $error",
      success => s"success: $success"
    ) shouldBe "success: 1"
  }

  it should "report correct status" in {
    Result.success(1).isSuccess shouldBe true
    Result.success(1).isError shouldBe false
  }

  it should "convert to Option correctly" in {
    Result.success(1).toOption shouldBe Some(1)
  }

  it should "convert to Either correctly" in {
    Result.success(1).toEither shouldBe Right(1)
  }

  it should "convert to Try correctly" in {
    Result.success(1).toTry shouldBe TrySuccess(1)
  }

  "Error" should "map correctly" in {
    Result.error("error").map(_ => 2) shouldBe Result.error("error")
  }

  it should "flatMap correctly" in {
    Result.error("error").flatMap(_ => Result.success(2)) shouldBe Result.error("error")
  }

  it should "fold correctly" in {
    Result.error("error").fold(
      error => s"error: $error",
      success => s"success: $success"
    ) shouldBe "error: error"
  }

  it should "report correct status" in {
    Result.error("error").isSuccess shouldBe false
    Result.error("error").isError shouldBe true
  }

  it should "convert to Option correctly" in {
    Result.error("error").toOption shouldBe None
  }

  it should "convert to Either correctly" in {
    Result.error("error").toEither shouldBe Left("error")
  }

  it should "convert to Try correctly" in {
    class TestException extends Exception("test")
    val error = Result.error(new TestException)
    error.toTry.isFailure shouldBe true
  }

  "Result companion object" should "create from Option correctly" in {
    Result.fromOption(Some(1), "error") shouldBe Result.success(1)
    Result.fromOption(None, "error") shouldBe Result.error("error")
  }

  it should "create from Either correctly" in {
    Result.fromEither(Right(1)) shouldBe Result.success(1)
    Result.fromEither(Left("error")) shouldBe Result.error("error")
  }

  it should "create from Try correctly" in {
    Result.fromTry(TrySuccess(1)) shouldBe Result.success(1)
    val exception = new RuntimeException("error")
    Result.fromTry(TryFailure(exception)) shouldBe Result.error(exception)
  }
}