name := """test-cicd"""
organization := "test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(DockerPlugin)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "test.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "test.binders._"

dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:11"
packageName := "ibanez/test-cicd"
maintainer := "ucc-unitiy@vf.com"

//From: https://github.com/sbt/sbt-native-packager/issues/1361

javaOptions in Universal ++= Seq(
  // don't write any pid files
  "-Dpidfile.path=/dev/null",
  // reference a logback config file that has no file appenders
  "-Dlogback.configurationFile=logback-prod.xml"
)


