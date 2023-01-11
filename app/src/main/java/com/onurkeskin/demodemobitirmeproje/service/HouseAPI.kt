package com.onurkeskin.demodemobitirmeproje.service

import com.google.gson.JsonObject
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HouseAPI {
    @GET("houses/getAllHouses")
    fun getHouses(): Observable<List<HouseModel>>

    @GET("houses/getOneHouseById/{houseId}")//http:192.168.1.21:8080/customers/{id}
    fun getHouseById(
        @Path(value = "houseId", encoded = true) houseId: String?
    ): Observable<HouseModel>

    @POST("houses/saveOneHouse")
    fun saveOneHouse(@Body requestBody: JsonObject):Observable<JsonObject>


    @GET("relations/getAllHousesOfOneCustomer/{customerId}")
    fun getAllHousesOfOneCustomer(
        @Path(value = "customerId", encoded = true) customerId: String?
    ): Observable<List<HouseModel>>

}
