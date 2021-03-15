name := """rest-api"""
organization := "com.baeldung"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.1"
libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
libraryDependencies += "org.apache.commons" % "commons-dbcp2" % "2.0.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.baeldung.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.baeldung.binders._"
