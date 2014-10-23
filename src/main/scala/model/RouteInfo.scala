package model
import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty


class RouteInfo(
    @BeanProperty@JsonProperty("message") var message: String,
      @BeanProperty@JsonProperty("change_date") var change_date: String,
       @BeanProperty@JsonProperty("office_code") var office_code: String,
    @BeanProperty@JsonProperty("office_card") var office_card: String,
    @BeanProperty@JsonProperty("new_routing_number") var new_routing_number: String,
                  @BeanProperty@JsonProperty("record_type_code") var record_type_code: String,
                    @BeanProperty@JsonProperty("rn") var rn: String,
                     @BeanProperty@JsonProperty("state") var state: String,
                  @BeanProperty@JsonProperty("address") var address: String,
                   @BeanProperty@JsonProperty("telephone") var telephone: String,
                    @BeanProperty@JsonProperty("data_view_code") var data_view_code: String,
                     @BeanProperty@JsonProperty("code") var code: String,
                      @BeanProperty@JsonProperty("customer_name") var customer_name: String,
                        @BeanProperty@JsonProperty("city") var city: String,
                         @BeanProperty@JsonProperty("routing_number") var routing_number: String,
                         @BeanProperty@JsonProperty("institution_status_code") var institution_status_code:String,
                  @BeanProperty@JsonProperty("zip") var zip: String                           
                   ){

}