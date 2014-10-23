package model

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import scala.annotation.meta.beanGetter
import javax.validation.constraints.{ Min, NotNull }
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.ComponentScan
import scala.collection.JavaConversions._
import scala.beans.BeanProperty
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
class IdCards(@JsonProperty("card_name")@BeanProperty @(NotNull @beanGetter)@(NotEmpty @beanGetter) var card_name: String,
  @JsonProperty("card_number")@BeanProperty @(NotNull @beanGetter)@(NotEmpty @beanGetter) var card_number: String,
  @JsonProperty("expiration_date")@BeanProperty @(NotNull @beanGetter)@(NotEmpty @beanGetter) var expiration_date: String) {
@Id 
  @BeanProperty var card_id: String = this.hashCode().toString

}