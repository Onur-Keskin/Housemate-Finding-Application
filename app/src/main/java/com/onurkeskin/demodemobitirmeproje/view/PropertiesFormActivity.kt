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
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
//import kotlinx.android.synthetic.main.content_main.*
//import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.content_properties_form.*
import kotlinx.android.synthetic.main.content_properties_form.view.*

class PropertiesFormActivity : AppCompatActivity() {
    private val properTiesFormObject = JsonObject()
    private lateinit var isCustomerOrHouseOwner:String

    @SuppressLint("ResourceType")
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

        //spinnerLuxuryImportance.selectedItem.toString() //seçilen dropdown item ı böyle alacağız

        //Evin fiyat aralığını seçeceğimiz dropdown menu
        val priceRangeSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.priceRange,android.R.layout.simple_spinner_item)
        priceRangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerPriceRange.adapter = priceRangeSpinnerAdapter

        //spinnerPriceRange.selectedItem.toString() //seçilen dropdown item ı böyle alacağız

        handleDropdownMenues()

    }

    fun handleDropdownMenues(){
        if(isCustomerOrHouseOwner == "customer"){ //customer'ın formu dolduruluyorsa
            properTiesFormObject.addProperty("luxury",spinnerLuxuryImportance.selectedItem.toString())//customer'a eklenecek
            properTiesFormObject.addProperty("priceRange",spinnerPriceRange.selectedItem.toString())//customer'a eklenecek
        }
        else{ //houseOwner'ın formu dolduruluyorsa
            properTiesFormObject.addProperty("luxury",spinnerLuxuryImportance.selectedItem.toString())//houseOwner'a eklenecek
            properTiesFormObject.addProperty("priceRange",spinnerPriceRange.selectedItem.toString())//houseOwner'a eklenecek
        }
    }

    fun sendForm(view: View){
        val userId = intent.getIntExtra("userId",1)

        if(userId != null){//always true gelir farkındayım
            val intent = Intent(this@PropertiesFormActivity,MainPageActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }
    }

    fun useSmokeHandler(view:View){ //radio button
        if(radioUseSmokeGroup.radioYesSmokeButton.isChecked){
            if(isCustomerOrHouseOwner == "customer"){ //sigara içiyor
                //properTiesFormObject.addProperty("customerGender",radioGroup.radioMaleButton.text.toString()) //customer sigara içiyor
            }else{
                //properTiesFormObject.addProperty("ownerGender",radioGroup.radioMaleButton.text.toString()) //houseOwner sigara içiyor
            }
        }else if(radioUseSmokeGroup.radioNoSmokeButton.isChecked){
            if(isCustomerOrHouseOwner == "customer"){ //sigara içmiyor
                //properTiesFormObject.addProperty("customerGender",radioGroup.radioMaleButton.text.toString()) //customer sigara içmiyor
            }else{
                //properTiesFormObject.addProperty("ownerGender",radioGroup.radioMaleButton.text.toString()) //houseOwner sigara içmiyor
            }
        }else{
            Toast.makeText(this@PropertiesFormActivity,"UsingSmoke Error",Toast.LENGTH_LONG).show()
        }
    }
    fun havePetHandler(view:View){//radio button
        if(radioHavePetGroup.radioYesPetButton.isChecked){ //evcil hayvan var
            if(isCustomerOrHouseOwner == "customer"){
                //properTiesFormObject.addProperty("customerGender",radioGroup.radioMaleButton.text.toString()) // customer'ın evcil hayvanı var
            }else{
                //properTiesFormObject.addProperty("ownerGender",radioGroup.radioMaleButton.text.toString()) // houseOwner'ın evcil hayvanı var
            }
        }else if(radioHavePetGroup.radioNoPetButton.isChecked){ //evcil hayvan yok
            if(isCustomerOrHouseOwner == "customer"){
                //properTiesFormObject.addProperty("customerGender",radioGroup.radioMaleButton.text.toString()) // customer'ın evcil hayvanı yok
            }else{
                //properTiesFormObject.addProperty("ownerGender",radioGroup.radioMaleButton.text.toString()) // houseOwner'ın evcil hayvanı yok
            }
        }else{
            Toast.makeText(this@PropertiesFormActivity,"HavePet Error",Toast.LENGTH_LONG).show()
        }
    }

}