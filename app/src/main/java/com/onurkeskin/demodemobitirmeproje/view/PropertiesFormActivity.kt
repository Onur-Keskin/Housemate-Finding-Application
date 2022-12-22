package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import kotlinx.android.synthetic.main.content_main.*

class PropertiesFormActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_properties_form)

        val luxuryImportanceSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.luxuryImportance,android.R.layout.simple_spinner_item)
        luxuryImportanceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerLuxuryImportance.adapter = luxuryImportanceSpinnerAdapter

        val priceRangeSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.priceRange,android.R.layout.simple_spinner_item)
        priceRangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerLPriceRange.adapter = priceRangeSpinnerAdapter

        val registeredUserName = intent.getStringExtra("registeredUser-Name") // yeni kayıt olan kullanıcının adı
        welcomeText.text = "Hoşgeldin! ${registeredUserName}! Seni biraz tanıyalım :) "


    }

    fun sendForm(view: View){
        val userId = intent.getIntExtra("userId",1)
        val isFromRegisterPage = intent.getStringExtra("fromRegisterPage")

        if(userId != null){//always true gelir farkındayım
            val intent = Intent(this@PropertiesFormActivity,MainPageActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }
    }

    fun useSmokeHandler(view:View){

    }
    fun havePetHandler(view:View){

    }

}