import sbt.Keys._

name := """akka-test"""

fork in Test := true

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:reflectiveCalls",
  "-language:postfixOps",
  "-language:implicitConversions"
)

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.7",
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.17",
  "org.scalatest" %% "scalatest" % "2.2.5" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.4.1" % Test
)
