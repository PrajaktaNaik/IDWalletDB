package controller

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.ComponentScan

object WalletApplication {
  
  def main(args: Array[String]) {
    SpringApplication.run(classOf[WalletApplication])
  }
  
  

}

@ComponentScan
@EnableAutoConfiguration
class WalletApplication {

}