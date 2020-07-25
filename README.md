# Scala Utils

[![Maven Central](https://img.shields.io/maven-central/v/org.tupol/scala-utils_2.11.svg)](https://mvnrepository.com/artifact/org.tupol/scala-utils) &nbsp;
[![GitHub](https://img.shields.io/github/license/tupol/scala-utils.svg)](https://github.com/tupol/scala-utils/blob/master/LICENSE) &nbsp; 
[![Travis (.org)](https://img.shields.io/travis/tupol/scala-utils.svg)](https://travis-ci.com/tupol/scala-utils) &nbsp; 
[![Codecov](https://img.shields.io/codecov/c/github/tupol/scala-utils.svg)](https://codecov.io/gh/tupol/scala-utils) &nbsp;
[![Javadocs](https://www.javadoc.io/badge/org.tupol/scala-utils_2.11.svg)](https://www.javadoc.io/doc/org.tupol/scala-utils_2.11)
[![Gitter](https://badges.gitter.im/scala-utils/community.svg)](https://gitter.im/scala-utils/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) &nbsp; 
[![Twitter](https://img.shields.io/twitter/url/https/_tupol.svg?color=%2317A2F2)](https://twitter.com/_tupol) &nbsp; 

## Description

This project contains some basic utilities that can help setting up a Scala project.

The main utilities available:
- [Configuration framework](config-z/docs/configuration-framework.md)
- Conversion to byte array in the [`byteable`](core/src/main/scala/org/tupol/utils/ByteableOps.scala) package.
- `Try`, `Future`, `Either`, `Product`, `Map` and other utilities in the [`utils`](core/src/main/scala/org/tupol/utils/) package.

***Attention!*** The [Configuration framework](config-z/docs/configuration-framework.md) might be
 deprecated, as the [PureConfig](https://pureconfig.github.io/) framework is much more mature and
  provides a better overall solution.

## Prerequisites ##

* Java 8 or higher (matching the Scala version)
* Scala 2.12


## Getting Scala Utils ##

Scala Utils is published to Sonatype OSS and [Maven Central](https://mvnrepository.com/artifact/org.tupol/scala-utils),
where the latest artifacts can be found.

Usage with SBT, adding a dependency to the latest version of scala utils to your sbt build definition file:

```scala
  libraryDependencies += "org.tupol" %% "scala-utils" % "1.0.0-RC01-SNAPSHOT"
```

The Sonatype snapshots repo needs to be added as well:

```scala
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```


## Usage

Some `config-z` usage examples can be found under [`config-z/src/test/scala/examples`](config-z/src/test/scala/examples).


## What's new?

**1.0.0-RC01** Effort Started

This new major version aims to bring a new and hopefully cleaner project structure.
The `scalaz` based configuration is moved to a different module to isolate better from the core.
In the future a `cats` based version will be added as well.

More core utilities were added and the old ones were brushed up for better consistency and clarity.

Attention! This version is no longer cross compiling across Scala 2.11 and 2.12.
Only Scala 2.12 is supported at the moment.

The previous versions are still available and can evolve independently on the [`0.2.x`](https://github.com/tupol/scala-utils/tree/0.2.x) branch.



## License ##

This code is open source software licensed under the [MIT License](LICENSE).
