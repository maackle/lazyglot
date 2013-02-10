package lazyglot

import java.io.{File, BufferedReader, IOException, InputStreamReader}
import io.Source
import org.squeryl.PrimitiveTypeMode._

object Run extends App {

  Dictionary.init()

//  Dictionary.build()

//  val res = Segmenter.processFile(new File("sentences.txt"))
  val path = "src/main/resources/script/m3/asdf.html"
  val res = Segmenter.processFile(path)
  val cards = Carder.process(res.all.keys)
  Carder.write(cards, "cards.txt")

  def lskjdf {
    transaction {

      for (line <- Source.fromFile("lookups.txt").getLines) {
        {
          for(
            r <- Carder.lookup(line)
          ) {
            println(r.text, r.`type`)
            for( e <- r.entries ) {
              println( e.readings.mkString(" | ") )
              for( s <- e.senses ) {
                println(s.glosses)
              }
              println("- - -")
            }
          }
        }
      }
    }
  }

}
