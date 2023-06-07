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
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_update_house.*
import kotlinx.android.synthetic.main.activity_update_house.view.*
import kotlinx.android.synthetic.main.content_update_properties_form.*
import kotlinx.android.synthetic.main.content_update_properties_form.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UpdateHouseActivity : AppCompatActivity() {
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
                .baseUrl(GlobalVariables.globalBASEURL)
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

        updateHouseAdressEditText.setText(house!!.houseAddress)
        updateHouseFloorEditText.setText(house!!.floor.toString())
        updateHouseRentEditText.setText(house!!.rent.toString())

        bringHouseTypeRadioButtonChoices()//Ev tipi için radio button seçimlerini getirir

        bringHeatResourceRadioButtonChoices() //Evin mevcut yakıt türünün radio button seçimlerini getirir

        bringFurnitureRadioButtonChoices() //Evin mevcut eşyalı olma durumunun radio button seçimlerini getirir

        bringInternetPavedRadioButtonChoices() //Evin mevcut internet dahil  olma durumunun radio button seçimlerini getirir


        updateHouseOwnersTitle.text = "${house!!.owners[0].get("ownerName")} + ${house!!.owners[0].get("ownerSurname")}"



        updateHouseDisableImageButtonBlue.isVisible = false
        updateHouseDisableImageButtonWhite.isVisible = true

        updateHouseImageButtonBlue.isVisible = false
        updateHouseImageButtonWhite.isVisible = true
    }

    fun confirmUpdateHouse(view: View){

        houseObject.addProperty("houseId", house?.houseId)
        houseObject.addProperty("houseAddress", updateHouseAdressEditText.text.toString())
        houseObject.addProperty("floor", updateHouseFloorEditText.text.toString())
        houseObject.addProperty("rent", updateHouseRentEditText.text.toString())


        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalVariables.globalBASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseAPI::class.java)

        compositeDisposable?.add(retrofit.updateOneHouseByHouseId(houseObject, house?.houseId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateHouseHandleResponse))

    }

    private fun bringHouseTypeRadioButtonChoices(){
        if(house!!.houseType == "1+1"){ //1+1
            updateHouseRadioHouseTypeGroup.updateHouseRadioTwoPlusOneButton.isChecked = true
            houseObject.addProperty("houseType","1+1")
        }
        else if(house!!.houseType == "2+1"){ //2+1
            updateHouseRadioHouseTypeGroup.updateHouseRadioTwoPlusOneButton.isChecked = true
            houseObject.addProperty("houseType","2+1")
        }
        else if(house!!.houseType == "3+1"){ //3+1
            updateHouseRadioHouseTypeGroup.updateHouseRadioThreePlusOneButton.isChecked= true
            houseObject.addProperty("houseType","3+1")
        }
        else if(house!!.houseType == "4+1"){ //4+1
            updateHouseRadioHouseTypeGroup.updateHouseRadioFourPlusOneButton.isChecked= true
            houseObject.addProperty("houseType","4+1")
        }
        else{ //Other HouseType
            updateHouseRadioHouseTypeGroup.updateHouseRadioOtherButton.isChecked= true
            houseObject.addProperty("houseType","diger")
        }
    }

    private fun bringHeatResourceRadioButtonChoices(){
        if(house!!.heatResource == "Doğal Gaz"){
            updateHouseRadioUseHeatResourceGroup.updateHouseRadioGasButton.isChecked = true
            houseObject.addProperty("heatResource","Doğal Gaz")
        }
        else if(house!!.heatResource == "Kömür"){
            updateHouseRadioUseHeatResourceGroup.updateHouseRadioCoalButton.isChecked = true
            houseObject.addProperty("heatResource","Kömür")
        }else{
            updateHouseRadioUseHeatResourceGroup.updateHouseRadioOtherHeatResourceButton.isChecked = true
            houseObject.addProperty("heatResource","diger")
        }

    }

    private fun bringFurnitureRadioButtonChoices(){
        if(house!!.furnished == "yes"){
            updateHouseRadioIsFurnitureGroup.updateHouseRadioYesFurnitureButton.isChecked = true
            houseObject.addProperty("furnished","yes")
        } else{
            updateHouseRadioIsFurnitureGroup.updateHouseRadioNoFurnitureButton.isChecked = true
            houseObject.addProperty("furnished","no")
        }

    }

    private fun bringInternetPavedRadioButtonChoices(){
        if(house!!.internetPaved == "yes"){
            updateHouseRadioInternetPavedGroup.updateHouseRadioYesInternetPavedButton.isChecked = true
            houseObject.addProperty("internetPaved","yes")
        } else{
            updateHouseRadioInternetPavedGroup.updateHouseRadioNoInternetPavedButton.isChecked = true
            houseObject.addProperty("internetPaved","no")
        }

    }

    fun updateHouseTypeHandler(view:View){ //HouseTypeRadioButton
        if(updateHouseRadioHouseTypeGroup.updateHouseRadioOnePlusOneButton.isChecked){ //1+1
            houseObject.addProperty("houseType","1+1")
        }
        else if(updateHouseRadioHouseTypeGroup.updateHouseRadioTwoPlusOneButton.isChecked){ //2+1
            houseObject.addProperty("houseType","2+1")
        }
        else if(updateHouseRadioHouseTypeGroup.updateHouseRadioThreePlusOneButton.isChecked){ //3+1
            houseObject.addProperty("houseType","3+1")
        }
        else if(updateHouseRadioHouseTypeGroup.updateHouseRadioFourPlusOneButton.isChecked){ //4+1
            houseObject.addProperty("houseType","4+1")
        }
        else{ //Other House Type
            houseObject.addProperty("houseType","diger")
        }
    }

    fun updateHouseHeatResourceHandler(view:View){//HeatResourceRadioButton
        if(updateHouseRadioUseHeatResourceGroup.updateHouseRadioGasButton.isChecked){ //Doğal Gaz
            houseObject.addProperty("heatResource","Doğal Gaz")
        }
        else if(updateHouseRadioUseHeatResourceGroup.updateHouseRadioCoalButton.isChecked){ //Kömür
            houseObject.addProperty("heatResource","Kömür")
        }
        else{//Diğer
            houseObject.addProperty("heatResource","diger")
        }
    }

    fun updateHouseIsFurnitureHandler(view:View){ //IsFurnitureRadioButton
        if(updateHouseRadioIsFurnitureGroup.updateHouseRadioYesFurnitureButton.isChecked){ //Eşyalı
            houseObject.addProperty("furnished","yes")
        }
        else{ //Eşyasız
            houseObject.addProperty("furnished","no")
        }
    }

    fun updateHouseIsInternetPavedHandler(view:View){ //IsInternetPavedRadioButton
        if(updateHouseRadioInternetPavedGroup.updateHouseRadioYesInternetPavedButton.isChecked){ //Internet dahil
            houseObject.addProperty("internetPaved","yes")
        }
        else{ //Internet dahil değil
            houseObject.addProperty("internetPaved","no")
        }
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