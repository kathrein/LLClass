resolvers += Resolver.url("my-test-repo", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

test in assembly := {}

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
