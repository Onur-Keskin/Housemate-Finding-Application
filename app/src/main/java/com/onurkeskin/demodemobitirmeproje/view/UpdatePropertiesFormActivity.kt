package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.content_properties_form.*
import kotlinx.android.synthetic.main.content_properties_form.view.*
import kotlinx.android.synthetic.main.content_update_properties_form.*
import kotlinx.android.synthetic.main.content_update_properties_form.view.*
import kotlinx.android.synthetic.main.content_update_properties_form.welcomeText
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UpdatePropertiesFormActivity : AppCompatActivity() {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var compositeDisposable : CompositeDisposable? = null
    private val properTiesFormObject = JsonObject()
    private var customerBringPropertiesObject : JsonObject? = null
    private var houseOwnerBringPropertiesObject : JsonObject?=null
    private var customerUpdatePropertiesResponseObject : JsonObject? = null
    private var houseOwnerUpdatePropertiesResponseObject : JsonObject? = null
    //private var isSelected : Boolean? = null
    private lateinit var isCustomerOrHouseOwner:String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_properties_form)

        isCustomerOrHouseOwner = intent.getStringExtra("customerOrHouseOwner").toString()//Formu g??ncellenecek olan ki??i customer m?? houseOwner m???
        println("isCustomerOrHouseOwner : $isCustomerOrHouseOwner")
        compositeDisposable = CompositeDisposable()

        val updateAttributeUserName = intent.getStringExtra("updateAttributeUser-Name") // yeni kay??t olan kullan??c??n??n ad??
        welcomeText.text = " ${updateAttributeUserName}! Formunu G??ncelleyebilirsin :) "

        //Evin l??ks ayar??n?? se??ece??imiz dropdown menu
        val updateLuxuryImportanceSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.luxuryImportance,android.R.layout.simple_spinner_item)
        updateLuxuryImportanceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerUpdateLuxuryImportance.adapter = updateLuxuryImportanceSpinnerAdapter


        //Evin fiyat aral??????n?? se??ece??imiz dropdown menu
        val updatePriceRangeSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.priceRange,android.R.layout.simple_spinner_item)
        updatePriceRangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerUpdatePriceRange.adapter = updatePriceRangeSpinnerAdapter

        loadData()
    }

    //Attribute model bilgilerini getirecek olan bir GET REQUEST laz??m
    //Sonras??nda gerekli update i??lemi yap??lacak

    fun loadData(){
        val intent = intent
        val customerId = intent.getIntExtra("customerId",0)
        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)

        //println("customerId : $customerId")
        //println("houseOwnerId : $houseOwnerId")

        if (customerId != 0){ //Customer'??n form bilgileri gelecek
            val customerIdObject = JsonObject()
            customerIdObject.addProperty("customerId",customerId)
            println(customerIdObject)
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            compositeDisposable?.add(retrofit.getOneCustomerProperties(customerIdObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bringCustomerHandleResponse))
        }


        else if(houseOwnerId != 0){ //HouseOwner'??n form bilgileri gelecek
            val houseOwnerIdObject = JsonObject()
            houseOwnerIdObject.addProperty("houseOwnerId",houseOwnerId)
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            compositeDisposable?.add(retrofit.getOneHouseOwnerProperties(houseOwnerIdObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bringHouseOwnerHandleResponse))
        }

    }

    @SuppressLint("SetTextI18n") //Bu ne i??e yar??yo?
    private fun bringCustomerHandleResponse(customerProperties: JsonObject){ //Update Properties sayfas??na customer??n bilgisini getirecek
        customerBringPropertiesObject = customerProperties

        if(customerBringPropertiesObject != null){
            //updateTextCustomerOrHouseOwner.text = "Customer"
            updateSleepTime.setText(customerBringPropertiesObject!!.get("sleepTime").asString)
            updateRentingDuration.setText(customerBringPropertiesObject!!.get("rentingDuration").asString)
            updateGpaText.setText(customerBringPropertiesObject!!.get("gpa").asString)


            if(customerBringPropertiesObject!!.get("smooking").asBoolean){ //Sigara i??iyorsa
                radioUpdateUseSmokeGroup.radioUpdateYesSmokeButton.isChecked = true
                properTiesFormObject.addProperty("smooking",true)
            }else{ //sigara i??miyorsa
                radioUpdateUseSmokeGroup.radioUpdateNoSmokeButton.isChecked = true
                properTiesFormObject.addProperty("smooking",false)
            }

            if(customerBringPropertiesObject!!.get("havingPet").asBoolean){ //Evcil hayvan?? varsa
                radioUpdateHavePetGroup.radioUpdateYesPetButton.isChecked = true
                properTiesFormObject.addProperty("havingPet",true)
            }else{ //Evcil hayvan?? yoksa
                radioUpdateHavePetGroup.radioUpdateNoPetButton.isChecked = true
                properTiesFormObject.addProperty("havingPet",false)
            }

            if(customerBringPropertiesObject!!.get("smooking").asBoolean){
                radioUpdateUseSmokeGroup.radioUpdateYesSmokeButton.isChecked = true
            }else{
                radioUpdateUseSmokeGroup.radioUpdateNoSmokeButton.isChecked = true
            }

        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun bringHouseOwnerHandleResponse(houseOwnerProperties: JsonObject){ //Update Properties sayfas??na houseOwner'??n bilgisini getirecek
        houseOwnerBringPropertiesObject = houseOwnerProperties
        //println("houseOwnerBringPropertiesObject")
        //println(houseOwnerBringPropertiesObject)
        if(houseOwnerBringPropertiesObject != null){
            //updateTextCustomerOrHouseOwner.text = "HouseOwner"
            updateSleepTime.setText(houseOwnerBringPropertiesObject!!.get("sleepTime").asString)
            updateRentingDuration.setText(houseOwnerBringPropertiesObject!!.get("rentingDuration").asString)
            updateGpaText.setText(houseOwnerBringPropertiesObject!!.get("gpa").asString)
            //spinnerUpdateLuxuryImportance[spinnerUpdateLuxuryImportance.selectedItemPosition]
            //spinnerUpdatePriceRange[spinnerUpdatePriceRange.selectedItemPosition]

            if(houseOwnerBringPropertiesObject!!.get("smooking").asBoolean){ //Sigara i??iyorsa
                radioUpdateUseSmokeGroup.radioUpdateYesSmokeButton.isChecked = true
                properTiesFormObject.addProperty("smooking",true)
            }else{ //sigara i??miyorsa
                radioUpdateUseSmokeGroup.radioUpdateNoSmokeButton.isChecked = true
                properTiesFormObject.addProperty("smooking",false)
            }

            if(houseOwnerBringPropertiesObject!!.get("havingPet").asBoolean){ //Evcil hayvan?? varsa
                radioUpdateHavePetGroup.radioUpdateYesPetButton.isChecked = true
                properTiesFormObject.addProperty("havingPet",true)
            }else{ //Evcil hayvan?? yoksa
                radioUpdateHavePetGroup.radioUpdateNoPetButton.isChecked = true
                properTiesFormObject.addProperty("havingPet",false)
            }

        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }

    }

    fun handleDropdownMenues(){
        if(isCustomerOrHouseOwner == "Customer"){ //customer'??n formu dolduruluyorsa
            properTiesFormObject.addProperty("luxury",spinnerUpdateLuxuryImportance.selectedItem.toString().toInt())
            //println("Selected luxury : " + spinnerLuxuryImportance.selectedItem.toString().toInt())
            properTiesFormObject.addProperty("price",spinnerUpdatePriceRange.selectedItem.toString().toInt())
        }
        else{ //houseOwner'??n formu dolduruluyorsa
            properTiesFormObject.addProperty("luxury",spinnerUpdateLuxuryImportance.selectedItem.toString().toInt())
            properTiesFormObject.addProperty("price",spinnerUpdatePriceRange.selectedItem.toString().toInt())
        }
    }

    fun updateUseSmokeHandler(view:View){ //radio button
        if(radioUpdateUseSmokeGroup.radioUpdateYesSmokeButton.isChecked){ //sigara i??iyor
            if(isCustomerOrHouseOwner == "Customer"){
                properTiesFormObject.addProperty("smooking",true) //customer sigara i??iyor
            }else{
                properTiesFormObject.addProperty("smooking",true) //houseOwner sigara i??iyor
            }
        }else if(radioUpdateUseSmokeGroup.radioUpdateNoSmokeButton.isChecked){ //sigara i??miyor
            if(isCustomerOrHouseOwner == "Customer"){
                properTiesFormObject.addProperty("smooking",false) //customer sigara i??miyor
            }else{
                properTiesFormObject.addProperty("smooking",false) //houseOwner sigara i??miyor
            }
        }else{
            Toast.makeText(this@UpdatePropertiesFormActivity,"UsingSmoke Error",Toast.LENGTH_LONG).show()
        }
    }
    fun updateHavePetHandler(view:View){//radio button
        if(radioUpdateHavePetGroup.radioUpdateYesPetButton.isChecked){ //evcil hayvan var
            if(isCustomerOrHouseOwner == "Customer"){
                //println("customer'??n evcil hayvan?? var")
                properTiesFormObject.addProperty("havingPet",true) // customer'??n evcil hayvan?? var
            }else{
                properTiesFormObject.addProperty("havingPet",true) // houseOwner'??n evcil hayvan?? var
            }
        }else if(radioUpdateHavePetGroup.radioUpdateNoPetButton.isChecked){ //evcil hayvan yok
            if(isCustomerOrHouseOwner == "Customer"){
                //println("customer'??n evcil hayvan?? yok")
                properTiesFormObject.addProperty("havingPet",false) // customer'??n evcil hayvan?? yok
            }else{
                properTiesFormObject.addProperty("havingPet",false) // houseOwner'??n evcil hayvan?? yok
            }
        }else{
            Toast.makeText(this@UpdatePropertiesFormActivity,"HavePet Error",Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun sendUpdatePropertiesForm(view: View){
        val customerId = intent.getIntExtra("customerId",0)
        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)

        if(customerId != 0){ //customer properties form bilgilerini g??ncelle
            properTiesFormObject.addProperty("customerId",customerId)
            properTiesFormObject.addProperty("sleepTime",updateSleepTime.text.toString())
            properTiesFormObject.addProperty("rentingDuration",updateRentingDuration.text.toString().toInt())
            properTiesFormObject.addProperty("gpa",updateGpaText.text.toString().toDouble())

            //customerUpdatingPropertiesObject.addProperty("luxury",customerId)
            //customerUpdatingPropertiesObject.addProperty("price",updatePriceRangeText.text.toString().toInt())
            //customerUpdatingPropertiesObject.addProperty("smooking",updateEditTextUsername.text.toString())
            //customerUpdatingPropertiesObject.addProperty("havingPet",updateEditTextSurname.text.toString())


            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)

                handleDropdownMenues()

            //println("properTiesFormObject")
            //println(properTiesFormObject)

            compositeDisposable?.add(retrofit.saveOneCustomerProperties(properTiesFormObject)
                .subscribeOn(Schedulers.io())//asenkron bir ??ekilde ana thread'i bloklamadan i??lem yap??lacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de i??lenecek
                .subscribe(this::handleCustomerUpdatePropertiesClickResponse))


        }
        else if(houseOwnerId != 0){ //houseOwner properties form bilgilerini g??ncelle
            properTiesFormObject.addProperty("houseOwnerId",houseOwnerId)
            properTiesFormObject.addProperty("sleepTime",updateSleepTime.text.toString())
            properTiesFormObject.addProperty("rentingDuration",updateRentingDuration.text.toString().toInt())
            properTiesFormObject.addProperty("gpa",updateGpaText.text.toString().toDouble())



            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

                handleDropdownMenues()

            compositeDisposable?.add(retrofit.saveOneHouseOwnerProperties(properTiesFormObject)
                .subscribeOn(Schedulers.io())//asenkron bir ??ekilde ana thread'i bloklamadan i??lem yap??lacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de i??lenecek
                .subscribe(this::handleHouseOwnerUpdatePropertiesClickResponse))

        }
        else{
            Toast.makeText(this@UpdatePropertiesFormActivity,"HATA" , Toast.LENGTH_LONG).show()
        }
    }

    private fun handleCustomerUpdatePropertiesClickResponse(customerPropertiesUpdate: JsonObject){
        customerUpdatePropertiesResponseObject = customerPropertiesUpdate
        //println(userUpdateProfileResponseModel)
        val customerId = customerUpdatePropertiesResponseObject!!.getAsJsonObject("customer").get("customerId").asInt
        if(customerId != 0 ){//password de kontrol edilecek ama ??nce api de olmas?? ??art
            val intent = Intent(this@UpdatePropertiesFormActivity , MainPageActivity::class.java)
            intent.putExtra("userId", customerId)
            intent.putExtra("customerOrOwner","customer")
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@UpdatePropertiesFormActivity , SingleProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleHouseOwnerUpdatePropertiesClickResponse(houseOwnerUpdateProperties: JsonObject){
        houseOwnerUpdatePropertiesResponseObject = houseOwnerUpdateProperties
        //println(houseOwnerUpdatePropertiesResponseObject)
        val houseOwnerId = houseOwnerUpdatePropertiesResponseObject!!.getAsJsonObject("houseOwner").get("ownerId").asInt

        if(houseOwnerId != null ){//password de kontrol edilecek ama ??nce api de olmas?? ??art
            val intent = Intent(this@UpdatePropertiesFormActivity , MainPageActivity::class.java)
            intent.putExtra("ownerId",houseOwnerId)
            intent.putExtra("customerOrOwner","houseOwner")
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@UpdatePropertiesFormActivity , SingleProfileActivity::class.java)
            startActivity(intent)
        }

    }
}