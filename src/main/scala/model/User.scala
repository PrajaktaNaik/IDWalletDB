package model

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import scala.util
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.ComponentScan
import scala.collection.JavaConversions._
import scala.beans.BeanProperty
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.immutable.Map
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.log4j.Logger
import scala.annotation.meta.beanGetter
import javax.validation.constraints.{Min, NotNull}
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id
import controller.MongoDbFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class User(
    @JsonProperty("email")@BeanProperty @(NotNull @beanGetter)@(NotEmpty @beanGetter) var email: String, 
    @JsonProperty("name")@BeanProperty @(NotNull @beanGetter)@(NotEmpty @beanGetter) var name: String,
  @JsonProperty("password")@BeanProperty  @(NotNull@beanGetter) @(NotEmpty @beanGetter)  var password: String) 
  {
  @Id
  @BeanProperty val user_id: String = this.hashCode().toString;
  @BeanProperty var created_at: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  @BeanProperty var updated_at: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  var idCards = Map[String, IdCards]()
  var webLogin = Map[String, WebLogin]()
  var bankAccount = Map[String, BankAccount]()
  //val logger = Logger.getLogger(getClass().getName());
//  add one id card
  def addIdCard(idcard: IdCards) {
    println("id card is "+idcard.getCard_id)
   // idCards(idcard.getCard_id) = idcard
    this.idCards =this.idCards.+(idcard.getCard_id->new IdCards(idcard.getCard_name,idcard.getCard_number,idcard.getExpiration_date))
    Database.mongoOps.save(this)
  }

  //view one id card
 def getIdCard(card_id: String): IdCards = {
this.idCards(card_id)  
  }

 // view all idcard
  def viewallIdCard(): List[IdCards] = {
 val result=Database.mongoOps.findAll(classOf[IdCards])
    idCards.values.toList //pass the map object
  }

  //delete one id card
  def deleteIdCard(card_id: String) {
    if (this.idCards.contains(card_id)) {
      println("Idcard"+card_id)
      this.idCards =this.idCards - card_id
    }
   Database.mongoOps.save(this)
  }

  //add one weblogin
 def addWebLogin(wlogin: WebLogin) {
   // webLogin(wlogin.getLogin_id) = wlogin //add user
   this.webLogin =this.webLogin.+(wlogin.getLogin_id->new WebLogin(wlogin.getUrl,wlogin.getLogin,wlogin.getPassword))
    Database.mongoOps.save(this)
  }

  //get one weblogin
  def getWebLogin(login_id: String): WebLogin = {
this.webLogin (login_id)
  }

  //view all weblogin
  def viewallWebLogin(): List[WebLogin] = {
   val result=Database.mongoOps.findAll(classOf[WebLogin])
    webLogin.values.toList //pass the map object
  }

  //delete one web login
  def deleteWebLogin(login_id: String) {
    if (this.webLogin.contains(login_id)) {
      this.webLogin =this.webLogin - login_id
    }
     Database.mongoOps.save(this)
  }

  //add one bank account
  def addBankAcc(bankAcc: BankAccount) {
   // bankAccount(bankAcc.getBa_id) = bankAcc
     this.bankAccount =this.bankAccount.+(bankAcc.getBa_id->new BankAccount(bankAcc.getAccount_name,bankAcc.getRouting_number,bankAcc.getAccount_number))
    Database.mongoOps.save(this)
  }

  //get one bank account
  def getBankAcc(ba_id: String): BankAccount = {
  this.bankAccount (ba_id)
  }

  //get list of bank account
  def viewallBankAcc(): List[BankAccount] = {
  val result=Database.mongoOps.findAll(classOf[BankAccount])
    bankAccount.values.toList //pass the map object
  }

  //delete one bank account
  def deleteBankAccount(ba_id: String) {
    if (this.bankAccount.contains(ba_id)) {
    this.bankAccount=this.bankAccount -ba_id
    }
     Database.mongoOps.save(this)
  }
}
