package com.onurkeskin.demodemobitirmeproje.service

import com.google.gson.JsonObject
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HouseOwnerAPI {
    @GET("houseOwners/getAllHouseOwners")//todos http://192.168.1.21:8080/houseOwners
    fun getOwners() : Observable<List<HouseOwnerModel>>

    @GET("houseOwners/getOneHouseOwnerById/{ownerId}")//http:192.168.1.21:8080/customers/{id}
    fun getOwnerById(
        @Path(value = "ownerId", encoded = true) ownerId: String?
    ): Observable<HouseOwnerModel>

    @POST("houseOwner/saveOneHouseOwner")
    fun saveOneHouseOwner(@Body requestBody: JsonObject):Observable<HouseOwnerModel>
}