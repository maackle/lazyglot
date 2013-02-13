

organization := "level11"

name := "superglot"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers ++= Seq(
  "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika",
  "OpenNLP Repository" at "http://opennlp.sourceforge.net/maven2"
)

libraryDependencies ++= Seq(
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  "org.squeryl" % "squeryl_2.9.2" % "0.9.5-2",
  "edu.stanford.nlp" % "stanford-corenlp" % "1.3.4",
  "org.apache.opennlp" % "opennlp-tools" % "1.5.2-incubating",
//  "opennlp" % "opennlp-tools" % "1.4.3",
  "com.h2database" % "h2" % "1.2.127"
)
