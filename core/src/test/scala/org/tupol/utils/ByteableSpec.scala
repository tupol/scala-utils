package org.tupol.utils

import org.scalatest.funsuite.AnyFunSuite

import java.nio.ByteBuffer
import org.scalatest.matchers.should.Matchers
import org.tupol.utils.ByteableOps._

class ByteableSpec extends AnyFunSuite with Matchers {

  test("short round-trip") {
    val value = 12.toShort
    ByteBuffer.wrap(value.toByteArray).asShortBuffer.get shouldBe value
  }

  test("int round-trip") {
    val value = 12
    ByteBuffer.wrap(value.toByteArray).asIntBuffer.get shouldBe value
  }

  test("long round-trip") {
    val value = 12.toLong
    ByteBuffer.wrap(value.toByteArray).asLongBuffer.get shouldBe value
  }

  test("float round-trip") {
    val value = 12.12.toFloat
    ByteBuffer.wrap(value.toByteArray).asFloatBuffer.get shouldBe value
  }

  test("double round-trip") {
    val value = 12.12
    ByteBuffer.wrap(value.toByteArray).asDoubleBuffer.get shouldBe value
  }

}
