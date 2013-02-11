package lazyglot

import java.io._
import io.Source
import org.squeryl.PrimitiveTypeMode._

object Build extends App {
  Dictionary.init
  Dictionary.build()
}

object Run extends App {

  Dictionary.init

  val basePath = "src/main/resources/script/m2/"

  End2End.run(
    Util.collectFilesRecursively(new File(basePath)).map(new FileInputStream(_))
  )(new FileOutputStream("cards.txt"))

}
