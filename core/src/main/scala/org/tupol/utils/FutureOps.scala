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

import scala.concurrent.{ ExecutionContext, Future }

/** Implicit decorator functions for `Future[T]` and `F[Future[T]]`  */
object FutureOps {

  /** Simple decorator for `Future[T]`, which adds the following simple functions:
   * - composable error logging
   * - composable success logging
   * - failure mapping
   */
  implicit class FutureOps[T](val future: Future[T]) {

    /**  Log the error using the logging function and return the failure;
     * This is only called when the `Future` fails and it can be used like
     * {{{
     *   Future(something_that_can_fail()).logFailure(t => println(s"Got a Throwable: ${t}"))
     * }}}
     * @param logging logging function
     * @return the same future
     */
    def logFailure(logging: (Throwable) => Unit)(implicit ec: ExecutionContext): Future[T] =
      future.recoverWith {
        case t: Throwable =>
          logging(t)
          future
      }

    /** Log the success using the logging function and return the future
     * This is only called when the `Future` succeeds and it can be used like
     * {{{
     *   Future(something_that_succeeded()).logSuccess(success => println(s"Success: ${success}"))
     * }}}
     * @param logging logging function
     * @return the same future
     */
    def logSuccess(logging: (T) => Unit)(implicit ec: ExecutionContext): Future[T] =
      future.map { t =>
        logging(t)
        t
      }

    /** Log the progress and return back the future.
     * @param successLogging
     * @param failureLogging
     * @return the same future
     */
    def log(successLogging: (T) => Unit, failureLogging: (Throwable) => Unit)(
      implicit ec: ExecutionContext
    ): Future[T] =
      future.logSuccess(successLogging).logFailure(failureLogging)

    /** Wrap the exception into a new exception
     * @param map exception wrapping function
     * @return The original result or the mapped `Throwable`
     */
    def mapFailure(map: Throwable => Throwable)(implicit ec: ExecutionContext): Future[T] =
      future.transform((success: T) => success, (failure: Throwable) => map(failure))
  }

  /** Simple decorator for the `Traversable[Future[_]]` */
  implicit class TraversableFuturesOps[T](val trys: Traversable[Future[T]]) {

    /** Flatten a `Traversable[Future[T]]` to a `Future[Traversable[T]]`, which is a failure if any if the Futures is a failure.
     * In case of a failure, the latest `Failure` will be returned */
    def allOkOrFail(implicit ec: ExecutionContext): Future[Traversable[T]] = FutureUtils.allOkOrFail(trys)

  }
}
