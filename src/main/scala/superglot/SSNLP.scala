package superglot

import java.util.Properties
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.trees.{TreeCoreAnnotations, Tree}
import edu.stanford.nlp.trees.semgraph.{SemanticGraphCoreAnnotations, SemanticGraph}
import edu.stanford.nlp.dcoref.{CorefChain, CorefCoreAnnotations}
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.util.CoreMap

//object SSNLP {
//  def test {
//    import scala.collection.JavaConversions._
//    val props: Properties = new Properties
//    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref")
//    val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)
//    val text: String = ""
//    val document: Annotation = new Annotation(text)
//    pipeline.annotate(document)
//    val sentences: List[CoreMap] = document.get(classOf[CoreAnnotations.SentencesAnnotation])
//    for (sentence <- sentences) {
//      for (token <- sentence.get(classOf[CoreAnnotations.TokensAnnotation])) {
////        val word: String = token.get(classOf[CoreAnnotations.TextAnnotation])
////        val pos: String = token.get(classOf[CoreAnnotations.PartOfSpeechAnnotation])
////        val ne: String = token.get(classOf[CoreAnnotations.NamedEntityTagAnnotation])
//      }
//      val tree: Tree = sentence.get(classOf[TreeCoreAnnotations.TreeAnnotation])
//      val dependencies: SemanticGraph = sentence.get(classOf[SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation])
//    }
//    val graph: Map[Integer, CorefChain] = document.get(classOf[CorefCoreAnnotations.CorefChainAnnotation])
//  }
//}
