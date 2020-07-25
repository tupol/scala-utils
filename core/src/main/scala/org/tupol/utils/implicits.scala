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
import scala.util.Try

/** Package wide implicits */
object implicits {

  implicit class TryOpsImplicits[T](override val attempt: Try[T]) extends TryOps.TryOps[T](attempt)
  implicit class TraversableTryOpsImplicits[T](override val trys: Traversable[Try[T]])
      extends TryOps.TraversableTryOps[T](trys)
  implicit class ArrayTryOpsImplicits[T](override val trys: Array[Try[T]]) extends TryOps.ArrayTryOps[T](trys)

  implicit class EitherOpsImplicits[L, R](either: Either[L, R]) extends EitherOps.EitherOps[L, R](either)
  implicit class TraversableEitherOpsImplicits[A, B](override val eithers: Traversable[Either[A, B]])
      extends EitherOps.TraversableEitherOps(eithers)
  implicit class ArrayEitherOpsImplicits[A, B](override val eithers: Array[Either[A, B]])
      extends EitherOps.ArrayEitherOps(eithers)

  implicit class ProductOfOptionsImplicits(product: Product) extends ProductOps.ProductOfOptionsOps(product)

  implicit class FutureOpsImplicits[T](override val future: Future[T]) extends FutureOps.FutureOps[T](future)

  implicit class MapOpsImplicits[K, V](override val map: Map[K, V]) extends CollectionOps.MapOps[K, V](map)

}
