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

/** Implicit decorator functions for `Either[A, B]` and `F[Either[A, B]]` */
object EitherOps {

  /** Simple decorator for the `Iterable[Either[A, B]]` */
  implicit class IterableEitherOps[A, B](val eithers: Iterable[Either[A, B]]) {
    def flattenEithers: Either[A, Iterable[B]] = EitherUtils.flatten(eithers)
    def separate: (Iterable[A], Iterable[B])   = EitherUtils.separate(eithers)
    def lefts: Iterable[A]                     = EitherUtils.lefts(eithers)
    def rights: Iterable[B]                    = EitherUtils.rights(eithers)
  }

  /** Simple decorator for the `Array[Either[A, B]]` */
  implicit class ArrayEitherOps[A, B](val eithers: Array[Either[A, B]]) {
    def flattenEithers: Either[A, Iterable[B]] = EitherUtils.flatten(eithers)
    def separate: (Iterable[A], Iterable[B])   = EitherUtils.separate(eithers)
    def lefts: Iterable[A]                     = EitherUtils.lefts(eithers)
    def rights: Iterable[B]                    = EitherUtils.rights(eithers)
  }

  implicit class EitherOps[L, R](either: Either[L, R]) {

    /** Map an Either by applying f to Left only and leave R untouched */
    def mapLeft[X](f: L => X): Either[X, R] =
      either.mapEither(f, identity)

    /** Map an Either by applying f to Left, it is a Left; and applying g to Right when it's a Right */
    def mapEither[X, Y](f: L => X, g: R => Y): Either[X, Y] =
      either.fold(a => Left(f(a)), b => Right(g(b)))
  }

}
