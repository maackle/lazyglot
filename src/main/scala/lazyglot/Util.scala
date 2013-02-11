package lazyglot

import java.io._
import io.{BufferedSource, Codec, Source}
import java.nio.charset.{CodingErrorAction, Charset, CharsetDecoder}

object Util {

  def collectFilesRecursively(base:File): Array[File] = {
    val fs = base.listFiles()
    fs ++ fs.filter(_.isDirectory).flatMap(collectFilesRecursively)
  }

  def write(name:String)(what:String) { write(new File(name))(what) }
  def write(file:File)(what:String) {
    val pw = new PrintWriter(file, "UTF-8")
    pw.println(what)
    pw.close()
  }
  def write(os:OutputStream)(what:String) {
    val pw = new PrintWriter(os)
    pw.println(what)
    pw.close()
  }

  def read(path:String):BufferedSource = read(new File(path))
  def read(file:File):BufferedSource = read(new FileInputStream(file))
  def read(fis:FileInputStream) = {
    val codec = Charset.forName("UTF-8").newDecoder.onMalformedInput(CodingErrorAction.REPLACE)
    Source.fromInputStream(fis)(codec)
  }

  def printProgress(i:Int)(a:Int, b:Int) {
    if (i % b == 0) print("\n" + i)
    else if (i % a == 0) print(" .")
  }

}
