addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")


// Proto Buffers
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.34")
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.7"


logLevel := Level.Warn

// Release version
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.0.15")
addSbtPlugin("org.scala-sbt" % "sbt-autoversion" % "1.0.0")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

// Docker builder requirement
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.6")
//addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.7")
//addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")


// Jacoco unittest & code coverage
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.0.3")
addSbtPlugin("com.github.mwz" % "sbt-sonar" % "2.2.0")


// plugins.sbt Workarround for jdk11
val jacocoVersion = "0.8.5"

dependencyOverrides ++= Seq(
  "org.jacoco" % "org.jacoco.core" % jacocoVersion,
  "org.jacoco" % "org.jacoco.report" % jacocoVersion,
  "org.jacoco" % "org.jacoco.agent" % jacocoVersion
)
