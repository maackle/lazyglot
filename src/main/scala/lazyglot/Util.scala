package lazyglot

import java.io.{InputStreamReader, Reader, InputStream, PrintWriter}
import io.{Codec, Source}
import java.nio.charset.{CodingErrorAction, Charset, CharsetDecoder}

object Util {
  def dump(name:String)(what:String) {
    val pw = new PrintWriter(name, "UTF-8")
    pw.println(what)
    pw.close()
  }

  def write(name:String)(what:Iterable[String]) {
    val pw = new PrintWriter(name, "UTF-8")
    what.foreach(pw.println)
    pw.close()
  }

  def read(path:String) = {
    val fis = new java.io.FileInputStream(path)
    val codec = Charset.forName("UTF-8").newDecoder.onMalformedInput(CodingErrorAction.REPLACE)
    Source.fromInputStream(fis)(codec)
  }

  def printProgress(i:Int)(a:Int, b:Int) {
    if (i % b == 0) print("\n" + i)
    else if (i % a == 0) print(" .")
  }

  def alksdjflajksdglk {

    val in: InputStream = null
    val decoder: CharsetDecoder = Charset.forName("UTF-8").newDecoder
    decoder.onMalformedInput(CodingErrorAction.IGNORE)
    val reader: Reader = new InputStreamReader(in, decoder)
  }
}
