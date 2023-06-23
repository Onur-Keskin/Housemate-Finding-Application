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
import com.onurkeskin.demodemobitirmeproje.adapter.CustomerRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.adapter.CustomerWhoLikeHouseRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.adapter.HousesRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.adapter.LikedHousesRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_customer_who_like_house.*
import kotlinx.android.synthetic.main.activity_houses.*
import kotlinx.android.synthetic.main.activity_liked_houses.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CustomerWhoLikeHouseActivity : AppCompatActivity() ,CustomerWhoLikeHouseRecyclerViewAdapter.Listener{

    private var customerWhoLikeHouseRecyclerViewAdapter : CustomerWhoLikeHouseRecyclerViewAdapter? = null
    private lateinit var customersWhoLikes : ArrayList<CustomerModel>

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_who_like_house)

        compositeDisposable = CompositeDisposable()

        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        customerWhoLikeHosueRecyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData(){
        val houseOwnerId = intent.getIntExtra("houseOwnerId",1)

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)


            compositeDisposable?.add(retrofit.getHouseOfHouseOwner(houseOwnerId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bringHouseIdHandleResponse))


    }

    private fun bringHouseIdHandleResponse(houseModel: HouseModel){
        val houseId = houseModel.houseId

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalVariables.globalBASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseOwnerAPI::class.java)


        compositeDisposable?.add(retrofit.getAllCustomersOfOneHouse(houseId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::bringCustomersHandleResponse))
    }

    private fun bringCustomersHandleResponse(customersWhoLikeList: List<CustomerModel>){
        customersWhoLikes = ArrayList(customersWhoLikeList)

        customersWhoLikes?.let {
            customerWhoLikeHouseRecyclerViewAdapter = CustomerWhoLikeHouseRecyclerViewAdapter(it,this@CustomerWhoLikeHouseActivity)
            customerWhoLikeHosueRecyclerView.adapter = customerWhoLikeHouseRecyclerViewAdapter
        }
    }

    override fun onCustomerItemClick(customerModel: CustomerModel) {
        Toast.makeText(this,"Clicked : ${customerModel.customerId}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@CustomerWhoLikeHouseActivity,SingleProfileActivity::class.java) //Evin detay bilgilerinin görüntüleneceği sayfaya yönlenecek.
        intent.putExtra("customerId",customerModel.customerId)
        intent.putExtra("fromCustomerWhoLikeHouse","customerWhoLikesHouse")
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.liked_houses_menu,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val xD = "Ayalarlar sayfasına gider"
        }
        else if(item.itemId == R.id.logout){
            val intent = Intent(this@CustomerWhoLikeHouseActivity,HouseOwnerLoginActivity::class.java)
            startActivity(intent)
            finish()

        } else{
            Toast.makeText(this@CustomerWhoLikeHouseActivity,"Some Errors Happened in CustomerWhoLikeHouseActivity", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }


}