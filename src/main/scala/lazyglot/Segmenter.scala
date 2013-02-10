package lazyglot

import org.atilika.kuromoji.{Token, Tokenizer}
import java.io.{File, InputStream, InputStreamReader, BufferedReader}
import java.util.List
import io.Source

class FreqMap[A] {
  private val m = collection.mutable.Map[A, Int]()

  def +=(a:A) = { m.getOrElseUpdate(a, 0); m(a) += 1 }

  def all = m
}

object Segmenter {

  lazy val tokenizer = Tokenizer.builder.build

  case class Segment(base:String, pos:String)

  private val segments = new FreqMap[Segment]

  def processFile(path:String) = {
    for (line <- Util.read(path).getLines) {
      for( t <- Segmenter.tokenize(line) ) {
        val arr = t.getAllFeaturesArray
        if (arr.length == 9 && arr(6) != "*" && arr(0) != "記号") {
          val Array(pos, pos1, pos2, pos3, inflection, form, base, reading1, reading2) = arr
          segments += Segment(base, pos)
        }
      }
    }
    segments
  }

  def results = segments

  def tokenize(line: String) : Iterable[Token] = {
    import scala.collection.JavaConversions
    JavaConversions.collectionAsScalaIterable(tokenizer.tokenize(line))

  }
}
