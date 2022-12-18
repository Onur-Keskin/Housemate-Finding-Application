package com.onurkeskin.demodemobitirmeproje.service

import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface HouseAPI {
    @GET("houses")
    fun getHouses(): Observable<List<HouseModel>>

    @GET("houses/{houseId}")//http:192.168.1.21:8080/customers/{id}
    fun getHouseById(
        @Path(value = "houseId", encoded = true) houseId: String?
    ): Observable<HouseModel>
}
