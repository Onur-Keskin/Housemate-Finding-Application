package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
//import kotlinx.android.synthetic.main.content_main.*
//import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.content_properties_form.*
import kotlinx.android.synthetic.main.content_properties_form.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PropertiesFormActivity : AppCompatActivity() {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private val properTiesFormObject = JsonObject()
    private lateinit var isCustomerOrHouseOwner:String
    private var compositeDisposable : CompositeDisposable? = null
    private var customerRegisterFormResponseModel: JsonObject? = null
    private var houseOwnerRegisterFormResponseModel: JsonObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_properties_form)

        isCustomerOrHouseOwner = intent.getStringExtra("fromRegisterPage").toString()//Formu kaydedilecek olan kişi customer mı houseOwner mı?

        val registeredUserName = intent.getStringExtra("registeredUser-Name") // yeni kayıt olan kullanıcının adı
        welcomeText.text = "Hoşgeldin! ${registeredUserName}! Seni biraz tanıyalım :) "

        //Evin lüks ayarını seçeceğimiz dropdown menu
        val luxuryImportanceSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.luxuryImportance,android.R.layout.simple_spinner_item)
        luxuryImportanceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerLuxuryImportance.adapter = luxuryImportanceSpinnerAdapter


        //Evin fiyat aralığını seçeceğimiz dropdown menu
        val priceRangeSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.priceRange,android.R.layout.simple_spinner_item)
        priceRangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerPriceRange.adapter = priceRangeSpinnerAdapter


        compositeDisposable = CompositeDisposable()

    }
    fun sendForm(view: View){
        val customerId = intent.getIntExtra("customerId",0)
        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)

        //println("houseOwnerId : $houseOwnerId")

        if(customerId != 0){ //customer için
            properTiesFormObject.addProperty("customerId",customerId)//customer'a eklenecek
            properTiesFormObject.addProperty("sleepTime",sleepTime.text.toString())//customer'a eklenecek
            properTiesFormObject.addProperty("gpa",gpa.text.toString().toDouble())//customer'a eklenecek
            properTiesFormObject.addProperty("rentingDuration",rentingDuration.text.toString().toInt())//customer'a eklenecek

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)

            handleDropdownMenues() //luxury ve price range için

            compositeDisposable?.add(retrofit.saveOneCustomerProperties(properTiesFormObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleCustomerFormRegisterResponse))
        }
        else if(houseOwnerId != 0){ //houseOwner için

            properTiesFormObject.addProperty("houseOwnerId",houseOwnerId)//houseOwner'a eklenecek
            properTiesFormObject.addProperty("sleepTime",sleepTime.text.toString())//houseOwner'a eklenecek
            properTiesFormObject.addProperty("gpa",gpa.text.toString().toDouble())//houseOwner'a eklenecek
            properTiesFormObject.addProperty("rentingDuration",rentingDuration.text.toString().toInt())//houseOwner'a eklenecek

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            handleDropdownMenues() //luxury ve price range için

            println(properTiesFormObject)

            compositeDisposable?.add(retrofit.saveOneHouseOwnerProperties(properTiesFormObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleHouseOwnerFormRegisterResponse))
        }
        else{
            Toast.makeText(this@PropertiesFormActivity,"House Owner Properties Form Error ",Toast.LENGTH_LONG).show()
            startActivity(Intent(this@PropertiesFormActivity,RegisterActivity::class.java))
        }
    }

    fun handleDropdownMenues(){
        if(isCustomerOrHouseOwner == "customer"){ //customer'ın formu dolduruluyorsa
            properTiesFormObject.addProperty("luxury",spinnerLuxuryImportance.selectedItem.toString().toInt())//customer'a eklenecek
            //println("Selected luxury : " + spinnerLuxuryImportance.selectedItem.toString().toInt())
            properTiesFormObject.addProperty("price",spinnerPriceRange.selectedItem.toString().toInt())//customer'a eklenecek
        }
        else{ //houseOwner'ın formu dolduruluyorsa
            properTiesFormObject.addProperty("luxury",spinnerLuxuryImportance.selectedItem.toString().toInt())//houseOwner'a eklenecek
            properTiesFormObject.addProperty("price",spinnerPriceRange.selectedItem.toString().toInt())//houseOwner'a eklenecek
        }
    }

    private fun handleCustomerFormRegisterResponse(customerRegister: JsonObject){
        customerRegisterFormResponseModel = customerRegister
        println(customerRegisterFormResponseModel)
        val customerId = customerRegisterFormResponseModel!!.getAsJsonObject("customer").get("customerId").asInt
        println(customerId)
        if(customerId != null ){//password de kontrol edilecek ama önce api de olması şart
            val intent = Intent(this@PropertiesFormActivity,HousesActivity::class.java)
            intent.putExtra("userId",customerId)
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@PropertiesFormActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleHouseOwnerFormRegisterResponse(houseOwnerFormRegister: JsonObject){

        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)
        val houseOwnerName = intent.getStringExtra("registeredUser-Name")
        val houseOwnerSurname = intent.getStringExtra("registeredUser-Surname")
        val houseOwnerUsername = intent.getStringExtra("registeredUser-Username")
        val houseOwnerEmail = intent.getStringExtra("registeredUser-Email")
        val houseOwnerPassword = intent.getStringExtra("registeredUser-Password")

        houseOwnerRegisterFormResponseModel = houseOwnerFormRegister

        println("houseOwnerRegisterFormResponseModel : " )
        println(houseOwnerRegisterFormResponseModel)

        //val houseOwnerId = houseOwnerRegisterFormResponseModel!!.getAsJsonObject("owner").get("ownerId").asInt
        //println(houseOwnerId)
        if(houseOwnerId != 0 ){

            val intent = Intent(this@PropertiesFormActivity,SaveHouseActivity::class.java)
            intent.putExtra("houseOwnerId",houseOwnerId)
            intent.putExtra("houseOwnerName",houseOwnerName)
            intent.putExtra("houseOwnerSurname",houseOwnerSurname)
            intent.putExtra("houseOwnerUsername",houseOwnerUsername)
            intent.putExtra("houseOwnerEmail",houseOwnerEmail)
            intent.putExtra("houseOwnerPassword",houseOwnerPassword)
            startActivity(intent)



            /*
            val intent = Intent(this@PropertiesFormActivity,MainPageActivity::class.java)
            intent.putExtra("customerOrOwner","houseOwner")
            intent.putExtra("ownerId",houseOwnerId)
            startActivity(intent)
            //finish()

             */

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@PropertiesFormActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun useSmokeHandler(view:View){ //radio button
        if(radioUseSmokeGroup.radioYesSmokeButton.isChecked){ //sigara içiyor
            if(isCustomerOrHouseOwner == "customer"){
                properTiesFormObject.addProperty("smooking",true) //customer sigara içiyor
            }else{
                properTiesFormObject.addProperty("smooking",true) //houseOwner sigara içiyor
            }
        }else if(radioUseSmokeGroup.radioNoSmokeButton.isChecked){ //sigara içmiyor
            if(isCustomerOrHouseOwner == "customer"){
                properTiesFormObject.addProperty("smooking",false) //customer sigara içmiyor
            }else{
                properTiesFormObject.addProperty("smooking",false) //houseOwner sigara içmiyor
            }
        }else{
            Toast.makeText(this@PropertiesFormActivity,"UsingSmoke Error",Toast.LENGTH_LONG).show()
        }
    }
    fun havePetHandler(view:View){//radio button
        if(radioHavePetGroup.radioYesPetButton.isChecked){ //evcil hayvan var
            if(isCustomerOrHouseOwner == "customer"){
                properTiesFormObject.addProperty("havingPet",true) // customer'ın evcil hayvanı var
            }else{
                properTiesFormObject.addProperty("havingPet",true) // houseOwner'ın evcil hayvanı var
            }
        }else if(radioHavePetGroup.radioNoPetButton.isChecked){ //evcil hayvan yok
            if(isCustomerOrHouseOwner == "customer"){
                properTiesFormObject.addProperty("havingPet",false) // customer'ın evcil hayvanı yok
            }else{
                properTiesFormObject.addProperty("havingPet",false) // houseOwner'ın evcil hayvanı yok
            }
        }else{
            Toast.makeText(this@PropertiesFormActivity,"HavePet Error",Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    //Gerekli Endpointe Post Request Yaptıktan sonra houseOwner olup olmama durumuna göre de ev ilanı eklemesini isteyeceksin

}