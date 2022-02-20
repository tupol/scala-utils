package org.tupol.utils.jdbc

import java.sql.ResultSet

object ResultSetUtils {

  /** Convert a `ResultSet` into an `Iterator` */
  def toIterator(rs: ResultSet): Iterator[ResultSet] =
    Iterator
      .continually((rs.next(), rs))
      .takeWhile(_._1)
      .map(_._2)

}
