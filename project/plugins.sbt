logLevel := Level.Warn

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += Resolver.url("typesafe", url("https://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.xerial.sbt"   % "sbt-sonatype" % "3.9.12")
addSbtPlugin("com.github.sbt"   % "sbt-release"  % "1.1.0")

addSbtPlugin("org.scoverage"    % "sbt-scoverage"        % "2.0.9")
addSbtPlugin("com.eed3si9n"     % "sbt-assembly"         % "2.3.0")
addSbtPlugin("com.eed3si9n"     % "sbt-buildinfo"        % "0.10.0")
addSbtPlugin("com.github.sbt"   % "sbt-native-packager"  % "1.10.4")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"         % "2.4.3")

