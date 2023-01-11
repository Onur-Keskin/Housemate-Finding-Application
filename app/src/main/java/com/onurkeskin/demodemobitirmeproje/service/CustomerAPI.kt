package com.onurkeskin.demodemobitirmeproje.service

import com.google.gson.JsonObject
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerAPI {
    @GET("customers/getAllCustomers")//http://192.168.1.21:8080/customers
    fun getCustomers() : Observable<List<CustomerModel>> //Gözlemlenebilir obje yani youtube subscribe gibi bir değişiklik olursa haber veren

    @GET("customers/getOneCustomerById/{customerId}")//http:192.168.1.21:8080/customers/{id}
    fun getCustomerById(
        @Path(value = "customerId", encoded = true) customerId: String?
    ): Observable<CustomerModel>

    @GET("customers/getOneCustomerByUsername/{userName}")
    fun getCustomerByUsername(
        @Path(value = "userName", encoded = true) userName: String?
    ): Observable<CustomerModel>

    @POST("models/getOneModelAttributeByCustomerId")
    fun getOneCustomerProperties(@Body requestBody: JsonObject):Observable<JsonObject>

    @POST("customers/saveOneCustomer")
    fun saveOneCustomer(@Body requestBody: JsonObject):Observable<JsonObject>

    @POST("/models/saveOrUpdateOneModelAttrOfCustomer")
    fun saveOneCustomerProperties(@Body requestBody: JsonObject):Observable<JsonObject>

    @POST("/relations/createOneRelation")
    fun customerLikeHouse(@Body requestBody : JsonObject):Observable<JsonObject>

    @PUT("customers/updateOneCustomer")
    fun updateOneCustomer(@Body requestBody: JsonObject):Observable<JsonObject>





}