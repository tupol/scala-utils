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

import scala.util.Try

sealed abstract class Result[+E, +A] {
  def map[B](f: A => B): Result[E, B]
  def flatMap[EE >: E, B](f: A => Result[EE, B]): Result[EE, B]
  def fold[B](error: E => B, success: A => B): B
  
  def withFilter(p: A => Boolean): Result[E, A] = this match {
    case Result.Success(a) if p(a) => this
    case Result.Success(_) => Result.Error(throw new NoSuchElementException("Result.filter predicate is not satisfied"))
    case e @ Result.Error(_) => e
  }

  def isSuccess: Boolean
  def isError: Boolean
  
  def toOption: Option[A]
  def toEither: Either[E, A]
  def toTry(implicit ev: E <:< Throwable): Try[A]
}

object Result {
  final case class Success[+A](value: A) extends Result[Nothing, A] {
    def map[B](f: A => B): Result[Nothing, B] = Success(f(value))
    
    def flatMap[EE >: Nothing, B](f: A => Result[EE, B]): Result[EE, B] = 
      f(value)
    
    def fold[B](error: Nothing => B, success: A => B): B = success(value)
    
    def isSuccess: Boolean = true
    def isError: Boolean = false
    
    def toOption: Option[A] = Some(value)
    def toEither: Either[Nothing, A] = Right(value)
    def toTry(implicit ev: Nothing <:< Throwable): Try[A] = 
      scala.util.Success(value)
  }
  
  final case class Error[+E](error: E) extends Result[E, Nothing] {
    def map[B](f: Nothing => B): Result[E, B] = this
    
    def flatMap[EE >: E, B](f: Nothing => Result[EE, B]): Result[EE, B] = 
      this
    
    def fold[B](error: E => B, success: Nothing => B): B = error(this.error)
    
    def isSuccess: Boolean = false
    def isError: Boolean = true
    
    def toOption: Option[Nothing] = None
    def toEither: Either[E, Nothing] = Left(error)
    def toTry(implicit ev: E <:< Throwable): Try[Nothing] = 
      scala.util.Failure(ev(error))
  }
  
  def success[A](value: A): Result[Nothing, A] = Success(value)
  def error[E](error: E): Result[E, Nothing] = Error(error)
  
  // Companion object utility methods
  def fromOption[E, A](option: Option[A], error: => E): Result[E, A] =
    option.fold[Result[E, A]](Error(error))(Success(_))
    
  def fromEither[E, A](either: Either[E, A]): Result[E, A] =
    either.fold(Error(_), Success(_))
    
  def fromTry[A](t: Try[A]): Result[Throwable, A] =
    t.fold(Error(_), Success(_))
}
