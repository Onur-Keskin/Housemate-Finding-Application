package com.onurkeskin.demobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.view.CustomerProfilesActivity
import com.onurkeskin.demodemobitirmeproje.view.HouseOwnersProfilesActivity
import com.onurkeskin.demodemobitirmeproje.view.HousesActivity
import com.onurkeskin.demodemobitirmeproje.view.SingleProfileActivity
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_main_page.view.*

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflater xml ilemkodu bağlama
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.category_menu,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val xD = "Ayalarlar sayfasına gider"
        }
        else if(item.itemId == R.id.logout){
            val intent = Intent(this@MainPageActivity,finish()::class.java)
            startActivity(intent)
            //onDestroy()
        }
        else{
            Toast.makeText(this@MainPageActivity,"Some Errors Happened",Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }




    fun bringHouses(view: View){
        val intent = Intent(this@MainPageActivity, HousesActivity::class.java)
        startActivity(intent)
    }

    fun bringCustomers(view: View){
        val intent = Intent(this@MainPageActivity, CustomerProfilesActivity::class.java)
        startActivity(intent)
    }

    fun bringHouseOwners(view: View){
        val intent = Intent(this@MainPageActivity, HouseOwnersProfilesActivity::class.java)
        startActivity(intent)
    }
    fun gotoMyProfile(view:View){
        val userId = intent.getIntExtra("userId",1)
        intent = Intent(this@MainPageActivity, SingleProfileActivity::class.java)//Şuanlık boş safyaya gider. username e göre unique kullanıcıyı alacak olan bir endpoint lazım
        intent.putExtra("fromMainPage","userLoginProfile")
        intent.putExtra("userLoginId",userId)
        startActivity(intent)
    }



}