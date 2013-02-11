package lazyglot

import org.squeryl.PrimitiveTypeMode._
import java.io.OutputStream

object Carder {

  import Dictionary._

  case class Card(expression:String, reading:Option[String], meaning:String, extra:String="") {

    private def escape(str:String) = {
      if (str.isEmpty) ""
      else "\"%s\"".format(str.replace("\"", "\"\""))
    }

    private def fields = Seq(expression, reading.getOrElse(expression), meaning)

    def toSSV = fields.map( text => {
      escape(text)
    }).mkString(";")
  }

  def lookup(text:String) =  {
    Dictionary.readings.where(r => r.text === text)
  }

  def lookup(text:String, `type`:ReadingType.Value) =  {
    Dictionary.readings.where(r => r.text === text and r.`type` === `type`)
  }

  def process(segments:Iterable[Segmenter.Segment], limit:Int=99999, commonOnly:Boolean=true) = transaction {

    println("processing %d total segments: " format segments.size)
    var i = 0
    val cards = for {
      (seg) <- segments.take(limit)
      reading <- lookup(seg.text)
      if !commonOnly || reading.common_?
      entry <- reading.entries
    } yield {
      Util.printProgress(i)(10,100)
      i += 1
      val (kanji, kana) = entry.readings.partition(_.`type` == ReadingType.Kanji)
      val topKanji = kanji.filter(_.common_?)
      val topKana = kana.filter(_.common_?)
      val expression = {
        val rs = {
          if (kanji.isEmpty)
            kana.take(1)
          else if (topKanji.isEmpty)
            kanji.take(1)
          else
            topKanji
        }
        rs.map(_.text).mkString("\n")
      }
      val cardReading = {
        val rs = {
          if (kanji.isEmpty)
            kana.take(0)
          else if (topKana.isEmpty)
            kana.take(1)
          else
            topKana
        }
        if(rs.isEmpty) None
        else Some(rs.map(_.text).mkString("\n"))
      }
      Card(
        expression = expression,
        reading = cardReading,
        meaning = {
          entry.senses.map( s => s.glosses.replace("|", " • ")).mkString("\n")
        },
        extra = reading.priority
      )
    }
    println("\nDONE (%d total cards)" format cards.size)
    cards
  }

  def write(cards:Iterable[Card], os:OutputStream) = {
    Util.write(os)(cards.map(_.toSSV).view.mkString("\n"))
  }

}
