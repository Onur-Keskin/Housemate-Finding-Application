package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.adapter.HousesRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.adapter.LikedHousesRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_houses.*
import kotlinx.android.synthetic.main.activity_liked_houses.*
import kotlinx.android.synthetic.main.activity_single_profile.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class LikedHousesActivity : AppCompatActivity() ,LikedHousesRecyclerViewAdapter.Listener{

    private var likedHousesRecyclerViewAdapter : LikedHousesRecyclerViewAdapter? = null
    private lateinit var likedHouses : ArrayList<HouseModel>
    private var customerId : Int = 0

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_houses)

        compositeDisposable = CompositeDisposable()

        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        likedHousesRecyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData(){
        customerId = intent.getIntExtra("customerId",0)

        if(customerId != 0){
            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseAPI::class.java)


            compositeDisposable?.add(retrofit.getAllHousesOfOneCustomer(customerId.toString())
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleResponse))
        }

    }

    private fun handleResponse(likedHouseList: List<HouseModel>){
        likedHouses = ArrayList(likedHouseList)

        likedHouses?.let {
            likedHousesRecyclerViewAdapter = LikedHousesRecyclerViewAdapter(it,this@LikedHousesActivity)
            likedHousesRecyclerView.adapter = likedHousesRecyclerViewAdapter
        }
    }

    override fun onHouseItemClick(houseModel: HouseModel) {
        Toast.makeText(this,"Clicked : ${houseModel.houseId}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@LikedHousesActivity,HouseSingleProfileActivity::class.java) //Evin detay bilgilerinin görüntüleneceği sayfaya yönlenecek.
        intent.putExtra("houseId",houseModel.houseId)
        intent.putExtra("fromLikedHouse","liked")
        intent.putExtra("customerId",customerId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflater xml ilemkodu bağlama
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.liked_houses_menu,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val xD = "Ayalarlar sayfasına gider"
        }
        else if(item.itemId == R.id.logout){
            val intent = Intent(this@LikedHousesActivity,MainActivity::class.java)
            startActivity(intent)
            finish()

        } else{
            Toast.makeText(this@LikedHousesActivity,"Some Errors Happened in LikedHouseActivity", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

}