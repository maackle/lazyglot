package superglot.nlp
import opennlp.tools.tokenize.{SimpleTokenizer, TokenizerME, TokenizerModel}
import java.io.{File, FileInputStream}
import opennlp.tools.parser.{ParserFactory, Parse, ParserModel}
import opennlp.tools.postag.{POSTaggerME, POSTagger, POSModel}

object OpenNLP {

  private val basePath = new File("data/opennlp")

  private def stream(path:String) = new FileInputStream(new File(basePath, path))

  protected object Models {
    lazy val tagger = new POSModel(stream("en-pos-maxent.bin"))
    lazy val tokenizer = new TokenizerModel(stream("en-token.bin"))
    lazy val parser = new ParserModel(stream("en-parser-chunking.bin"))
  }

  private lazy val tokenizer = new TokenizerME(Models.tokenizer)
  private lazy val parser = ParserFactory.create(Models.parser)
  private lazy val tagger = new POSTaggerME(Models.tagger)

  def tokenize(string:String) = tokenizer.tokenize(string)

  def tokenizeWithTags(string:String) = {
    val ts = tokenize(string)
    ts.zip(tagger.tag(ts))
  }

}