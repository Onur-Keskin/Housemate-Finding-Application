package com.onurkeskin.demodemobitirmeproje.service

import com.google.gson.JsonObject
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import io.reactivex.Observable
import retrofit2.http.*

interface HouseOwnerAPI {
    @GET("houseOwners/getAllHouseOwners")
    fun getOwners() : Observable<List<HouseOwnerModel>>

    @GET("houseOwners/getOneHouseOwnerById/{ownerId}")
    fun getOwnerById(
        @Path(value = "ownerId", encoded = true) ownerId: String?
    ): Observable<HouseOwnerModel>

    @GET("houseOwners/getOneHouseOwnerByUsername/{houseOwnerUsername}")
    fun getOwnerByUsername(
        @Path(value = "houseOwnerUsername", encoded = true) houseOwnerUsername: String?
    ): Observable<HouseOwnerModel>

    @POST("houseOwners/saveOneHouseOwner")
    fun saveOneHouseOwner(@Body requestBody: JsonObject):Observable<JsonObject>

    @PUT("houseOwners/updateOneHouseOwner")
    fun updateOneHouseOwner(@Body requestBody: JsonObject):Observable<JsonObject>

    @POST("/models/saveOrUpdateOneHouseOwnerAttribute")
    fun saveOneHouseOwnerProperties(@Body requestBody: JsonObject):Observable<JsonObject>

}