package scalaplayground

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson._
import play.api.libs.iteratee.Iteratee
import scala.concurrent.Future


class MongoDb {

  case class User(Name: String)
  implicit val userFormat = Macros.handler[User]
  
  
  def test() = {
  
    // gets an instance of the driver
    // (creates an actor system)
    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))
  
    // Gets a reference to the database "plugin"
    val db = connection("scalaplayground")
  
    // Gets a reference to the collection "acoll"
    // By default, you get a BSONCollection.
    val c = db("testcol")
    
    //val entry = BSONDocument("Name"->"Karl")
    //c.save(entry)
    
    c.save(User("Andy"))
    
    val query = BSONDocument("Name" -> "Tom")
    val filter = BSONDocument("Name" -> 1)
    
//    //get full list
//    val futureList: Future[List[BSONDocument]] =
//      c.find(BSONDocument.empty,BSONDocument.empty).
//      cursor[BSONDocument].
//      collect[List]()
    
    val futureList: Future[List[User]] = 
      c.find(query, BSONDocument.empty).
      cursor[User].
      collect[List]()
      
    futureList.map { list =>
      list.foreach { user =>
        println("found user: " + user.Name)
      }
    } 

    val futureList2: Future[List[BSONDocument]] =
      c.find(query,BSONDocument.empty).
      cursor[BSONDocument].
      collect[List]()
      
    futureList2.map { list =>
      list.foreach { doc =>
        println("found document: " + BSONDocument.pretty(doc))
      }
    }
   //connection.close()
  }
}