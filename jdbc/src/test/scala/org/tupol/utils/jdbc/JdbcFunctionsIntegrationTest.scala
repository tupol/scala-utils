package org.tupol.utils.jdbc

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.tupol.utils.Bracket
import org.tupol.utils.jdbc.JdbcFunctionsIntegrationTest.{ JdbcTestData, JdbcTestDataExtractor }
import org.tupol.utils.jdbc.config.{ ConnectionPoolConfig, JdbcConfig }
import org.tupol.utils.jdbc.extractors._

import java.net.URI
import java.sql.ResultSet
import scala.util.{ Success, Try }

class JdbcFunctionsIntegrationTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  def createPostgresContainer(): PostgreSQLContainer[Nothing] = {
    val dockerImage = DockerImageName
      .parse("postgres:11")
      .asCompatibleSubstituteFor("postgres")
    new PostgreSQLContainer(dockerImage)
  }

  val container = createPostgresContainer()

  private var hcp: HikariConnectionPool = _

  /**
   * Before the execution of any suite that does operations
   * on the database, we initialize the database.
   */
  override def beforeAll(): Unit = {
    super.beforeAll()
    container.start()
    val config = ConnectionPoolConfig(
      JdbcConfig(new URI(container.getJdbcUrl), None, Some(container.getUsername), Some(container.getPassword)),
      "some-pool-name"
    )
    hcp = HikariConnectionPool.fromConfig(config).get
  }

  override def afterAll(): Unit = {
    hcp.close
    container.stop()
  }

  "basic integration test" should {
    "succeed" in {
      val result = Bracket
        .auto(hcp.connection.get) { con =>
          implicit val implicitCon = con
          for {
            _  <- update("CREATE TABLE test (col1 int, col2 float, col3 text)")(con)
            _  <- update("INSERT INTO test (col1, col2, col3) VALUES (1, 1.1, 's1')")(con)
            _  <- update("INSERT INTO test (col1, col2, col3) VALUES (2, 2.2, 's2')")(con)
            _  <- update("INSERT INTO test (col1, col2, col3) VALUES (3, 3.3, 's3')")(con)
            r1 <- query[JdbcTestData]("SELECT * FROM test")(con, JdbcTestDataExtractor)
            r2 <- queryCallable[JdbcTestData](
                    "SELECT * FROM test WHERE col1 > ? AND col3 LIKE ?",
                    Seq(1, "s2").map(SqlParam.fromAny)
                  )(con, JdbcTestDataExtractor)
            r3 <- queryStatement[JdbcTestData](
                    "SELECT * FROM test WHERE col1 > ? AND col3 LIKE ?",
                    Seq(1, "s3").map(SqlParam.fromAny)
                  )(con, JdbcTestDataExtractor)
          } yield (r1, r2, r3)
        }
        .flatten

      val expected1 = Seq(JdbcTestData(1, 1.1, "s1"), JdbcTestData(2, 2.2, "s2"), JdbcTestData(3, 3.3, "s3"))
      val expected2 = Seq(JdbcTestData(2, 2.2, "s2"))
      val expected3 = Seq(JdbcTestData(3, 3.3, "s3"))

      result shouldBe Success((expected1, expected2, expected3))
    }
  }

}

object JdbcFunctionsIntegrationTest {

  /** Test data model */
  case class JdbcTestData(col1: Int, col2: Double, col3: String)

  /** Test ata model ResultSet extractor */
  implicit object JdbcTestDataExtractor extends RowExtractor[JdbcTestData] {

    /** Extract typed structures out of a `ResultSet`. */
    override def extract(rs: ResultSet): Try[JdbcTestData] =
      for {
        col1 <- rs.extract[Int]("col1")
        col2 <- rs.extract[Double]("col2")
        col3 <- rs.extract[String]("col3")
      } yield JdbcTestData(col1, col2, col3)
  }

}
