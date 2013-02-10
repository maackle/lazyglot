package lazyglot

import io.Source
import xml.XML
import java.io.{PrintWriter, File}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl
import squeryl.adapters.H2Adapter
import squeryl.Session

object Carder {

  import Dictionary._

  case class Card(expression:String, reading:String, meaning:String, extra:String="") {
    def escape(str:String) = "\"%s\"".format(str)
    def toCSV = Seq(expression, reading, escape(meaning), extra).map( text => {
      text
    }).mkString(",")
  }

  def lookup(text:String) =  {
    Dictionary.readings.where(r => r.text === text)
  }

  def lookup(text:String, `type`:ReadingType.Value) =  {
    Dictionary.readings.where(r => r.text === text and r.`type` === `type`)
  }

  def process(segments:Iterable[Segmenter.Segment]) = transaction {
    println("processing %d total segments: " format segments.size)
    var i = 0
    val cards = for {
      (seg) <- segments.take(10)
      reading <- lookup(seg.base)
      entry <- reading.entries
    } yield {
      Util.printProgress(i)(10,100)
      i += 1
      Card(
        expression = reading.text,
        reading = reading.text,
        meaning = {
          entry.senses.map( s => s.glosses.replace("|", " | ")).mkString("\n")
        },
        extra = reading.priority
      )
    }
    println("\nDONE (%d total cards)" format cards.size)
    cards
  }

  def write(cards:Iterable[Card], path:String) = {
    Util.write(path)(cards.map(_.toCSV))
  }

}
