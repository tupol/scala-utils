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

object Bracket {

  /**
   * The `Bracket` tries running a `code` block that uses a `resource` which is silently closed on return using the
   * provided `close` function.
   *
   * @param resource resource creation block
   * @param close resource closing block
   * @param code code that uses the resource
   * @tparam R resource type which must be closed by `close`
   * @tparam T the code block return type
   * @return Success if the resource was successfully initialised and the code was successfully ran,
   *         even if the resource was not successfully closed.
   */
  def apply[R, T](resource: => R)(close: R => Unit)(code: R => T): Try[T] = {
    val res    = Try(resource)
    val result = res.map(code)
    res.map(r => Try(close(r)))
    result
  }

  /**
   * The `Bracket.auto` tries running a `code` block that uses a `resource` which is silently closed on return.
   * @param resource resource creation block
   * @param code code that uses the resource
   * @tparam R resource type which must be [[AutoCloseable]]
   * @tparam T the code block return type
   * @return Success if the resource was successfully initialised and the code was successfully ran,
   *         even if the resource was not successfully closed.
   */
  def auto[R <: AutoCloseable, T](resource: => R)(code: R => T): Try[T] =
    Bracket(resource)(_.close())(code)
}
