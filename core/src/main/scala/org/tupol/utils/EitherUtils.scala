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

/** Explicit utility functions for working with `Either[A, B]` and `F[Either[A, B]]`*/
object EitherUtils {

  /** The last Left prevails (if any), otherwise all Right-s collected in order of occurrence (possibly empty). */
  def flatten[A, B](eithers: Traversable[Either[A, B]]): Either[A, Traversable[B]] = {
    val (lefts, rights) = separate(eithers)
    if (lefts.isEmpty) Right(rights)
    else Left(lefts.last)
  }

  /** Partitions a sequence of Either-s into their left and right elements, similar to Haskell's `partitionEithers` */
  def separate[A, B](eithers: Traversable[Either[A, B]]): (Traversable[A], Traversable[B]) = {
    import scala.collection.immutable.Vector
    eithers.foldLeft((Vector.empty[A], Vector.empty[B]))(
      (acc, x) =>
        x match {
          case Left(a)  => (acc._1 :+ a, acc._2)
          case Right(b) => (acc._1, acc._2 :+ b)
        }
    )
  }

  /** All left elements from a sequence of Either-s */
  def lefts[A, B](eithers: Traversable[Either[A, B]]): Traversable[A] =
    eithers collect { case Left(l) => l }

  /** All right elements from a sequence of Either-s */
  def rights[A, B](eithers: Traversable[Either[A, B]]): Traversable[B] =
    eithers collect { case Right(r) => r }

}
