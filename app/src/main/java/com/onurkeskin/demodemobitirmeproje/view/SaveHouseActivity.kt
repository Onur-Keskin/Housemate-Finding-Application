package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.onurkeskin.demobitirmeproje.R

class SaveHouseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_house)
    }

    fun toMainPage(view:View){
        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)
        val houseOwnerName = intent.getStringExtra("registeredUser-Name")

        if(houseOwnerId != 0){
            val intent = Intent(this@SaveHouseActivity , PropertiesFormActivity::class.java)
            intent.putExtra("houseOwnerId", houseOwnerId)
            intent.putExtra("fromRegisterPage","houseOwner")
            intent.putExtra("registeredUser-Name", houseOwnerName)
            startActivity(intent)
            //finish()
        }else{
            Toast.makeText(this@SaveHouseActivity,"There is a error in SaveHouseActivity",Toast.LENGTH_LONG).show()
        }
    }
}