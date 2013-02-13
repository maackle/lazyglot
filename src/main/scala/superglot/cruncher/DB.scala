package superglot.cruncher

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._

object DB extends org.squeryl.Schema {

  trait IdPK extends KeyedEntity[Long] {
    var id:Long = 0
  }

  case class Reading(text:String, meaning_id:Long) extends IdPK {
    def meaning = meaningToReadings.right(this)
  }

  case class Meaning(text:String, form:String) extends IdPK {
    def readings = meaningToReadings.left(this)
  }

//  case class ReadingMeaning(reading_id:Long, meaning_id:Long) extends IdPK

  val meanings = table[Meaning]
  val readings = table[Reading]

  val meaningToReadings = oneToManyRelation(meanings, readings)
    .via(_.id === _.meaning_id)
//  val readingsToEntries = manyToManyRelation(readings, meanings)
//    .via[ReadingMeaning]((r,m,rm) => (r.id === rm.reading_id, rm.meaning_id === m.id))

  on(readings)( t => declare (
    t.id is(primaryKey, autoIncremented),
    t.text is(indexed)
  ))

  on(meanings)( t => declare (
    t.id is(primaryKey, autoIncremented),
    t.text is(dbType("text"))
  ))
}
