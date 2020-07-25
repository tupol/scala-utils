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

import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

/** Implicit decorator functions for `Try[T]` and `F[Try[T]]`  */
object TryOps {

  /** Simple decorator for `Try[T]`, which adds the following simple functions:
   * - composable error logging
   * - composable success logging
   * - failure mapping
   */
  implicit class TryOps[T](val attempt: Try[T]) {

    /**  Log the error using the logging function and return the failure;
     * This is only called when the `Try` is a Failure and it can be used like
     * {{{
     *   Try(something_that_can_fail()).logFailure(t => println(s"Got a Throwable: ${t}"))
     * }}}
     * @param logging logging function
     * @return the same attempt
     */
    def logFailure(logging: (Throwable) => Unit): Try[T] =
      attempt.recoverWith {
        case t: Throwable =>
          logging(t)
          attempt
      }

    /** Log the success using the logging function and return the attempt
     * This is only called when the `Try` is a Success and it can be used like
     * {{{
     *   Try(something_that_succeeded()).logSuccess(success => println(s"Success: ${success}"))
     * }}}
     * @param logging logging function
     * @return the same attempt
     */
    def logSuccess(logging: (T) => Unit): Try[T] =
      attempt.map { t =>
        logging(t)
        t
      }

    /** Log the progress and return back the try.
     * @param successLogging
     * @param failureLogging
     * @return the same attempt
     */
    def log(successLogging: (T) => Unit, failureLogging: (Throwable) => Unit): Try[T] =
      attempt.logSuccess(successLogging).logFailure(failureLogging)

    /** Wrap the exception into a new exception
     * @param map exception wrapping function
     * @return The original `Success[T]` or a `Failure` containing the mapped `Throwable`
     */
    def mapFailure(map: Throwable => Throwable): Try[T] =
      attempt match {
        case Success(s) => attempt
        case Failure(t) => Failure(map(t))
      }

    /** Convenience decoration to convert `Try[T]` to `Future[T]` using Future.fromTry`` */
    def asFuture: Future[T] = Future.fromTry(attempt)
  }

  /** Simple decorator for the `Traversable[Try[_]]` */
  implicit class TraversableTryOps[T](val trys: Traversable[Try[T]]) {

    /** Flatten a `Traversable[Try[T]]` to a `Try[Traversable[T]]`, which is a failure if any if the Trys is a failure.
     * In case of a failure, the latest `Failure` will be returned */
    def allOkOrFail: Try[Traversable[T]] = TryUtils.allOkOrFail(trys)

    /** Flatten a `Traversable[Try[T]]` to a `Try[Traversable[T]]`, which is a failure if ALL of the Trys is a failure. */
    def collectOkOrFail: Try[Traversable[T]] = TryUtils.collectOkOrFail(trys)

  }

  /** Simple decorator for the `Array[Try[_]]` */
  implicit class ArrayTryOps[T](val trys: Array[Try[T]]) {

    /** Flatten a `Array[Try[T]]` to a `Try[Array[T]]`, which is a failure if any if the Trys is a failure.
     * In case of a failure, the latest `Failure` will be returned */
    def allOkOrFail: Try[Traversable[T]] = TryUtils.allOkOrFail(trys)

    /** Flatten a `Traversable[Try[T]]` to a `Try[Traversable[T]]`, which is a failure if ALL of the Trys is a failure. */
    def collectOkOrFail: Try[Traversable[T]] = TryUtils.collectOkOrFail(trys)
  }
}
