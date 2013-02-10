
import sbt._
import Keys._

object LazyglotBuild extends Build {
  val hwsettings = Defaults.defaultSettings ++ Seq(

  )

  lazy val project = Project (
    "project",
    file ("."),
    settings = hwsettings ++ Seq()
  )
}