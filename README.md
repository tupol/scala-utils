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
- [Configuration framework](docs/configuration-framework.md)
- Convertion to byte array in the [`byteable`](src/main/scala/org/tupol/utils/byteable.scala) package.
- `Try` utilities in the [`utils`](src/main/scala/org/tupol/utils/utils.scala) package.

***Attention!*** The [Configuration framework](docs/configuration-framework.md) is deprecated, as the
[PureConfig](https://pureconfig.github.io/) framework is much more mature and provides a better
overall solution.

## Prerequisites ##

* Java 6 or higher
* Scala 2.11,or 2.12


## Getting Scala Utils ##

Scala Utils is published to Sonatype OSS and [Maven Central](https://mvnrepository.com/artifact/org.tupol/scala-utils),
where the latest artifacts can be found.

Usage with SBT, adding a dependency to the latest version of scala utils to your sbt build definition file:

```scala
libraryDependencies += "org.tupol" %% "scala-utils" % "0.2.0"
```


## Usage

Some usage examples can be found under [`src/test/scala/examples`](src/test/scala/examples).


## What's new?

**0.2.1-SNAPSHOT**
  - Deprecated the `configuration` framework in favour of `pureconfig`
  - Added `Try.mapFailure` for exception wrapping or conversion
  - Added `Traversable[Try[_]].separate` function to split failures from successes


For previous versions please consult the [release notes](RELEASE-NOTES.md).


## Credits & Thanks ##

- [Daan Hoogenboezem](https://github.com/daanhoogenboezem) For sketching the initial configuration framework.


## License ##

This code is open source software licensed under the [MIT License](LICENSE).
