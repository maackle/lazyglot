package superglot

import org.squeryl.Session
import org.squeryl.adapters.H2Adapter

package object util {

  def initH2(dbName:String) = {
    import org.squeryl.SessionFactory

    Class.forName("org.h2.Driver");

    SessionFactory.concreteFactory = Some(()=>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:h2:%s" format dbName),
        new H2Adapter)
    )
  }

  def printProgress(i:Int)(a:Int, b:Int) {
    if (i % b == 0) print("\n" + i)
    else if (i % a == 0) print(" .")
  }
}
