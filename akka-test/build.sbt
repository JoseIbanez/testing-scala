
name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.13.1"

val AkkaActorVersion = "2.6.13"
val AlpakkaVersion   = "2.0.2"
val AkkaHttpVersion  = "10.2.4"
val Json4sVersion    = "3.6.6"
val JacksonVersion   = "2.10.5.1"
val squbsVersion     = "0.14.0"
val heikoseebergerAkkaHttpJsonVersion = "1.35.3"

libraryDependencies ++= Seq(
  "org.squbs"         %% "squbs-ext"                % squbsVersion,
  "com.typesafe"      % "config"                    % "1.3.3",
  "com.typesafe.akka" %% "akka-actor"               % AkkaActorVersion,
  "com.typesafe.akka" %% "akka-actor-typed"         % AkkaActorVersion,
  "io.spray"          %% "spray-json"               % "1.3.6",
  "com.typesafe.akka" %% "akka-stream"              % AkkaActorVersion,
  "com.typesafe.akka" %% "akka-http"                % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream-kafka"        % "2.0.6",
  "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion,
  "org.json4s"        %% "json4s-jackson"           % Json4sVersion,
  "org.json4s"        %% "json4s-ext"               % Json4sVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"     % AkkaHttpVersion,
//  "com.typesafe.akka" %% "akka-http-json4s"         % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-jackson"        % AkkaHttpVersion,
  "org.scalatest"     %% "scalatest"                % "3.2.5" % Test,
  "junit"             %  "junit"                    % "4.13.2" % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaActorVersion % Test,
  "io.github.embeddedkafka" %% "embedded-kafka"     % "2.4.1.1" % Test,
  "org.testcontainers" % "kafka"                    % "1.15.2"
)
//  "de.heikoseeberger" %% "akka-http-json4s"         % heikoseebergerAkkaHttpJsonVersion,
//  "de.heikoseeberger" %% "akka-http-jackson"        % heikoseebergerAkkaHttpJsonVersion,


// Proto Buf
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.7"


resolvers ++= Seq(Resolver.mavenLocal)

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)
