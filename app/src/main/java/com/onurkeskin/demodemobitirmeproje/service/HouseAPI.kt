package com.onurkeskin.demodemobitirmeproje.service

import com.google.gson.JsonObject
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("houses/updateOneHouseByHouseId/{houseId}")
    fun updateOneHouseByHouseId(@Body requestBody: JsonObject,
        @Path(value = "houseId", encoded = true) houseId: String?
    ): Observable<HouseModel>

    @GET("houses/getHousesByClass/{classOfHouse}")
    fun getHousesByClass(
        @Path(value = "classOfHouse", encoded = true) classOfHouse: String?
    ): Observable<List<HouseModel>>

    @POST("relations/likeControl")
    fun likeControl(@Body requestBody: JsonObject):Observable<Boolean>

    @HTTP(method = "DELETE", path = "/relations/deleteRelation", hasBody = true)
    fun deleteLike(@Body requestBody: JsonObject) : Observable<JsonObject>
}
