package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_house_single_profile.*
import kotlinx.android.synthetic.main.activity_my_house.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MyHouseActivity : AppCompatActivity() {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var house : HouseModel? = null
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_house)

        compositeDisposable = CompositeDisposable()

        loadData()
    }

    private fun loadData(){
        val intent = intent

        val houseOwnerId = intent.getIntExtra("houseOwnerId",1)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseOwnerAPI::class.java)


        compositeDisposable?.add(retrofit.getHouseOfHouseOwner(houseOwnerId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    @SuppressLint("SetTextI18n")
    private fun handleResponse(houseModel: HouseModel){
        house = houseModel

        houseOwnersHouseAddress.text = "Adres : " + house!!.houseAddress
        houseOwnersHouseType.text = "Ev Tipi : ${house!!.houseType}"
        houseOwnersHouseHeatResource.text = "Yakıt Türü : " + house!!.heatResource
        houseOwnersHouseInternetPaved.text = "İnternet Dahil: " + house!!.internetPaved
        houseOwnersHouseFloor.text = "Kat : " + house!!.floor.toString()
        houseOwnersHouseRent.text = "Kira: " + house!!.rent.toString()

        homeDwellers1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString

    }

    fun onUpdateHouseClick(view:View){
        val intent = Intent(this@MyHouseActivity, UpdateHouseActivity::class.java)
        house?.let { intent.putExtra("houseId", it.houseId) }
        startActivity(intent)
    }

    fun goToOwnerProfile1(view:View){
        Toast.makeText(this,"Clicked ${house!!.owners[0].get("ownerId").asInt}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MyHouseActivity , SingleProfileActivity::class.java)
        intent.putExtra("fromHouseOwner","houseOwnerProfile")
        intent.putExtra("houseOwnerId",house!!.owners[0].get("ownerId").asInt)
        startActivity(intent)

    }

    fun onDeleteHouseClick(view:View){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Ev Silme Onay")
        alertDialogBuilder.setMessage("İşlemi gerçekleştirmek istediğinize emin misiniz?")

        alertDialogBuilder.setPositiveButton("Evet") { dialog, which ->
            Toast.makeText(this,"Ev silindi", Toast.LENGTH_SHORT).show()
        }

        alertDialogBuilder.setNegativeButton("Hayır") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    //Henüz burayı kullanmayacağız sadece kullanılabilme ihtimaline karşın ekledim
    fun onClosePageClick(view:View){
        val intent = Intent(this@MyHouseActivity, MainPageActivity::class.java)
        intent.putExtra("fromMyHouse","myHouse")
        intent.putExtra("houseOwnerId",house!!.owners[0].get("ownerId").asInt)
        startActivity(intent)

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
            val intent = Intent(this@MyHouseActivity,finish()::class.java)
            startActivity(intent)
            //onDestroy()
        }
        else{
            Toast.makeText(this@MyHouseActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

}