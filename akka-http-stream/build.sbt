/*
  General project attributes
 */
organization := "com.vodafone.ucc.middleware"
name := "akka-http-stream"
//version := "0.1"
//maintainer := "Jose Ibanez <jose.ibanez@vodafone.com>"
description := "Register a customer account in RingCentral. Kafka consumer, save events, check all info, send rest query to RC."
organizationHomepage := Some(url("https://www.vodafone.com"))

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .enablePlugins(GitVersioning)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
  )



// set the main class for jar
val myMainClass = "com.vodafone.ucc.middleware.accountcreation.AccountKafkaConsumer"
mainClass in (Compile, packageBin) := Some(myMainClass)
mainClass in (Compile, run) := Some(myMainClass)
Compile / mainClass := Some(myMainClass)

scalaVersion := "2.13.1"

lazy val akkaVersion      = "2.6.13"
lazy val AkkaHttpVersion  = "10.2.4"
lazy val AkkaHttpSerializer = "1.34.0"
lazy val JacksonVersion   = "2.10.5.1"


libraryDependencies ++= Seq(

  // akka streams + kafka
  "com.typesafe.akka" %% "akka-actor-typed"          % akkaVersion,
  "com.typesafe.akka" %% "akka-actor"                % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed"  % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream"               % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka"         % "2.0.6",

  // akka http + serializers
  "com.typesafe.akka" %% "akka-http"                 % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"      % AkkaHttpVersion,

  // jackson serilizer
  "com.typesafe.akka" %% "akka-http-jackson"        % AkkaHttpVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.5",

  // play (un)marshalling for akka http
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.35.3",

  // logging
  "ch.qos.logback"    %  "logback-classic"           % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"    % "3.9.2",

  // unittest
  "org.scalatest"     %% "scalatest"                 % "3.1.0" % Test,

  // redis
  "com.github.etaty"  %% "rediscala"                 % "1.9.0",

  // protobuffers
  "com.thesamet.scalapb" %% "scalapb-json4s"         % "0.10.1"
)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

// TODO jacoco: code coverage
val jacocoVersion = "0.8.5"

// jacoco vs jdk11
dependencyOverrides ++= Seq(
  "org.jacoco" % "org.jacoco.core" % jacocoVersion % Test,
  "org.jacoco" % "org.jacoco.report" % jacocoVersion % Test,
  "org.jacoco" % "org.jacoco.agent" % jacocoVersion % Test)

jacocoReportSettings := JacocoReportSettings(
  "Jacoco Coverage Report",
  None,
  JacocoThresholds(
    instruction = 40,
    method = 40,
    branch = 1,
    complexity = 10,
    line = 40,
    clazz = 50),
  Seq(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML),
  "utf-8")



//TODO Dockerfile settings
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:11"
packageName := "ibanez/ucc-account-creation"
maintainer := "ucc-unitiy@vodafone.com"

skip in publish := true

