# Release Notes

## 1.0.0

- Added the Collections `toOptionNel` utility
- Further refactoring of the `config-z` module


## 1.0.0-RC02

- Minor project refactoring and cleanup
- Added `Future.allOkOrFail`
 
## 1.0.0-RC01

- Major project refactoring and cleanup
- The core utilities that have no external dependencies are moved to the `core` module
- The config utilities framework is moved to the `config-z` module
- Added more utilities, for `Future`, `Either` and guarding code through `Bracket`s

## 0.2.0
  - `Configurator`s can also be used as `Extractor`s
  - Added support for complex `Map` and `Seq` configuration types
  - Added extractor for `Either` objects
  - Added extractors for time related properties: `Duration`, `Timestamp`, `Date`, `LocalDateTime` and `LocalDate`

## 0.1.0
  - `Configurator`s framework
  - `byteable` to convert various data types into array of bytes
  - `utils` with various utilities for `Map`s, `Product`s and `Try`s

