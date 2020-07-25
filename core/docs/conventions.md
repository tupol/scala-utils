# Conventions


## Implicit and Explicit Behaviors

We should ***contain*** implicit additional behavior for types and use a convention for creating them.

**`object`s that have a name suffixed by `Ops` contain implicit behavior.**

For example, the `ResultSetOps.scala` should contain a top level object called `ResultSetOps` which
adds implicit behavior to the `ResultSet` data type.

In case we want to expose non-implicit behavior for a type, we can add the utility functions
to a different object than the one containing the implicit behavior.

**`object`s that have a name suffixed by `Utils` contain explicit behavior mirroring the `Ops` one.**

For example, the `ResultSetUtils.scala` should contain a top level object called `ResultSetUtils`
 which adds explicit behavior to the `ResultSet` data type.

For a given type we can have both `Ops` and `Utils`, just one of them or none.

If needed, package wide implicits should be collected in an `implicits.scala` file containing
an `implicits` object.

The `implicits` object can include implicit values that extend implicits classes from the `Ops`
objects.

By no means the use of implicits should be abused! Each additional implicit behavior should be
 carefully reviewed.
