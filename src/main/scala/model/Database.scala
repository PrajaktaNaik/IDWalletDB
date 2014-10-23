package model

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.ComponentScan
import scala.collection.JavaConversions._
import scala.beans.BeanProperty
import scala.collection.mutable.Map
import java.text.SimpleDateFormat
import java.util.Date
import controller.MongoDbFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

object Database {
 // var users = Map[Int, User]()

  var mongoOps: MongoOperations = MongoDbFactory.mongoTemplate
  // add the user object to the map
  def addUser(u: User) {
     mongoOps.insert(u)   
   // users(u.getUser_id) = u //add user
  }
  // get the a user from the map
  def getUser(user_id: String): User = {
    val result:User = mongoOps.findOne(new Query(Criteria where("_id") is(user_id)), classOf[User], "user")
   // users(user_id) //return the use object
    println("Result"+result)
    return result
  }

  def updateUser(user_id: String, u: User): User = {

    var oldUser: User = getUser(user_id)
    
    if (u.getEmail != null) {
      oldUser.setEmail(u.getEmail)
    }

    if (u.getName != null) {
      oldUser.setName(u.getName)
    }

    if (u.getPassword != null) {
      oldUser.setPassword(u.getPassword)
    }
   mongoOps.save(oldUser)
    return oldUser
  }
  //get a list of users from the map
}
