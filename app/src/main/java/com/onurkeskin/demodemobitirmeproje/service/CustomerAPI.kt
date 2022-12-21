package com.onurkeskin.demodemobitirmeproje.service

import com.google.gson.JsonObject
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerAPI {
    @GET("customers")//http://192.168.1.21:8080/customers
    fun getCustomers() : Observable<List<CustomerModel>> //Gözlemlenebilir obje yani youtube subscribe gibi bir değişiklik olursa haber veren

    @GET("customers/{customerId}")//http:192.168.1.21:8080/customers/{id}
    fun getCustomerById(
        @Path(value = "customerId", encoded = true) customerId: String?
    ): Observable<CustomerModel>

    @GET("customers/byUsername/{userName}")//http://172.18.141.14:8080/customers/byUsername/hsman
    fun getUserByUsername(
        @Path(value = "userName", encoded = true) userName: String?
    ): Observable<CustomerModel>

    @POST("save2")
    fun addCustomer(@Body requestBody: JsonObject):Observable<CustomerModel>//henüz backend de bir dönüş body si yok
}