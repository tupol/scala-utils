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
package org.tupol.utils

import scala.util.{ Failure, Success, Try }

/** Explicit utility functions for working with `Try[T]` and `F[Try[T]]` */
object TryUtils {

  /**
   * Flatten a `Iterable[Try[T]]` to a `Try[Iterable[T]]`, which is a failure if ANY of the Trys is a failure.
   * In case of a failure, the latest `Failure` will be returned
   */
  def allOkOrFail[T](trys: Iterable[Try[T]]): Try[Iterable[T]] = {
    import scala.collection.immutable.Vector
    trys.foldLeft(Try(Vector.empty[T])) { (acc, tryField) =>
      tryField.flatMap(tf => acc.map(_ :+ tf))
    }
  }

  /** Flatten a `Iterable[Try[T]]` to a `Try[Iterable[T]]`, which is a failure if ALL of the Trys is a failure. */
  def collectOkOrFail[T](trys: Iterable[Try[T]]): Try[Iterable[T]] =
    trys match {
      case Nil => Success(Nil)
      case _   =>
        trys.collect { case Success(x) => x } match {
          case Nil => Failure(new Exception("Everything failed."))
          case col => Success(col)
        }
    }

}
