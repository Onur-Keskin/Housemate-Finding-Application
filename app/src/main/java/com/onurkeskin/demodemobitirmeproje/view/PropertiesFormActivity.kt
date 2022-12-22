package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity

class PropertiesFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_properties_form)
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
}