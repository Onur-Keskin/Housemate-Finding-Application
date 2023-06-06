package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_update_house.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UpdateHouseActivity : AppCompatActivity() {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var compositeDisposable : CompositeDisposable? = null

    private val houseObject = JsonObject()
    private var house : HouseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_house)

        compositeDisposable = CompositeDisposable()
        loadData()
    }

    fun loadData(){
        val intent = intent
        val houseId = intent.getIntExtra("houseId",0)
        println("houseId in UpdateHouseActivity : $houseId")

        if (houseId != 0){
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseAPI::class.java)

            compositeDisposable?.add(retrofit.getHouseById(houseId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bringHouseHandleResponse))
        }else{
            Toast.makeText(this,"Error happened in UpdateHouseActivity getHouseById function" , Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun bringHouseHandleResponse(houseModel:HouseModel){
        house = houseModel
        //println(house)

        updateHouseAddress.setText(house!!.houseAddress)
        updateHouseType.setText(house!!.houseType)
        updateHouseHeatResource.setText(house!!.heatResource)
        updateHouseFurnished.setText( house!!.furnished.toString())
        updateHouseInternetPaved.setText(house!!.internetPaved)
        updateHouseFloor.setText(house!!.floor.toString())
        updateHouseRent.setText(house!!.rent.toString())
        updateHouseOwnersTitle.text = "${house!!.owners[0].get("ownerName")}" + "${house!!.owners[0].get("ownerSurname")}"
        updateHouseDisableImageButtonBlue.isVisible = false
        updateHouseDisableImageButtonWhite.isVisible = true

        updateHouseImageButtonBlue.isVisible = false
        updateHouseImageButtonWhite.isVisible = true
    }

    fun confirmUpdateHouse(view: View){

        houseObject.addProperty("houseId", house?.houseId)
        houseObject.addProperty("houseAddress", updateHouseAddress.text.toString())
        houseObject.addProperty("houseType", updateHouseType.text.toString())
        houseObject.addProperty("heatResource", updateHouseHeatResource.text.toString())
        houseObject.addProperty("furnished", updateHouseFurnished.text.toString())
        houseObject.addProperty("internetPaved", updateHouseInternetPaved.text.toString())
        houseObject.addProperty("floor", updateHouseFloor.text.toString())
        houseObject.addProperty("rent", updateHouseRent.text.toString())


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseAPI::class.java)

        compositeDisposable?.add(retrofit.updateOneHouseByHouseId(houseObject, house?.houseId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateHouseHandleResponse))

    }

    private fun updateHouseHandleResponse(houseModel: HouseModel){

        updateHouseImageButtonBlue.isVisible = true
        updateHouseImageButtonWhite.isVisible = false
        Toast.makeText(this,"Update House Successfull" , Toast.LENGTH_LONG).show()

        val intent = Intent(this@UpdateHouseActivity, MainPageActivity::class.java)
        intent.putExtra("ownerId",houseModel.owners[0].get("ownerId").asInt)
        startActivity(intent)
    }
}