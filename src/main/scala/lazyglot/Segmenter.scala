package lazyglot

import org.atilika.kuromoji.{Token, Tokenizer}
import java.io._
import java.util.List
import io.Source
import javax.swing.text.Segment


object Segmenter {

  case class Occurrence(position:Int, token:Token)
  case class Segment(text:String)

  private lazy val tokenizer = Tokenizer.builder.build

  private object Tracker {
    type Occ = Occurrence
    private val Empty = collection.mutable.ListBuffer[Occ]()
    private val all = collection.mutable.Map[Segment, collection.mutable.ListBuffer[Occ]]()

    def addToken(token:Token) {
      val arr = token.getAllFeaturesArray
      if (arr.length == 9 && arr(6) != "*" && arr(0) != "記号") {
        val Array(pos, pos1, pos2, pos3, inflection, form, base, reading1, reading2) = arr
        val seg = Segment(base)
        all.getOrElseUpdate(seg, Empty) += Occurrence(token.getPosition, token)
      }
    }

    def segments = all.keys
  }

  def processFileWhole(fis:FileInputStream) {
    val text = Util.read(fis).mkString
    for( t <- Segmenter.tokenize(text) ) {
      Tracker.addToken(t)
    }
  }

  def processFileLineByLine(fis:FileInputStream) {
    var totalChars = 0
    for (line <- Util.read(fis).getLines) {
      for( t <- Segmenter.tokenize(line) ) {
        Tracker.addToken(t)
      }
      totalChars += line.length
    }
  }

  def results = Tracker.segments

  def tokenize(line: String) : Iterable[Token] = {
    import scala.collection.JavaConversions
    JavaConversions.collectionAsScalaIterable(tokenizer.tokenize(line))
  }
}
