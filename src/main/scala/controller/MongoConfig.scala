package controller

import com.mongodb.Mongo
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfig extends AbstractMongoConfiguration {
  
  def getDatabaseName:String = "wallet"
 
  def mongo:Mongo = new Mongo("localhost")
  
}