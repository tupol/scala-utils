/*
MIT License

Copyright (c) 2018 Tupol (github.com/tupol)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package org.tupol.utils.io
import java.net.{ URI, URL }
import java.nio.charset.{ Charset, StandardCharsets }
import org.tupol.utils.implicits._

import scala.io.{ BufferedSource, Codec, Source }
import scala.util.{ Failure, Try }

trait TextSource {
  def source(path: String, charSet: Charset): Try[BufferedSource]
}

final case class TextSourceException(private val message: String) extends Exception(message) {
  def this(message: String, cause: Throwable) {
    this(message)
    initCause(cause)
  }
}

/**
 * This is a fancy text source that uses a process description for the loading of sources.
 * A process step is defined by it's name or type (e.g. file, classpath, uri, url...) and the corresponding factory
 * function.
 * The factory functions will be executed one by one until the first of them succeeds.
 * All the error messages are aggregated into the final exception.
 */
trait TextSourceProcess {
  type LabeledFactory = (String, () => BufferedSource) // (Label or the source type, source factory function)
  type LabeledAttempt = (String, Try[BufferedSource])  // (Label or the source type, source factory function attempt result)
  def process(path: String): Seq[LabeledFactory]

  def source(path: String, charSet: Charset = StandardCharsets.UTF_8): Try[BufferedSource] = {
    implicit val codec = Codec(charSet)
    val trimmedPath    = path.trim
    if (trimmedPath.isEmpty)
      Failure(new IllegalArgumentException(s"Cannot load a text resource from an empty path."))
    else {
      val (_, attempts): (Boolean, Seq[Try[BufferedSource]]) =
        process(trimmedPath).foldLeft((false, Seq[Try[BufferedSource]]())) { (acc, x) =>
          val (label, attempt)       = x
          val (prevResult, attempts) = acc
          if (!prevResult) {
            val currentAttempt = Try(attempt())
              .mapFailure(new TextSourceException(s"Could not load the resource as $label from '$trimmedPath'.", _))
            (currentAttempt.isSuccess, currentAttempt +: attempts)
          } else {
            acc
          }
        }
      val traceLog = attempts.collect { case (Failure(t)) => t.getMessage }.reverse.mkString("- ", s"\n- ", "")
      attempts.head
        .mapFailure(
          new TextSourceException(
            s"Failed to load text resource from '$trimmedPath' using the following attempts:\n" +
              traceLog,
            _
          )
        )
    }
  }
}

/**
 * Try loading a text resource from a given path, whether it is local, from a given URL or URI or from the classpath.
 * Also, the order of attempting to load the resource file corresponds to:
 * 1. local file
 * 2. URI
 * 3. URL
 * 4. classpath
 */
trait FuzzySource extends TextSource with TextSourceProcess {
  def process(path: String): Seq[LabeledFactory] =
    Seq(
      ("local file", () => Source.fromFile(path)),
      ("URI", () => Source.fromURI(new URI(path))),
      ("URL", () => Source.fromURL(new URL(path))),
      ("classpath", () => Source.fromURL(Thread.currentThread.getContextClassLoader.getResource(path)))
    )
}
