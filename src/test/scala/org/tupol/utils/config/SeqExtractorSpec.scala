package org.tupol.utils.config

import com.typesafe.config.{ Config, ConfigFactory }
import org.scalatest.{ FunSuite, Matchers }
import scalaz.syntax.applicative._
import scalaz.{ ValidationNel, Failure => ZFailure }

class SeqExtractorSpec extends FunSuite with Matchers {

  case class CustomConf(prop1: Int, prop2: Seq[Long])
  object CustomConf extends Configurator[CustomConf] {
    override def validationNel(config: Config): ValidationNel[Throwable, CustomConf] = {
      config.extract[Int]("prop1") |@| config.extract[Seq[Long]]("prop2") apply CustomConf.apply
    }
  }

  test("Extract a Seq[CustomConf] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |customs :[
                                             |   {prop1 : 1, prop2: [11, 12]},
                                             |   {prop1 : 2, prop2: [21, 22]}
                                             |]
                                           """.stripMargin)
    val actual = config.extract[Seq[CustomConf]]("customs").get
    val expected = Seq(CustomConf(1, Seq(11, 12)), CustomConf(2, Seq(21, 22)))
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[CustomConf] should fail for bad values") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |customs :[
                                             |   {prop1 : 1, prop2: [11, 12]},
                                             |   {prop1 : c, prop2: [21, 22]}
                                             |]
                                           """.stripMargin)
    val actual = config.extract[Seq[CustomConf]]("customs")
    actual shouldBe a[ZFailure[_]]
  }

}
