package org.tupol.utils

import java.util
import java.util.Properties

import org.scalatest.{ FunSuite, Matchers }

import scala.collection.JavaConverters._

class UtilsSpec extends FunSuite with Matchers {

  test("map2Properties for an empty map should yield an empty properties instance") {
    val result = map2Properties(Map[String, String]())
    result.propertyNames.asScala shouldBe empty
    result.values.asScala shouldBe empty
  }

  test("map2Properties for map of strings should yield the corresponding properties instance") {
    val result = map2Properties(Map[String, String]("key1" -> "val1", "key2" -> "val2"))
    val expected = new Properties()
    expected.setProperty("key1", "val1")
    expected.setProperty("key2", "val2")
    result shouldBe expected
  }

  test("map2Properties for map of ints and booleans should yield the corresponding properties instance") {
    val result = map2Properties(Map[Int, Boolean](1 -> true, 2 -> false))
    val expected = new Properties()
    expected.setProperty("1", "true")
    expected.setProperty("2", "false")
    result shouldBe expected
  }

  test("map2HashMap for an empty map should yield an empty HashMap instance") {
    val result = map2HashMap(Map[String, String]())
    result.keySet.asScala shouldBe empty
    result.values().asScala shouldBe empty
  }

  test("map2HashMap for map of strings should yield the corresponding HashMap instance") {
    val result = map2HashMap(Map[String, String]("key1" -> "val1", "key2" -> "val2"))
    val expected = new util.HashMap[String, String]()
    expected.put("key1", "val1")
    expected.put("key2", "val2")
    result shouldBe expected
  }

  test("map2HashMap for map of ints and booleans should yield the corresponding HashMap instance") {
    val result = map2HashMap(Map[Int, Boolean](1 -> true, 2 -> false))
    val expected = new util.HashMap[Int, Boolean]()
    expected.put(1, true)
    expected.put(2, false)
    result shouldBe expected
  }
}
