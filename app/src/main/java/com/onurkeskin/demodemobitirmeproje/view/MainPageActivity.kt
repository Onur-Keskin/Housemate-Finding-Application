package com.onurkeskin.demobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.adapter.HousesRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import com.onurkeskin.demodemobitirmeproje.view.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_houses.*
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_main_page.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainPageActivity : AppCompatActivity(){
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var houseModels : ArrayList<HouseModel>? = null
    private var housesRecyclerViewAdapter : HousesRecyclerViewAdapter? = null

    private var compositeDisposable : CompositeDisposable? = null
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
        else if(item.itemId == R.id.profile){
            val customerOrOwner = intent.getStringExtra("customerOrOwner")
            println("customerOrOwner : $customerOrOwner")
            if(customerOrOwner == "houseOwner"){//houseOwner giriş yapmışsa
                val houseOwnerId = intent.getIntExtra("ownerId",0)
                //println("houseOwnerId : $houseOwnerId")
                if(houseOwnerId != 0){
                    intent = Intent(this@MainPageActivity, SingleProfileActivity::class.java)
                    intent.putExtra("fromMainPageHouseOwner","houseOwnerLoginProfile")
                    intent.putExtra("houseOwnerLoginId",houseOwnerId)
                    startActivity(intent)
                }

            }else{ //customer giriş yapmışsa
                val userId = intent.getIntExtra("userId",1)
                intent = Intent(this@MainPageActivity, SingleProfileActivity::class.java)//Şuanlık boş safyaya gider. username e göre unique kullanıcıyı alacak olan bir endpoint lazım
                intent.putExtra("fromMainPageCustomer","customerLoginProfile")
                intent.putExtra("customerLoginId",userId)
                startActivity(intent)
            }
            //onDestroy()
        }
        else{
            Toast.makeText(this@MainPageActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
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
        val houseOwnerId = intent.getIntExtra("ownerId",0)
        val intent = Intent(this@MainPageActivity, CustomerWhoLikeHouseActivity::class.java)
        intent.putExtra("houseOwnerId",houseOwnerId)//houseOwner' ın id'si gönderilmeli
        startActivity(intent)
    }
    fun gotoMyProfile(view:View){

        val customerOrOwner = intent.getStringExtra("customerOrOwner")
        println("customerOrOwner : $customerOrOwner")
        if(customerOrOwner == "houseOwner"){
            val houseOwnerId = intent.getIntExtra("ownerId",0)
            //println("houseOwnerId : $houseOwnerId")
            if(houseOwnerId != 0){
                intent = Intent(this@MainPageActivity, SingleProfileActivity::class.java)
                intent.putExtra("fromMainPageHouseOwner","houseOwnerLoginProfile")
                intent.putExtra("houseOwnerLoginId",houseOwnerId)
                startActivity(intent)
            }

        }else{ //customer giriş yapmışssa
            val userId = intent.getIntExtra("userId",1)
            intent = Intent(this@MainPageActivity, SingleProfileActivity::class.java)//Şuanlık boş safyaya gider. username e göre unique kullanıcıyı alacak olan bir endpoint lazım
            intent.putExtra("fromMainPageCustomer","customerLoginProfile")
            intent.putExtra("customerLoginId",userId)
            startActivity(intent)
        }

    }



}