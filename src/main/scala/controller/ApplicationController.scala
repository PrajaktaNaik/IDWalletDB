package controller

import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import scala.collection.JavaConversions._
import org.springframework.web.bind.annotation.RequestBody
import model.User
import model.Database
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import model.IdCards
import model.WebLogin
import model.BankAccount
import collection.JavaConverters._
import org.apache.log4j.Logger
import javax.validation.Valid
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import com.mongodb.Mongo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.validation.BindingResult
import org.springframework.web.client.HttpClientErrorException
import org.springframework.http.MediaType
import java.util
import org.springframework.http.ResponseEntity
import com.fasterxml.jackson.databind.ObjectMapper
import model.RouteInfo

@RestController
class ApplicationController {
//url for checking the routing number
  val userRoutingUrl:String = "http://www.routingnumbers.info/api/data.json?rn="
  
  val logger = Logger.getLogger(getClass().getName());
  //create user
  @RequestMapping(method = Array(RequestMethod.POST), value = Array("/api/v1/users"))
  def createUser(@Valid @RequestBody user: User,bindingResult:BindingResult,httpresponse: HttpServletResponse): Any = {
     if (bindingResult.hasErrors()) {
      httpresponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }else{
    println(user.toString())
    Database.addUser(user) // add the object user in the map
    println("user id is :" + user.getUser_id)
    Database.getUser(user.getUser_id) // return the user response converted to json
  }
  }
  //view user
  @RequestMapping(Array("/api/v1/users/{user_id}"))
  def viewUser(@PathVariable user_id: String, request: HttpServletRequest, response: HttpServletResponse): Any = {
    Database.getUser(user_id)
  }

  //update user
  @RequestMapping(method = Array(RequestMethod.PUT), value = Array("/api/v1/users/{user_id}"))
  def updateUser(@PathVariable user_id: String, @Valid @RequestBody user: User): User = {
    Database.updateUser(user_id, user)
  }

  //create the IDcard
  @RequestMapping(method = Array(RequestMethod.POST), value = Array("/api/v1/users/{user_id}/idcards"))
  def createIdCard(@PathVariable user_id: String, @Valid @RequestBody id_card: IdCards,bindingResult:BindingResult,httpresponse: HttpServletResponse): Any = {
    // first get the user of this particular user_id
    if (bindingResult.hasErrors()) {
      httpresponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }else{
    val u: User = Database.getUser(user_id)
    // add the idcard information to this user.
    u.addIdCard(id_card)
    logger.info("Size1:" + u.idCards.size)
    u.getIdCard(id_card.getCard_id)
  }
  }
  //List all ID cards
  
  //Resource: /users/{user_id}/idcards.  List zero or more ID cards from the wallet.
  @RequestMapping(Array("/api/v1/users/{user_id}/idcards"))
  def viewIdCards(@PathVariable user_id: String): java.util.List[IdCards] = {
    val u: User = Database.getUser(user_id)
    println("user id is"+u.getUser_id)
    logger.info("Size2:" + u.idCards.size)
    u.viewallIdCard().asJava
  }
  
  //delete  one idcard of a particular user
  @RequestMapping(Array("/api/v1/users/{user_id}/idcards/{card_id}"))
  def deleteIdCard(@PathVariable user_id: String, @PathVariable card_id: String) {
    val u: User = Database.getUser(user_id)
    u.deleteIdCard(card_id)
  }

  //create a web login
  @RequestMapping(method = Array(RequestMethod.POST), value = Array("/api/v1/users/{user_id}/weblogins"))
  def createWebLogin(@PathVariable user_id: String, @Valid @RequestBody weblogin: WebLogin): WebLogin = {
    // first get the user of this particular user_id
    val u: User = Database.getUser(user_id)
    // add the web login information to this user.
    u.addWebLogin(weblogin)
    u.getWebLogin(weblogin.getLogin_id)

  }

  //list all web login
  @RequestMapping(Array("/api/v1/users/{user_id}/weblogins"))
  def viewWebLogins(@PathVariable user_id: String): java.util.List[WebLogin] = {
    val u: User = Database.getUser(user_id)
    u.viewallWebLogin().asJava
  }

  //delete a web login
  @RequestMapping(Array("/api/v1/users/{user_id}/weblogins/{login_id}"))
  def deleteWebLogin(@PathVariable user_id: String, @PathVariable login_id: String) {
    val u: User = Database.getUser(user_id)
    u.deleteWebLogin(login_id)
  }
  //create a bank account
  @RequestMapping(method = Array(RequestMethod.POST), value = Array("/api/v1//users/{user_id}/bankaccounts"))
  def createBankAccount(@PathVariable user_id: String, @Valid @RequestBody bankAccount: BankAccount,bindingResult:BindingResult,httpresponse: HttpServletResponse): Any = {
    // first get the user of this particular user_id
    if (bindingResult.hasErrors()) {
      httpresponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      //new ErrorMessage("Error")
      "Please provide all valid parameters in the request!"
    }else{
  
    httpresponse.setStatus(HttpServletResponse.SC_CREATED)
     val httpHeader: HttpHeaders = new HttpHeaders()
    try{
      val u: User = Database.getUser(user_id)
       val rt: RestTemplate = new RestTemplate()
        var headers: HttpHeaders = new HttpHeaders()
        headers.setAccept(util.Arrays.asList(MediaType.APPLICATION_JSON))
        var entity : HttpEntity[String] = new HttpEntity[String](headers)
        //get the response of the routing url
        var response: ResponseEntity[String] = rt.exchange(userRoutingUrl+ bankAccount.routing_number, HttpMethod.GET, entity, classOf[String]);
        var resource: String = response.getBody()
        var ri: RouteInfo = new ObjectMapper().readValue(resource, classOf[RouteInfo])
        print("Customer Name" + ri.customer_name)
        print("State" + ri.state)
        print("Message" + ri.message)
        print("Code" + ri.code)
        if (ri.code == "404")
        {
         "Please provide all valid parameters in the request!"
        }
        else{
          bankAccount.account_name = ri.customer_name
           u.addBankAcc(bankAccount)
          u.getBankAcc(bankAccount.getBa_id)
          
        }  
    }catch{
      case ioe: HttpClientErrorException =>
         throw ioe
    }
    }
    
    // add the bank acc information to this user.
    
    
 
   
  
  }
  //list all bank account
  @RequestMapping(Array("/api/v1/users/{user_id}/bankaccounts"))
  def viewBankAccounts(@PathVariable user_id: String): java.util.List[BankAccount] = {
    val u: User = Database.getUser(user_id)
    u.viewallBankAcc().asJava
  }

  //delete a bank account
  @RequestMapping(Array("/api/v1/users/{user_id}/bankaccounts/{ba_id}"))
  def deleteBankAccount(@PathVariable user_id: String, @PathVariable ba_id: String) {
    val u: User = Database.getUser(user_id)
    u.deleteBankAccount(ba_id)
  }
}
