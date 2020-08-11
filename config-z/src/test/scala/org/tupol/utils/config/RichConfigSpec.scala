package org.tupol.utils.config

import com.typesafe.config.ConfigFactory
import org.scalatest.{ FunSuite, Matchers }
import org.tupol.utils.configz.RichConfig
import scalaz.{ Failure => ZFailure, Success => ZSuccess }

class RichConfigSpec extends FunSuite with Matchers {

  test("validatePath should succeed if the path exists") {
    val path   = "prop"
    val config = ConfigFactory.parseString(s""" $path: "value" """)
    val result = new RichConfig(config).validatePath(path)
    result shouldBe ZSuccess(path)
  }

  test("validatePath should fail if the path does not exist") {
    val path    = "prop"
    val badPath = s"bad-$path-bad-$path-bad"
    val config  = ConfigFactory.parseString(s""" $path: "value" """)
    val result  = new RichConfig(config).validatePath(badPath)
    result shouldBe a[ZFailure[_]]
  }

}
