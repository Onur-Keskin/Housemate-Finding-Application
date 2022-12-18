package com.onurkeskin.demodemobitirmeproje.service

import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface HouseOwnerAPI {
    @GET("houseOwners")//todos http://192.168.1.21:8080/houseOwners
    fun getOwners() : Observable<List<HouseOwnerModel>>

    @GET("houseOwners/{ownerId}")//http:192.168.1.21:8080/customers/{id}
    fun getOwnerById(
        @Path(value = "ownerId", encoded = true) ownerId: String?
    ): Observable<HouseOwnerModel>
}