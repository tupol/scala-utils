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

  test("Extract a Seq[Any] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, 2.3, true, false, text ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Any]]("props").get
    val expected = Seq(1, 2.3, true, false, "text")
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[String] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ abc, cde ]
                                           """.stripMargin)
    val actual = config.extract[Seq[String]]("props").get
    val expected = Seq("abc", "cde")
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[Boolean] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ true, false, true ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Boolean]]("props").get
    val expected = Seq(true, false, true)
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[Boolean] should fail if any element is not a Boolean") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ true, false, true, yummy ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Boolean]]("props")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extract a Seq[Int] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, 2 ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Int]]("props").get
    val expected = Seq(1, 2)
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[Int] should fail if any element is not an Int") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, a ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Int]]("props")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extract a Seq[Long] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, 2 ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Long]]("props").get
    val expected = Seq(1L, 2L)
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[Long] should fail if any element is not a Long") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, a ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Long]]("props")
    actual shouldBe a[ZFailure[_]]
  }

  test("Extract a Seq[Double] should be successful") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, 2.3 ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Double]]("props").get
    val expected = Seq(1.0, 2.3)
    actual should contain theSameElementsAs (expected)
  }

  test("Extract a Seq[Double] should fail if any element is not a Double") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |props :[ 1, 2.3, a ]
                                           """.stripMargin)
    val actual = config.extract[Seq[Double]]("props")
    actual shouldBe a[ZFailure[_]]
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
