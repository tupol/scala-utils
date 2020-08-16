package org.tupol.utils

import scala.concurrent.{ ExecutionContext, Future }

/** Explicit utility functions for working with `Future[T]` and `F[Future[T]]`*/
object FutureUtils {

  /** Flatten a `Traversable[Future[T]]` to a `Future[Traversable[T]]`, which is a failure if ANY of the Futures is a failure.
   * In case of a failure, the latest one will be returned. */
  def allOkOrFail[T](futures: Traversable[Future[T]])(implicit ec: ExecutionContext): Future[Traversable[T]] = {
    import scala.collection.immutable.Vector
    futures.foldLeft(Future.successful(Vector.empty[T])) { (acc, future) =>
      future.flatMap(tf => acc.map(_ :+ tf))
    }
  }

}
