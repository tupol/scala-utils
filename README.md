# Scala Utils

[![Maven Central](https://img.shields.io/maven-central/v/org.tupol/scala-utils_2.12.svg)](https://mvnrepository.com/artifact/org.tupol/scala-utils) &nbsp;
[![GitHub](https://img.shields.io/github/license/tupol/scala-utils.svg)](https://github.com/tupol/scala-utils/blob/master/LICENSE) &nbsp; 
[![Travis (.org)](https://img.shields.io/travis/tupol/scala-utils.svg)](https://travis-ci.com/tupol/scala-utils) &nbsp; 
[![Codecov](https://img.shields.io/codecov/c/github/tupol/scala-utils.svg)](https://codecov.io/gh/tupol/scala-utils) &nbsp;
[![Javadocs](https://www.javadoc.io/badge/org.tupol/scala-utils_2.12.svg)](https://www.javadoc.io/doc/org.tupol/scala-utils_2.12) &nbsp;
[![Discord](https://img.shields.io/discord/963384629572812860)](https://discord.gg/kDFsJ3y7Du) &nbsp;
[![Twitter](https://img.shields.io/twitter/url/https/_tupol.svg?color=%2317A2F2)](https://twitter.com/_tupol) &nbsp;


## Description

This project contains some basic utilities that can help setting up a Scala project.

The main utilities available:
- Conversion to byte array in the [`byteable`](core/src/main/scala/org/tupol/utils/ByteableOps.scala) package.
- `Try`, `Future`, `Either`, `Product`, `Map` and other utilities in the [`utils`](core/src/main/scala/org/tupol/utils/) package.


## Prerequisites ##

* Java 8 or higher (matching the Scala version)
* Scala 2.12 or 2.13


## Getting Scala Utils ##

Scala Utils is published to Sonatype OSS and [Maven Central](https://mvnrepository.com/artifact/org.tupol/scala-utils),
where the latest artifacts can be found.

Usage with SBT, adding a dependency to the latest version of scala utils to your sbt build definition file:

```scala
  libraryDependencies += "org.tupol" %% "scala-utils-core" % "2.0.0"
```
or
```scala
  libraryDependencies += "org.tupol" %% "scala-utils-core" % "2.0.0"
```

For Snapshots, the Sonatype snapshots repo needs to be added as well:

```scala
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```


## What's new?

**2.0.0**

- Cross compilation on Scala 2.12 and Scala 2.13
- Compiled with JDK 17 targeting Java 8
- Removed the `config-z` module
- Removed `TraversableOps` as it will be deprecated in future Scala versions


## License ##

This code is open source software licensed under the [MIT License](LICENSE).
