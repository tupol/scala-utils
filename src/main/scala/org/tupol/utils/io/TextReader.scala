package org.tupol.utils.io

import java.nio.charset.{ Charset, StandardCharsets }

import org.tupol.utils._

import scala.util.Try

trait TextReader extends TextSource {
  def read(path: String, charSet: Charset = StandardCharsets.UTF_8, ignorableChars: Seq[Char] = Seq()): Try[String] =
    source(path, charSet).map(_.filterNot(ignorableChars.contains).mkString)
      .mapFailure(new TextReaderException(s"Failed to load text resource from '$path'.", _))
}

final case class TextReaderException(private val message: String) extends Exception(message) {
  def this(message: String, cause: Throwable) {
    this(message)
    initCause(cause)
  }
}

object FuzzyTextReader extends TextReader with FuzzySource
