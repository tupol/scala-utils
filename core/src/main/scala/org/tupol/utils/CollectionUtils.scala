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

/** Explicit utility functions for working with various collections */
object CollectionUtils {

  /** Convert a Scala Map into a Java Properties instance */
  def map2Properties[K, V](map: Map[K, V]): java.util.Properties = {
    val properties = new java.util.Properties()
    map.foreach { case (k, v) => properties.setProperty(k.toString, v.toString) }
    properties
  }

  /** Convert a Scala Map into a Java HashMap instance */
  def map2HashMap[K, V](map: Map[K, V]): java.util.HashMap[K, V] = {
    val hashmap = new java.util.HashMap[K, V]()
    map.foreach { case (k, v) => hashmap.put(k, v) }
    hashmap
  }
}
