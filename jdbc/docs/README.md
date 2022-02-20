# `jdbc` Module

## Scope

This module should hold system-wide logic that is JDBC related.

**Note**: *This module should not include concrete database dependencies.*

## Features

- `ResultSet` data extraction from columns by name or by index, strongly opinionated about `null` values
- `ResultSet` data extraction from the entire `ResultSet`


Note: Some of these features, like `JdbcFunctions` were based on existing code from various system components.

Note: Usage samples are available in the corresponding unit tests.
