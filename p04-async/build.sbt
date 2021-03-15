name := "p04-async"

version := "0.1"

scalaVersion := "2.13.4"

scalacOptions += "-Xasync"

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.10.0"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided

