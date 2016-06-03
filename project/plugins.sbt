resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.8")
addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.15")

libraryDependencies += "com.github.os72" % "protoc-jar" % "3.0.0-b1"
