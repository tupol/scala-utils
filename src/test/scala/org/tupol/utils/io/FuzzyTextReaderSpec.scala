package org.tupol.utils.io

import org.scalatest.{ FunSuite, Matchers }

import scala.util.Failure

class FuzzyTextReaderSpec extends FunSuite with Matchers {

  import FuzzyTextReader._

  val expectedText =
    """line 1
      |line 2
      |""".stripMargin

  test("FuzzyTextReader.read fails while trying to load an empty path") {
    val path = ""
    read("") shouldBe a[Failure[_]]
  }

  test("FuzzyTextReader.read fails while trying to load a path that does not exist anywhere") {
    val path = "/unknown/path/leading/to/unknown/file/s1n.ci7y"
    read(path) shouldBe a[Failure[_]]
  }

  test("FuzzyTextReader.read successfully loads a text from a local path") {
    val path = "src/test/resources/io/sample-text.resource"
    val result = read(path).get
    result shouldBe expectedText
  }

  test("FuzzyTextReader.read successfully loads a text from the class path") {
    val path = "io/sample-text.resource"
    val result = read(path).get
    result shouldBe expectedText
  }

  test("FuzzyTextReader.read successfully loads a text from a local path and ignores chars") {
    val path = "src/test/resources/io/sample-text.resource"
    val result = read(path = path, ignorableChars = "line ".toCharArray).get
    result shouldBe
      """1
        |2
        |""".stripMargin
  }

  test("FuzzyTextReader.read successfully loads a text from the URI") {
    val path = new java.io.File("src/test/resources/io/sample-text.resource").toURI.toASCIIString
    val result = read(path).get
    result shouldBe expectedText
  }

  test("FuzzyTextReader.read successfully loads a text from the URL") {
    val path = new java.io.File("src/test/resources/io/sample-text.resource").toURI.toURL.toString
    val result = read(path).get
    result.size should be > 10
  }

}
