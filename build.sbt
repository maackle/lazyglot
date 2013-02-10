

organization := "level11"

name := "lazyglot"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"

libraryDependencies ++= Seq(
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  "org.squeryl" %% "squeryl" % "0.9.5-2",
  "com.h2database" % "h2" % "1.2.127"
)
