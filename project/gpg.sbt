resolvers += Resolver.url("scalasbt-plugin-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")

addSbtPlugin("org.scoverage"    % "sbt-scoverage"        % "1.9.1")
addSbtPlugin("com.eed3si9n"     % "sbt-buildinfo"        % "0.10.0")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"         % "2.4.3")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
