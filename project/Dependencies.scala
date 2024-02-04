import sbt._

object Dependencies {

  object Versions {
    val targetJava       = "8"
    val scala_2_12       = "2.12.18"
    val scala_2_13       = "2.13.12"
    val scala            = scala_2_13
    val crossScala       = Seq(scala_2_12, scala_2_13)
    val scalatest        = "3.1.1"
    val scalacheck       = "1.15.1"
    val json4s           = "3.6.8"
    val scala_logging    = "3.9.2"
    val mockito          = "1.14.4"
    val typesafe_config  = "1.4.0"
    val postgresql       = "42.2.+"
    val testcontainers   = "1.16.+"
    val hikari           = "3.4.+"
  }

  val CoreTestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest"  %% "scalatest"     % Versions.scalatest  % Test cross CrossVersion.binary,
    "org.scalacheck" %% "scalacheck"    % Versions.scalacheck % Test cross CrossVersion.binary,
    "org.mockito"    %% "mockito-scala" % Versions.mockito    % Test cross CrossVersion.binary
  )

  val CoreDependencies: Seq[ModuleID] = CoreTestDependencies ++ Seq()


  val JdbcDependencies: Seq[ModuleID] = Seq(
    "com.zaxxer"            % "HikariCP"    % Versions.hikari         % Provided,
    "org.testcontainers"    % "postgresql"  % Versions.testcontainers % Test,
    "org.postgresql"        % "postgresql"  % Versions.postgresql     % Test,
  )

}
