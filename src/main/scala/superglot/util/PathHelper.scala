package superglot.util

import java.io.{FileInputStream, File}

trait PathHelper {

  val basePath:String

//  private def baseFile = new File(basePath)

  def stream(path:String) = {
    val file = new File(basePath, path)
    new FileInputStream(file)
  }
}

trait Logging {
  val log = java.util.logging.Logger.getLogger(this.getClass.toString)
  log.setLevel(java.util.logging.Level.INFO)
}

trait CommonMixins extends PathHelper with Logging with org.squeryl.PrimitiveTypeMode