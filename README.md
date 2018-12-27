# scala-utils

## Description

This project contains some basic utilities that can help setting up a Scala project.

The main utilities available:
- [Configuration framework](docs/configuration-framework.md)
- Other utilities available in the `byteable` and `utils` packages


## Prerequisites ##

* Java 6 or higher
* Scala 2.11,or 2.12


## Getting Scala Utils ##

Scala Utils is published to Sonatype OSS and Maven Central:

- Group id / organization: `org.tupol`
- Artifact id / name: `scala-utils`
- Latest version is `0.2.0-SNAPSHOT`

Usage with SBT, adding a dependency to the latest version of Scala Logging to your sbt build definition file:

```scala
libraryDependencies += "org.tupol" %% "scala-utils" % "0.2.0-SNAPSHOT"
```


## Usage

Some usage examples can be found under [`src/test/scala/examples`](src/test/scala/examples).


## What's new?

### 0.2.0-SNAPSHOT
 - `Configurator`s can also be used as `Extractor`s
 - Added support for complex `Map` and `Seq` configuration types
 - Added extractor for `Either` objects
 - Added extractors for time related properties: `Duration`, `Timestamp`, `Date`, `LocalDateTime` and `LocalDate`


## License ##

This code is open source software licensed under the [MIT License](LICENSE).
