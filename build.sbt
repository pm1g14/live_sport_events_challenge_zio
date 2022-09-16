ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "live_sport_events_challenge_zio"
  )
libraryDependencies += "dev.zio" %% "zio" % "1.0.16"
libraryDependencies += "dev.zio" %% "zio-streams" % "1.0.16"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
libraryDependencies ++= Seq("dev.zio" %% "zio-test" % "1.0.16" % "test",
  "dev.zio" %% "zio-test-sbt" % "1.0.16" % "test"
)
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.13" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.23.4" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.16.0" % "test"
