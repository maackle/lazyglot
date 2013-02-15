package superglot.cruncher

import superglot.util.{CommonMixins, PathHelper}
import java.io._
import xml.XML
import java.util.logging.{Level, Logger}
import superglot.cruncher.DB.{Reading, Meaning}
import java.util.Locale

object Wiktionary extends CommonMixins {

  val basePath = "data/wiktionary"

  val xmlJA = stream("jawiktionary-20130202-pages-articles-multistream.xml")

  object English {

//    val xmlEN = stream("enwiktionary-20130202-pages-articles-multistream.xml")
    val tsvEN = stream("enwikt-defs-latest-all.tsv")

    val lineMap = collection.mutable.Map[String, collection.mutable.ArrayBuffer[String]]()


    object toSpanish {

      superglot.util.initH2("en-es")

      def stats() = transaction {

        println("meanings: %s" format from(DB.meanings)(select(_)).size)
        println("readings: %s" format from(DB.readings)(select(_)).size)
      }

      def test() = transaction {
        val words =
          """
            |Hijo de Gabriel Eligio García y de Luisa Santiaga Márquez Iguarán, Gabriel García Márquez nació en Aracataca, en el departamento del Magdalena, Colombia, «el domingo 6 de marzo de 1927 a las nueve de la mañana...», como refiere el propio escritor en sus memorias.3
            |Cuando sus padres se enamoraron, el padre de Luisa, coronel Nicolás Ricardo Márquez Mejía, se opuso a esa relación pues Gabriel Eligio García, que había llegado a Aracataca como telegrafista, no era el hombre que consideraba más adecuado para su hija, por ser hijo de madre soltera, pertenecer al Partido Conservador Colombiano y ser un mujeriego confeso.3 Con la intención de separarlos, Luisa fue enviada fuera de la ciudad, pero Gabriel Eligio la cortejó con serenatas de violín, poemas de amor, innumerables cartas y frecuentes mensajes telegráficos. Finalmente la familia capituló y Luisa consiguió el permiso para casarse con Gabriel Eligio, lo cual sucedió el 11 de junio de 1926 en Santa Marta. La historia y tragicomedia de ese cortejo inspiraría más tarde a su hijo la novela El amor en los tiempos del cólera.3
            |Poco después del nacimiento de Gabriel, su padre se convirtió en farmacéutico y, en enero de 1929, se mudó con Luisa a Barranquilla, dejando a Gabriel en Aracataca al cuidado de sus abuelos maternos. Dado que vivió con ellos durante los primeros años de su vida, recibió una fuerte influencia del coronel Márquez, quien de joven mató a un hombre en un duelo y tuvo, además de los tres hijos oficiales, otros nueve con distintas madres. El Coronel era un liberal veterano de la Guerra de los Mil Días, muy respetado por sus copartidarios y conocido por su negativa a callar sobre la Masacre de las bananeras, suceso en el que murieron cientos de personas a manos de las Fuerzas Armadas de Colombia durante una huelga de los trabajadores de las bananeras, hecho que García Márquez plasmaría en su obra.3
            |El coronel, a quien Gabriel llamaba "Papalelo", describiéndolo como su «cordón umbilical con la historia y la realidad», fue también un excelente narrador y le enseñó, por ejemplo, a consultar frecuentemente el diccionario, lo llevaba al circo cada año y fue el primero en introducir a su nieto en el «milagro» del hielo, que se encontraba en la tienda de la United Fruit Company.3 Frecuentemente decía: «Tú no sabes lo que pesa un muerto», refiriéndose así a que no había mayor carga que la de haber matado a un hombre, lección que García Márquez más tarde incorporaría en sus novelas.3 2 10
            |Su abuela, Tranquilina Iguarán Cotes, a quien García Márquez llama la abuela Mina y describe como "una mujer imaginativa y supersticiosa"2 que llenaba la casa con historias de fantasmas, premoniciones, augurios y signos, fue de tanta influencia en GGM como su marido e incluso es señalada por el escritor como su primera y principal influencia literaria pues le inspiró la original forma en que ella trataba lo extraordinario como algo perfectamente natural cuando contaba historias y como sin importar cuán fantásticos o improbables fueran sus relatos, siempre los refería como si fueran una verdad irrefutable. Además del estilo, la abuela Mina inspiró también el personaje de Ursula Iguarán que, unos treinta años más tarde, su nieto usaría en Cien años de soledad, su novela más popular.3 11
            |Su abuelo murió en 1936, cuando Gabriel tenía ocho años. Debido a la ceguera de su abuela él fue a vivir con sus padres en Sucre, población ubicada en el departamento de Sucre (Colombia), donde su padre trabajaba como farmacéutico.
            |Su niñez está relatada en sus memorias Vivir para contarla.3 Después de 24  años de ausencia, en 2007 regresó a Aracataca para un homenaje que le rindió el gobierno colombiano al cumplir sus 80 años de vida y los 40 desde la primera publicación de Cien años de soledad.
          """.stripMargin.split(" ").map(_.replaceAll("""[.,]""", ""))

        for(word <- words) {
          val entry = DB.readings.where(_.text like word).headOption.map(_.meaning.head)
          if(entry.isEmpty)
            print("*** ")
          println("%s = %s".format(word, entry))
        }
      }

      def makeDB() = transaction {

        DB.drop
        DB.create

        val r_crossref = """^#\s+\{\{([^|]+\|)+(\w+)}}$""".r
        //      val r_entry = """^# $""".r

        var i = 0
        for ((line, i) <- io.Source.fromInputStream(stream("en/Spanish.tsv"), "UTF-8").getLines.zipWithIndex) {
          superglot.util.printProgress(i)(1000,10000)
          val Array(_, term, form, entry) = line.split("\t")
          entry match {
            case r_crossref(_, ref) =>
            case _ =>
              //            val text = entry.replaceAll("""\{\{|}}|\[\[|]]""", "")
              val text = entry
              val meaning = DB.meanings.insert( Meaning(text, form) )
              val reading = DB.readings.insert( Reading(term, meaning.id) )
          }
        }

        for ((line, i) <- io.Source.fromInputStream(stream("en/Spanish.tsv"), "UTF-8").getLines.zipWithIndex) {
          superglot.util.printProgress(i)(1000,10000)
          val Array(_, term, form, entry) = line.split("\t")
          entry match {
            case r_crossref(_, ref) =>
              DB.readings.where(r => r.text === ref).headOption map { existing =>
                DB.readings.insert( Reading(term, existing.meaning_id) )
              }
            case _ =>
          }
        }
      }
    }



    private def __categorize() {
      for (line <- io.Source.fromInputStream(tsvEN, "UTF-8").getLines) {
        val Array(lang, term, form, entry) = line.split("\t")
        lineMap.get(lang) match {
          case Some(ab) => ab += line
          case None => lineMap(lang) = collection.mutable.ArrayBuffer[String](line)
        }
      }

      for (lang <- lineMap.keys) {
        val f = new File(new File(basePath), "en/%s.tsv".format(lang))
        val pw = new PrintWriter(f)
        lineMap(lang).foreach(pw.println)
        pw.close()

      }
    }

  }

}
