package org.tupol.configz

import com.typesafe.config.{ Config, ConfigFactory }
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import scalaz.syntax.applicative._
import scalaz.{ ValidationNel, Failure => ZFailure }

class EitherExtractorSpec extends AnyFunSuite with Matchers {

  case class CustomConf(prop1: Int, prop2: Seq[Long])
  object CustomConf extends Configurator[CustomConf] {
    override def validationNel(config: Config): ValidationNel[Throwable, CustomConf] =
      config.extract[Int]("prop1") |@| config.extract[Seq[Long]]("prop2") apply CustomConf.apply
  }

  test("Extract Either an Int or a CustomConf returns an Int") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |either: 111
                                           """.stripMargin)
    val actual                       = config.extract[Either[Int, CustomConf]]("either").get
    val expected                     = 111
    actual shouldBe Left(expected)
  }

  test("Extract Either an Int or a CustomConf returns a CustomConf") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |either: {prop1 : 0, prop2: [1, 2]}
                                           """.stripMargin)
    val actual                       = config.extract[Either[Int, CustomConf]]("either").get
    val expected                     = CustomConf(0, Seq(1, 2))
    actual shouldBe Right(expected)
  }

  test("Extract Either an Int or a CustomConf should fail if no match") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |either: "-incorrect_value-"
                                           """.stripMargin)
    val actual                       = config.extract[Either[Int, CustomConf]]
    actual shouldBe a[ZFailure[_]]
  }

}
