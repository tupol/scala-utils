import sbt._

object Dependencies {

  object Versions {
    val scala            = "2.12.12"
    val crossScala       = Seq(scala)
    val scalaz           = "7.2.26"
    val scalatest        = "3.0.8"
    val scalacheck       = "1.14.1"
    val json4s           = "3.6.8"
    val scala_logging    = "3.9.2"
    val mockito          = "1.14.4"
    val typesafe_config  = "1.4.0"
  }

  val CoreTestDependencies: Seq[ModuleID] = Seq(
    "org.scalatest"  %% "scalatest"     % Versions.scalatest  % Test cross CrossVersion.binary,
    "org.scalacheck" %% "scalacheck"    % Versions.scalacheck % Test cross CrossVersion.binary,
    "org.mockito"    %% "mockito-scala" % Versions.mockito    % Test cross CrossVersion.binary
  )

  val CoreDependencies: Seq[ModuleID] = CoreTestDependencies ++ Seq()

  val ConfigZDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.scala-logging" % "scala-logging" % Versions.scala_logging cross CrossVersion.binary,
    "org.scala-lang" % "scala-reflect" % Versions.scala,
    "org.scalaz" % "scalaz-core" % Versions.scalaz cross CrossVersion.binary,
    "com.typesafe" % "config" % Versions.typesafe_config
  )

}
