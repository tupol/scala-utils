package org.tupol.configz

import com.typesafe.config.{ Config, ConfigFactory }
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scalaz.syntax.applicative._
import scalaz.{ ValidationNel, Failure => ZFailure }

class CompositeExtractorSpec extends AnyFunSuite with Matchers {

  case class CustomConf(prop1: Int, prop2: Seq[Long])
  object CustomConf extends Configurator[CustomConf] {
    override def validationNel(config: Config): ValidationNel[Throwable, CustomConf] =
      config.extract[Int]("prop1") |@| config.extract[Seq[Long]]("prop2") apply CustomConf.apply

  }

  test("Extract CustomConf from path should be successful") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |cc: {prop1 : 0, prop2: [21, 22]}
                                           """.stripMargin)
    val actual                       = config.extract[CustomConf]("cc").get
    val expected                     = CustomConf(0, Seq(21, 22))
    actual shouldBe expected
  }

  test("Extract CustomConf from path should fail for bad values") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |cc: {prop1 : 0, prop2: [21, a]}
                                           """.stripMargin)
    val actual                       = config.extract[CustomConf]("cc")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extract CustomConf with no path should be successful") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |{prop1 : 0, prop2: [1, 2]}
                                           """.stripMargin)
    val actual                       = config.extract[CustomConf].get
    val expected                     = CustomConf(0, Seq(1, 2))
    actual shouldBe expected
  }

  test("Extract CustomConf with no path should fail for bad values") {
    implicit val customConfExtractor = CustomConf
    val config                       = ConfigFactory.parseString("""
                                             |{prop1 : 0, prop2: [21, a]}
                                           """.stripMargin)
    val actual                       = config.extract[CustomConf]
    actual shouldBe a[ZFailure[_]]
  }

}
