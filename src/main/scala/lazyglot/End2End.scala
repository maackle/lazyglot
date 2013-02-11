package lazyglot

import java.io.{FileOutputStream, FileInputStream}

object End2End {

  def run(files:Iterable[java.io.FileInputStream])(output:java.io.OutputStream) {

    /* SEGMENT */
    files.foreach(Segmenter.processFileWhole)
    val segments = Segmenter.results

    /* LOOKUP */
    val cards = Carder.process(segments)

    /* OUTPUT */
    Carder.write(cards, new FileOutputStream("cards.txt"))
  }

}
