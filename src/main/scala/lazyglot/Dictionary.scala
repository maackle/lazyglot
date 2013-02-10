package lazyglot

import org.squeryl.PrimitiveTypeMode._
import org.squeryl
import squeryl.adapters.H2Adapter
import squeryl.{KeyedEntity, Session}
import java.io.File
import xml.XML

object Dictionary extends squeryl.Schema {

  private var initialized_? = false

  trait IdPK extends KeyedEntity[Long] {
    var id:Long = 0
  }

  case class Entry(
                  id: Long
                  ) extends KeyedEntity[Long] {

    def senses = entryToSenses.left(this)
    def readings = readingsToEntries.right(this)
  }

  case class Reading(
                  text:String,
                  `type`: ReadingType.Value,
                  entry_id: Long,
                  priority: String = ""
                          ) extends IdPK {
    def this() = this("",ReadingType.Invalid, 0)
    def entries = readingsToEntries.left(this)
  }

  case class Sense(
                  glosses:String,
                  pos:String,
                  entry_id:Long
                  ) extends IdPK {

  }

  case class ReadingEntry(reading_id:Long, entry_id:Long) extends IdPK

  object ReadingType extends Enumeration {
    val Invalid = Value(0, "invalid")
    val Kana = Value(1, "kana")
    val Kanji = Value(2, "kanji")
  }

  val entries = table[Entry]
  val senses = table[Sense]
  val readings = table[Reading]

  val entryToSenses = oneToManyRelation(entries, senses)
    .via((e,s) => e.id === s.entry_id)

  val readingsToEntries = manyToManyRelation(readings, entries)
    .via[ReadingEntry]((r,e,re) => (r.id === re.reading_id, re.entry_id === e.id))

  on(entries)( e => declare (
    e.id is(primaryKey)
  ))

  on(readings)( e => declare (
    e.id is(autoIncremented)
  ))

  on(senses)( s => declare (
    s.glosses is(dbType("TEXT")),
    s.id is(autoIncremented)
  ))

  def build(filename:String = "src/main/resources/dict/JMdict_e") {
    init()
    System.setProperty("entityExpansionLimit", "250000")
    val xml = XML.load(new java.io.InputStreamReader(new java.io.FileInputStream(filename), "UTF-8"))
    val entries = xml \ "entry"
    transaction {
      try {
        Dictionary.drop
      } catch { case _ => }
      Dictionary.create
    }
    transaction {
      for ((e,i) <- entries.view.zipWithIndex) {
        if(i % 1000 == 0) print("\n" + i)
        else if(i % 100 == 0) print(".")
        val entry = Entry(
          id = (e \ "ent_seq").text.toLong
        )
        Dictionary.entries.insert(entry)

        for( rs <- e \ "r_ele") {
          val reading = Dictionary.readings.insert(Reading(
            text = (rs \ "reb").text,
            `type` = ReadingType.Kana,
            entry_id = entry.id,
            priority = (rs \ "re_pri").map(_.text).mkString("|")
          ))
          entry.readings.associate(reading)
        }

        for( rs <- e \ "k_ele") {
          val reading = Dictionary.readings.insert(Reading(
            text = (rs \ "keb").text,
            `type` = ReadingType.Kanji,
            entry_id = entry.id,
            priority = (rs \ "ke_pri").map(_.text).mkString("|")
          ))
          entry.readings.associate(reading)
        }

        for {
          sense <- e \ "sense"
          glosses = (sense \ "gloss").map(_.text).mkString("|")
        } {
          val s = Dictionary.senses.insert(Sense(
            glosses = glosses,
            pos = "",
            entry_id = entry.id
          ))
          entry.senses.associate(s)
        }
      }
    }


  }

  def init() {
    import org.squeryl.SessionFactory

    if (initialized_?) return
    initialized_? = true

    Class.forName("org.h2.Driver");

    SessionFactory.concreteFactory = Some(()=>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:h2:dbdict"),
        new H2Adapter)
    )
  }
}