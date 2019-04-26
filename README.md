# Scala Utils

[![Join the chat at https://gitter.im/scala-utils/community](https://badges.gitter.im/scala-utils/community.svg)](https://gitter.im/scala-utils/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Description

This project contains some basic utilities that can help setting up a Scala project.

The main utilities available:
- [Configuration framework](docs/configuration-framework.md)
- Other utilities available in the [`byteable`](src/main/scala/org/tupol/utils/byteable.scala) and
[`utils`](src/main/scala/org/tupol/utils/utils.scala) packages


## Prerequisites ##

* Java 6 or higher
* Scala 2.11,or 2.12


## Getting Scala Utils ##

Scala Utils is published to Sonatype OSS and [Maven Central](https://mvnrepository.com/artifact/org.tupol/scala-utils),
where the latest artifacts can be found.


## Usage

Some usage examples can be found under [`src/test/scala/examples`](src/test/scala/examples).


## What's new?

**0.2.0**
  - `Configurator`s can also be used as `Extractor`s
  - Added support for complex `Map` and `Seq` configuration types
  - Added extractor for `Either` objects
  - Added extractors for time related properties: `Duration`, `Timestamp`, `Date`, `LocalDateTime` and `LocalDate`

**0.1.0**
  - [`Configurator`](docs/configuration-framework.md)s framework
  - [`byteable`](src/main/scala/org/tupol/utils/byteable.scala) to convert various data types into array of bytes
  - [`utils`](src/main/scala/org/tupol/utils/utils.scala) with various utilities for `Map`s, `Product`s and `Try`s


## Credits & Thanks ##

- [Daan Hoogenboezem](https://github.com/daanhoogenboezem) For sketching the initial configuration framework.


## License ##

This code is open source software licensed under the [MIT License](LICENSE).
