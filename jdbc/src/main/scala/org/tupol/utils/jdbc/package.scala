package org.tupol.utils
import org.tupol.utils.implicits._
import org.tupol.utils.jdbc.extractors.{Extractor, RowExtractor}

import java.sql.{Connection, ResultSet}
import scala.util.Try

package object jdbc {

  implicit class ResultSetOps(rs: ResultSet) {
    def extract[T](col: String)(implicit extractor: Extractor[T]): Try[T] =
      extractor.extract(rs, col)
    def extract[T](col: Int)(implicit extractor: Extractor[T]): Try[T] =
      extractor.extract(rs, col)
    def extract[T](implicit extractor: RowExtractor[T]): Try[T] =
      extractor.extract(rs)
    def toIterator: Iterator[ResultSet] = ResultSetUtils.toIterator(rs)
  }

  /** Executes a sql statement such as INSERT, UPDATE, DELETE, or DDL statement.
   * The accepted sql statements are the same as in `java.sql.Statement.executeUpdate`. */
  def update(connection: Connection, sql: String): Try[Int] =
    Bracket.auto(connection.createStatement()) { _.executeUpdate(sql) }

  /** Executes a sql statement */
  def query[T](connection: Connection, sql: String)(implicit extractor: RowExtractor[T]): Try[Iterable[T]] =
    Bracket
      .auto(connection.createStatement()) { stmt =>
        Bracket
          .auto(stmt.executeQuery(sql)) { rs =>
            rs.toIterator.map(rs => rs.extract[T]).toIterable.allOkOrFail
          }
          .flatten
      }
      .flatten.map(_.toIterable)

  /** Executes a callable sql statement */
  def queryCallable[T](connection: Connection, sql: String, params: Seq[SqlParam[_]] = Seq())(
    implicit extractor: RowExtractor[T]
  ): Try[Iterable[T]] =
    Bracket
      .auto(connection.prepareCall(sql)) { stmt =>
        for {
          _ <- params.zipWithIndex.map { case (param, index) => param.set(stmt, index + 1) }.allOkOrFail
          result <- Bracket
            .auto(stmt.executeQuery()) { rs =>
              rs.toIterator.map(rs => rs.extract[T]).toIterable.allOkOrFail
            }
            .flatten
        } yield result
      }
      .flatten.map(_.toIterable)

  def commitClose(connection: Connection): Try[Unit] =
    Bracket.auto(connection) { _.commit() }

  def rollbackClose(connection: Connection): Try[Unit] =
    Bracket.auto(connection) { _.rollback() }
}
