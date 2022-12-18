package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.adapter.HouseOwnerRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_house_owners_profiles.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HouseOwnersProfilesActivity : AppCompatActivity(), HouseOwnerRecyclerViewAdapter.Listener {
    private val BASE_URL = "http://192.168.1.21:8080/" //IPv4 Address. . . . . . . . . . . : 192.168.1.21
    private var houseOwnerModels : ArrayList<HouseOwnerModel>? = null
    private var houseOwnerRecyclerViewAdapter : HouseOwnerRecyclerViewAdapter? = null

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_owners_profiles)

        compositeDisposable = CompositeDisposable()

        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        houseOwnersRecyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseOwnerAPI::class.java)



        compositeDisposable?.add(retrofit.getOwners()
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))
    }
    private fun handleResponse(houseOwnerList: List<HouseOwnerModel>){
        houseOwnerModels = ArrayList(houseOwnerList)
        houseOwnerModels?.let {
            houseOwnerRecyclerViewAdapter = HouseOwnerRecyclerViewAdapter(it,this@HouseOwnersProfilesActivity)
            houseOwnersRecyclerView.adapter = houseOwnerRecyclerViewAdapter
        }
    }

    override fun onItemClick(houseOwnerModel: HouseOwnerModel) {
        //Toast.makeText(this,"Clicked : ${houseOwnerModel.ownerId}",Toast.LENGTH_LONG).show()
        val intent = Intent(this@HouseOwnersProfilesActivity,SingleProfileActivity::class.java)
        intent.putExtra("fromHouseOwner","houseOwnerProfile")
        intent.putExtra("houseOwnerId",houseOwnerModel.ownerId)
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
            val intent = Intent(this@HouseOwnersProfilesActivity,finish()::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this@HouseOwnersProfilesActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }


}