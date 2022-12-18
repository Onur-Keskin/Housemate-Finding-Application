package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_house_single_profile.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HouseSingleProfileActivity : AppCompatActivity() {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var house : HouseModel? = null
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_single_profile)

        compositeDisposable = CompositeDisposable()

        //bindRecyclerView()

        loadData()
    }

    private fun loadData(){
        val intent = intent

        val id = intent.getIntExtra("houseId",1)
        val houseId : String? = "$id"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseAPI::class.java)

        compositeDisposable?.add(retrofit.getHouseById(houseId)
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(houseModel: HouseModel){
        house = houseModel
        if(house != null){
            singleHouseId.text = "Id : " + house!!.houseId.toString()
            singleHouseAddress.text = "Adress : " + house!!.houseAddress
            singleHouseCountOfBathroom.text = "Count of Bathrom : " + house!!.countOfBathroom.toString()
            singleHouseCountOfBedroom.text = "Count of Bedroom : " + house!!.countOfBedroom.toString()
            singleHouseCountOfSalon.text = "Count of Salon : " + house!!.countOfSalon.toString()
            singleHouseCountOfOwner.text = "Count of Owner : " + house!!.countOfOwner.toString()
            singleHouseHeatResource.text = "Count of Heat Resource : " + house!!.heatResource
            singleHouseInternetPaved.text = "Internet Paved?  : " + house!!.internetPaved
            singleHouseFloor.text = "Floor : " + house!!.floor.toString()
            singleHouseRent.text = "Rent: " + house!!.rent.toString()
        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }
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
            val intent = Intent(this@HouseSingleProfileActivity,finish()::class.java)
            startActivity(intent)
            //onDestroy()
        }
        else{
            Toast.makeText(this@HouseSingleProfileActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    /*
    private fun bindRecyclerView()
    {
        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        houseSingleProfileRecyclerView.layoutManager = layoutManager

    }

     */
}