package org.tupol.utils.jdbc
import java.net.URI
import scala.concurrent.duration._

object config {

  /**
   * JDBC Connection configuration
   * @param uri database URI (like `jdbc:postgresql://host:port/database`)
   * @param schema
   * @param user
   * @param pass
   */
  case class JdbcConfig(
                         uri: URI,
                         schema: Option[String],
                         user: Option[String],
                         pass: Option[String]
                       )

  /**
   * Connection pool configuration class
   * @param jdbc
   * @param poolName The name of the connection pool.
   * @param connectionTimeout The maximum duration that a client will wait for a connection from the pool.
   *                          If this time is exceeded without a connection becoming available, a SQLException
   *                          will be thrown from `javax.sql.DataSource#getConnection()`.
   * @param validationTimeout The duration that the pool will wait for a connection to be validated as alive.
   * @param idleTimeout This property controls the maximum amount of time that a connection is allowed to sit idle
   *                    in the pool. Whether a connection is retired as idle or not is subject to a maximum
   *                    variation of +30 seconds, and average variation of +15 seconds.
   *                    A connection will never be retired as idle before this timeout.
   *                    A value of 0 means that idle connections are never removed from the pool.
   * @param leakDetectionThreshold This property controls the amount of time that a connection can be out of the
   *                               pool before a message is logged indicating a possible connection leak.
   *                               A value of 0 means leak detection is disabled.
   * @param maxLifetime This property controls the maximum lifetime of a connection in the pool. When a connection
   *                    reaches this timeout, even if recently used, it will be retired from the pool.
   *                    An in-use connection will never be retired, only when it is idle will it be removed.
   * @param minimumIdle The property controls the minimum number of idle connections that HikariCP tries to maintain
   *                    in the pool, including both idle and in-use connections. If the idle connections dip below
   *                    this value, HikariCP will make a best effort to restore them quickly and efficiently.
   * @param maximumPoolSize The property controls the maximum number of connections that HikariCP will keep in
   *                        the pool, including both idle and in-use connections.
   * @param transactionIsolation Set the default transaction isolation level.  The specified value is the constant
   *                             name from `java.sql.Connection`.
   *                             Possible values: `TRANSACTION_READ_UNCOMMITTED`, `TRANSACTION_READ_COMMITTED`,
   *                             `TRANSACTION_REPEATABLE_READ` or `TRANSACTION_SERIALIZABLE`
   * @param connectionTestQuery The SQL query to be executed to test the validity of connections. Using the JDBC4
   *                            `Connection.isValid()` method to test connection validity can be more efficient on
   *                            some databases and is recommended.
   * @param dataSourceClassName The name of `javax.sql.DataSource` class used to create Connections.
   * @param readOnly The property sets the connection to read-only (in case of true) or read-write (in case of false)
   * @param connectionInitSql Set the SQL string that will be executed on all new connections when they are
   *                          created, before they are added to the pool.  If this query fails, it will be
   *                          treated as a failed connection attempt.
   */
  case class ConnectionPoolConfig(
                                   jdbc: JdbcConfig,
                                   poolName: String,
                                   connectionTimeout: Option[Duration] = None,
                                   validationTimeout: Option[Duration] = None,
                                   idleTimeout: Option[Duration] = None,
                                   leakDetectionThreshold: Option[Duration] = Some(Duration(3, MINUTES)),
                                   maxLifetime: Option[Duration] = None,
                                   minimumIdle: Option[Int] = None,
                                   maximumPoolSize: Option[Int] = None,
                                   transactionIsolation: Option[String] = Some("TRANSACTION_READ_COMMITTED"),
                                   connectionTestQuery: Option[String] = None,
                                   dataSourceClassName: Option[String] = None,
                                   autoCommit: Option[Boolean] = None,
                                   readOnly: Option[Boolean] = None,
                                   connectionInitSql: Option[String] = None
                                 )
}