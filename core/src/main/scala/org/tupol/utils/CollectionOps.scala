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

/**  Implicit decorator functions for working with various collections */
object CollectionOps {
  import CollectionUtils._

  /** Decorate Scala Maps with various utility functions */
  implicit class MapOps[K, V](val map: Map[K, V]) {
    def asProperties: java.util.Properties = map2Properties(map)
    def asHashMap: java.util.HashMap[K, V] = map2HashMap(map)
    /**
     * Convert a sequence into an option of None if empty or Some of elements; Nel: Non-Empty List
     */
    def toOptionNel: Option[Map[K, V]] = if(map.isEmpty) None else Some(map)
  }

  implicit class SeqOps[T](xs: Seq[T]) {
    /** Convert a sequence into an option of None if empty or Some of elements; Nel: Non-Empty List */
    def toOptionNel: Option[Seq[T]] = if(xs.isEmpty) None else Some(xs)
  }

  implicit class SetOps[T](xs: Set[T]) {
    /** Convert a set into an option of None if empty or Some of elements; Nel: Non-Empty List */
    def toOptionNel: Option[Set[T]] = if(xs.isEmpty) None else Some(xs)
  }

  implicit class TraversableOps[T](xs: Traversable[T]) {
    /** Convert a traversable into an option of None if empty or Some of elements; Nel: Non-Empty List */
    def toOptionNel: Option[Traversable[T]] = if(xs.isEmpty) None else Some(xs)
  }

  implicit class VectorOps[T](xs: Vector[T]) {
    /** Convert a vector into an option of None if empty or Some of elements; Nel: Non-Empty List */
    def toOptionNel: Option[Vector[T]] = if(xs.isEmpty) None else Some(xs)
  }

  implicit class IterableOps[T](xs: Iterable[T]) {
    /** Convert an iterable into an option of None if empty or Some of elements; Nel: Non-Empty List */
    def toOptionNel: Option[Iterable[T]] = if(xs.isEmpty) None else Some(xs)
  }

  implicit class IteratorOps[T](xs: Iterator[T]) {
    /** Convert an iterator into an option of None if empty or Some of elements; Nel: Non-Empty List */
    def toOptionNel: Option[Iterator[T]] = if(xs.isEmpty) None else Some(xs)
  }
}
