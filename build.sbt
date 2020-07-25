import Dependencies.{Versions, _}
import com.typesafe.sbt.SbtPgp.autoImportImpl.useGpg
import sbt.Keys.{libraryDependencies, organization}
import sbt.url
import sbtbuildinfo.BuildInfoPlugin.autoImport.{buildInfoKeys, buildInfoOptions}
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val publishSettings = Seq(
  isSnapshot := version.value.trim.endsWith("SNAPSHOT"),
  useGpg := true,
  // Nexus (see https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html)
  publishTo := {
    val repo = "https://oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at repo + "content/repositories/snapshots")
    else Some("releases" at repo + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := true,
  publishMavenStyle := true,
  pomIncludeRepository := { x => false },
  licenses := Seq("MIT-style" -> url("https://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/tupol/scala-utils")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/tupol/scala-utils.git"),
      "scm:git@github.com:tupol/scala-utils.git"
    )
  ),
  developers := List(
    Developer(
      id = "tupol",
      name = "Tupol",
      email = "tupol.github@gmail.com",
      url = url("https://github.com/tupol")
    )
  ),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies, // : ReleaseStep
    inquireVersions, // : ReleaseStep
    runClean, // : ReleaseStep
    runTest, // : ReleaseStep
    setReleaseVersion, // : ReleaseStep
    commitReleaseVersion, // : ReleaseStep, performs the initial git checks
    tagRelease, // : ReleaseStep
    releaseStepCommand(s"""sonatypeOpen "${organization.value}" "${name.value} v${version.value}""""),
    releaseStepCommand("+publishSigned"),
    releaseStepCommand("+sonatypeRelease"),
    setNextVersion, // : ReleaseStep
    commitNextVersion, // : ReleaseStep
    pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
  )
)

lazy val coverageSettings = Seq(
  coverageEnabled in Test := true,
  coverageMinimum in Test := 95,
  coverageFailOnMinimum in Test := true,
  coverageExcludedPackages := ".*BuildInfo.*"
)
lazy val basicSettings = Seq(
  organization := "org.tupol",
  name := "scala-utils",
  scalaVersion := Versions.scala,
  crossScalaVersions := Versions.crossScala,
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Ywarn-unused-import"
  ),
  updateOptions := updateOptions.value.withCachedResolution(true),
  libraryDependencies ++= CoreTestDependencies,
  scoverage.ScoverageKeys.coverageExcludedFiles := ".*BuildInfo.*"

)
val commonSettings = basicSettings ++ coverageSettings ++ publishSettings


lazy val core = (project in file("core"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "scala-utils-core",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoOptions := Seq[BuildInfoOption](BuildInfoOption.BuildTime, BuildInfoOption.ToMap, BuildInfoOption.ToJson),
    buildInfoPackage := "org.tupol.utils",
    libraryDependencies ++= CoreDependencies,
    publishArtifact in Test := true
  )

lazy val config_z = (project in file("config-z"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "scala-utils-config-z",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoOptions := Seq[BuildInfoOption](BuildInfoOption.BuildTime, BuildInfoOption.ToMap, BuildInfoOption.ToJson),
    buildInfoPackage := "org.tupol.utils.config",
    libraryDependencies ++= ConfigZDependencies
  )
  .dependsOn(core % "test->test;compile->compile")

lazy val scala_utils = Project(
  id = "scala-utils",
  base = file(".")
).settings(commonSettings: _*)
  .dependsOn(core % "test->test;compile->compile", config_z)
  .aggregate(core, config_z)
