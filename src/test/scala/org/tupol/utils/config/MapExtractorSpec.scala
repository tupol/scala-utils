package org.tupol.utils.config

import com.typesafe.config.{ Config, ConfigFactory }
import org.scalatest.{ FunSuite, Matchers }
import scalaz.syntax.applicative._
import scalaz.{ ValidationNel, Failure => ZFailure }

class MapExtractorSpec extends FunSuite with Matchers {

  case class ComplexExample(char: Character, str: String, bool: Boolean, dbl: Double, in: Int, lng: Long,
    optChar: Option[Character], optStr: Option[String], optBool: Option[Boolean],
    optDouble: Option[Double], optInt: Option[Int], optLong: Option[Long])

  case class CustomConf(prop1: Int, prop2: Seq[Long])
  object CustomConf extends Configurator[CustomConf] {
    override def validationNel(config: Config): ValidationNel[Throwable, CustomConf] = {
      config.extract[Int]("prop1") |@| config.extract[Seq[Long]]("prop2") apply CustomConf.apply
    }
  }

  test("Map[String, String] should be parsed from an object") {

    val config = ConfigFactory.parseString("""
      |property {
      |   key1 : 600
      |   key2 : "100"
      |}
    """.stripMargin)
    val actual: Map[String, String] = config.extract[Map[String, String]]("property").get
    actual shouldEqual Map("key1" -> "600", "key2" -> "100")
  }

  test("Map[String, String] should be parsed from an empty object") {
    val config = ConfigFactory.parseString("""
      |property {}
    """.stripMargin)
    val actual: Map[String, String] = config.extract[Map[String, String]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, String] should be parsed from a flat object") {
    val config = ConfigFactory.parseString("""
      |property.key1 = value1
      |property.key2 = value2
    """.stripMargin)
    val actual: Map[String, String] = config.extract[Map[String, String]]("property").get
    actual shouldEqual Map("key1" -> "value1", "key2" -> "value2")
  }

  test("Map[String, String] should be parsed from a list of entries") {
    val config = ConfigFactory.parseString("""
      |property :[
      |   {key1 : 600},
      |   {key2 : "100"}
      |]
    """.stripMargin)
    val actual: Map[String, String] = config.extract[Map[String, String]]("property").get

    actual shouldEqual Map("key1" -> "600", "key2" -> "100")
  }

  test("Map[String, String] should be parsed from an empty list of entries") {
    val config = ConfigFactory.parseString("""
      |property :[]
    """.stripMargin)
    val actual: Map[String, String] = config.extract[Map[String, String]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Any] should be parsed from an object") {
    val config = ConfigFactory.parseString("""
      |property {
      |   key1 : 600
      |   key2 : "100"
      |}
    """.stripMargin)
    val actual: Map[String, Any] = config.extract[Map[String, Any]]("property").get
    actual shouldEqual Map("key1" -> 600, "key2" -> "100")
  }

  test("Map[String, Any] should be parsed from an empty object") {
    val config = ConfigFactory.parseString("""
      |property {}
    """.stripMargin)
    val actual: Map[String, Any] = config.extract[Map[String, Any]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Any] should be parsed from a flat object") {
    val config = ConfigFactory.parseString("""
      |property.key1 = value1
      |property.key2 = value2
    """.stripMargin)
    val actual: Map[String, Any] = config.extract[Map[String, Any]]("property").get
    actual shouldEqual Map("key1" -> "value1", "key2" -> "value2")
  }

  test("Map[String, Any] should be parsed from a list of entries") {
    val config = ConfigFactory.parseString("""
      |property :[
      |   {key1 : 600},
      |   {key2 : "100"}
      |]
    """.stripMargin)
    val actual: Map[String, Any] = config.extract[Map[String, Any]]("property").get
    actual shouldEqual Map("key1" -> 600, "key2" -> "100")
  }

  test("Map[String, Any] should be parsed from an empty list of entries") {
    val config = ConfigFactory.parseString("""
      |property :[]
    """.stripMargin)
    val actual: Map[String, Any] = config.extract[Map[String, Any]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Double] should be parsed from an object") {
    val config = ConfigFactory.parseString("""
                                             |property {
                                             |   key1 : 1.1
                                             |   key2 : "2.2"
                                             |}
                                           """.stripMargin)
    val actual: Map[String, Double] = config.extract[Map[String, Double]]("property").get
    actual shouldEqual Map("key1" -> 1.1, "key2" -> 2.2)
  }

  test("Map[String, Double] should be parsed from an empty object") {
    val config = ConfigFactory.parseString("""
                                             |property {}
                                           """.stripMargin)
    val actual: Map[String, Double] = config.extract[Map[String, Double]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Double] should be parsed from a flat object") {
    val config = ConfigFactory.parseString("""
                                             |property.key1 = 1.1
                                             |property.key2 = 2.2
                                           """.stripMargin)
    val actual: Map[String, Double] = config.extract[Map[String, Double]]("property").get
    actual shouldEqual Map("key1" -> 1.1, "key2" -> 2.2)
  }

  test("Map[String, Double] should be parsed from a list of entries") {
    val config = ConfigFactory.parseString("""
                                             |property :[
                                             |   {key1 : 1.1},
                                             |   {key2 : "2.2"}
                                             |]
                                           """.stripMargin)
    val actual: Map[String, Double] = config.extract[Map[String, Double]]("property").get
    actual shouldEqual Map("key1" -> 1.1, "key2" -> 2.2)
  }

  test("Map[String, Double] should be parsed from an empty list of entries") {
    val config = ConfigFactory.parseString("""
                                             |property :[]
                                           """.stripMargin)
    val actual: Map[String, Double] = config.extract[Map[String, Double]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Int] should be parsed from an object") {
    val config = ConfigFactory.parseString("""
                                             |property {
                                             |   key1 : 1
                                             |   key2 : "2"
                                             |}
                                           """.stripMargin)
    val actual: Map[String, Int] = config.extract[Map[String, Int]]("property").get
    actual shouldEqual Map("key1" -> 1, "key2" -> 2)
  }

  test("Map[String, Int] should be parsed from an empty object") {
    val config = ConfigFactory.parseString("""
                                             |property {}
                                           """.stripMargin)
    val actual: Map[String, Int] = config.extract[Map[String, Int]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Int] should be parsed from a flat object") {
    val config = ConfigFactory.parseString("""
                                             |property.key1 = 1
                                             |property.key2 = 2
                                           """.stripMargin)
    val actual: Map[String, Int] = config.extract[Map[String, Int]]("property").get
    actual shouldEqual Map("key1" -> 1, "key2" -> 2)
  }

  test("Map[String, Int] should be parsed from a list of entries") {
    val config = ConfigFactory.parseString("""
                                             |property :[
                                             |   {key1 : 1},
                                             |   {key2 : "2"}
                                             |]
                                           """.stripMargin)
    val actual: Map[String, Int] = config.extract[Map[String, Int]]("property").get
    actual shouldEqual Map("key1" -> 1, "key2" -> 2)
  }

  test("Map[String, Int] should be parsed from an empty list of entries") {
    val config = ConfigFactory.parseString("""
                                             |property :[]
                                           """.stripMargin)
    val actual: Map[String, Int] = config.extract[Map[String, Int]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Long] should be parsed from an object") {
    val config = ConfigFactory.parseString("""
                                             |property {
                                             |   key1 : 1
                                             |   key2 : "2"
                                             |}
                                           """.stripMargin)
    val actual: Map[String, Long] = config.extract[Map[String, Long]]("property").get
    actual shouldEqual Map("key1" -> 1, "key2" -> 2)
  }

  test("Map[String, Long] should be parsed from an empty object") {
    val config = ConfigFactory.parseString("""
                                             |property {}
                                           """.stripMargin)
    val actual: Map[String, Long] = config.extract[Map[String, Long]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, Long] should be parsed from a flat object") {
    val config = ConfigFactory.parseString("""
                                             |property.key1 = 1
                                             |property.key2 = 2
                                           """.stripMargin)
    val actual: Map[String, Long] = config.extract[Map[String, Long]]("property").get
    actual shouldEqual Map("key1" -> 1, "key2" -> 2)
  }

  test("Map[String, Long] should be parsed from a list of entries") {
    val config = ConfigFactory.parseString("""
                                             |property :[
                                             |   {key1 : 1},
                                             |   {key2 : "2"}
                                             |]
                                           """.stripMargin)
    val actual: Map[String, Long] = config.extract[Map[String, Long]]("property").get
    actual shouldEqual Map("key1" -> 1, "key2" -> 2)
  }

  test("Map[String, Long] should be parsed from an empty list of entries") {
    val config = ConfigFactory.parseString("""
                                             |property :[]
                                           """.stripMargin)
    val actual: Map[String, Long] = config.extract[Map[String, Long]]("property").get
    actual shouldEqual Map()
  }

  test("Map[String, CustomConf] should be successfully parsed from an object") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |customs {
                                             |   c1: {prop1 : 1, prop2: [11, 12]}
                                             |   c2: {prop1 : 2, prop2: [21, 22]}
                                             |}
                                           """.stripMargin)
    val actual = config.extract[Map[String, CustomConf]]("customs").get
    val expected = Map("c1" -> CustomConf(1, Seq(11, 12)), "c2" -> CustomConf(2, Seq(21, 22)))
    actual should contain theSameElementsAs (expected)
  }

  test("Map[String, CustomConf] should fail parsing if any of the configurations are wrong") {
    implicit val customConfExtractor = CustomConf
    val config = ConfigFactory.parseString("""
                                             |customs {
                                             |   c1: {prop1 : 1, prop2: [11, 12]}
                                             |   c2: {prop1 : 2, prop2: [21, xx]}
                                             |}
                                           """.stripMargin)
    val actual = config.extract[Map[String, CustomConf]]("customs")
    actual shouldBe a[ZFailure[_]]
  }

}
