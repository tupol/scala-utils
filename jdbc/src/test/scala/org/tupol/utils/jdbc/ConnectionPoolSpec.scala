package org.tupol.utils.jdbc

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.tupol.utils.jdbc.config.{ ConnectionPoolConfig, JdbcConfig }

import java.net.URI
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

class ConnectionPoolSpec extends AnyWordSpec with Matchers {

  "HikariConnectionPool.hikariConfig" should {
    "succeeds with defaults" in {
      val config = ConnectionPoolConfig(
        JdbcConfig(new URI("jdbc:postgresql://host:8080/database"), None, None, None),
        "some-pool-name"
      )
      val result = HikariConnectionPool.hikariConfig(config)
      result shouldBe a[Success[_]]
      result.get.getJdbcUrl shouldBe "jdbc:postgresql://host:8080/database"
      result.get.getPoolName shouldBe "some-pool-name"
      result.get.getTransactionIsolation shouldBe "TRANSACTION_READ_COMMITTED"
      result.get.getLeakDetectionThreshold shouldBe Duration(3, MINUTES).toMillis
      result.get.getUsername shouldBe null
      result.get.getPassword shouldBe null
      result.get.getSchema shouldBe null
      result.get.isAutoCommit shouldBe true
      result.get.isReadOnly shouldBe false
      result.get.getConnectionInitSql shouldBe null
    }
    "succeeds" in {
      val config = ConnectionPoolConfig(
        jdbc = JdbcConfig(
          uri = new URI("jdbc:postgresql://host:1234/database"),
          schema = Some("some-schema"),
          user = Some("some-user"),
          pass = Some("some-pass")
        ),
        poolName = "some-pool-name",
        connectionTimeout = Some(Duration(1, SECONDS)),
        validationTimeout = Some(Duration(10, SECONDS)),
        idleTimeout = Some(Duration(20, SECONDS)),
        leakDetectionThreshold = Some(Duration(30, SECONDS)),
        maxLifetime = Some(Duration(40, SECONDS)),
        minimumIdle = Some(1),
        maximumPoolSize = Some(2),
        transactionIsolation = Some("TRANSACTION_SERIALIZABLE"),
        connectionTestQuery = Some("some-test-query"),
        dataSourceClassName = Some("some-data-source-class-name"),
        autoCommit = Some(true),
        readOnly = Some(false),
        connectionInitSql = Some("SELECT 1;")
      )
      val result = HikariConnectionPool.hikariConfig(config)
      result shouldBe a[Success[_]]
      result.get.getJdbcUrl shouldBe "jdbc:postgresql://host:1234/database"
      result.get.getUsername shouldBe "some-user"
      result.get.getPassword shouldBe "some-pass"
      result.get.getSchema shouldBe "some-schema"
      result.get.getPoolName shouldBe "some-pool-name"
      result.get.getTransactionIsolation shouldBe "TRANSACTION_SERIALIZABLE"
      result.get.getConnectionTimeout shouldBe 1000
      result.get.getValidationTimeout shouldBe 10000
      result.get.getIdleTimeout shouldBe 20000
      result.get.getLeakDetectionThreshold shouldBe 30000
      result.get.getMaxLifetime shouldBe 40000
      result.get.getMinimumIdle shouldBe 1
      result.get.getMaximumPoolSize shouldBe 2
      result.get.getConnectionTestQuery shouldBe "some-test-query"
      result.get.getDataSourceClassName shouldBe "some-data-source-class-name"
      result.get.isAutoCommit shouldBe true
      result.get.isReadOnly shouldBe false
      result.get.getConnectionInitSql shouldBe "SELECT 1;"
    }
    "fails if the MaximumPoolSize is 0" in {
      val config = ConnectionPoolConfig(
        jdbc = JdbcConfig(
          uri = new URI("jdbc:postgresql://host:1234/database"),
          schema = None,
          user = None,
          pass = None
        ),
        poolName = "some-pool-name",
        maximumPoolSize = Some(0)
      )
      val result = HikariConnectionPool.hikariConfig(config)
      result shouldBe a[Failure[_]]
    }
  }

}
