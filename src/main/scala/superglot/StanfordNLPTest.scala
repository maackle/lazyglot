package superglot

import edu.stanford.nlp.process.DocumentPreprocessor
import java.io.StringReader

object StanfordNLPTest extends App {

  val text =
    """
      | "My life is very monotonous," the fox said. "I hunt chickens; men hunt me. All the chickens are just alike, and all the men are just alike. And, in consequence, I am a little bored. But if you tame me, it will be as if the sun came to shine on my life. I shall know the sound of a step that will be different from all the others. Other steps send me hurrying back underneath the ground. Yours will call me, like music, out of my burrow. And then look: you see the grain-fields down yonder? I do not eat bread. Wheat is of no use to me. The wheat fields have nothing to say to me. And that is sad. But you have hair that is the colour of gold. Think how wonderful that will be when you have tamed me! The grain, which is also golden, will bring me back the thought of you. And I shall love to listen to the wind in the wheat..."
      |
      |The fox gazed at the little prince, for a long time.
      |
      |"Please-- tame me!" he said.
      |
      |"I want to, very much," the little prince replied. "But I have not much time. I have friends to discover, and a great many things to understand."
      |
      |"One only understands the things that one tames," said the fox. "Men have no more time to understand anything. They buy things all ready made at the shops. But there is no shop anywhere where one can buy friendship, and so men have no friends any more. If you want a friend, tame me..."
      |
      |"What must I do, to tame you?" asked the little prince.
      |
      |"You must be very patient," replied the fox. "First you will sit down at a little distance from me-- like that-- in the grass. I shall look at you out of the corner of my eye, and you will say nothing. Words are the source of misunderstandings. But you will sit a little closer to me, every day..."
    """.stripMargin

  def test() {
    import scala.collection.JavaConversions._
    println(text)
    val reader = new StringReader(text)
    val dp = new DocumentPreprocessor(reader)

    val tokens = dp.filter(s => !s.isEmpty)
    for (line <- dp; s <- line) println(s)
  }

//  test()
}

