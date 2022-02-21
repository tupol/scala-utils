package org.tupol.utils.jdbc

import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import org.tupol.utils.jdbc.config.ConnectionPoolConfig

import java.sql.Connection
import scala.util.Try

trait ConnectionPool {
  def connection: Try[Connection]
  def close: Unit
}

case class HikariConnectionPool private (datasource: HikariDataSource) extends ConnectionPool {
  override def connection: Try[Connection] = Try(datasource.getConnection)
  override def close: Unit                 = datasource.close
}

object HikariConnectionPool {

  def fromConfig(config: ConnectionPoolConfig): Try[HikariConnectionPool] =
    hikariConfig(config).map(new HikariDataSource(_)).map(HikariConnectionPool(_))

  private[jdbc] def hikariConfig(config: ConnectionPoolConfig): Try[HikariConfig] =
    Try {
      import config._
      val hikonf = new HikariConfig()
      hikonf.setPoolName(poolName)
      connectionTimeout.map(t => hikonf.setConnectionTimeout(t.toMillis))
      validationTimeout.map(t => hikonf.setValidationTimeout(t.toMillis))
      idleTimeout.map(t => hikonf.setIdleTimeout(t.toMillis))
      leakDetectionThreshold.map(t => hikonf.setLeakDetectionThreshold(t.toMillis))
      maxLifetime.map(t => hikonf.setMaxLifetime(t.toMillis))
      minimumIdle.map(hikonf.setMinimumIdle)
      maximumPoolSize.map(hikonf.setMaximumPoolSize)
      transactionIsolation.map(hikonf.setTransactionIsolation)
      connectionTestQuery.map(hikonf.setConnectionTestQuery)
      dataSourceClassName.map(hikonf.setDataSourceClassName)
      autoCommit.map(hikonf.setAutoCommit)
      readOnly.map(hikonf.setReadOnly)
      connectionInitSql.map(hikonf.setConnectionInitSql)

      hikonf.setJdbcUrl(jdbc.uri.toASCIIString)
      jdbc.user.map(hikonf.setUsername)
      jdbc.pass.map(hikonf.setPassword)
      jdbc.schema.map(hikonf.setSchema)

      hikonf.validate()
      hikonf
    }
}
