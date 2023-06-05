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
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_houses.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HousesActivity : AppCompatActivity(),HousesRecyclerViewAdapter.Listener {

    private val BASE_URL = "http://192.168.1.21:8080/"
    private var houseModels : ArrayList<HouseModel>? = null
    private var houseWithClassModel : ArrayList<HouseModel>? = null
    private var housesRecyclerViewAdapter : HousesRecyclerViewAdapter? = null
    private val customerIdObject = JsonObject()
    private  lateinit var customerWithClassObject : JsonObject

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houses)

        compositeDisposable = CompositeDisposable()

        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        housesRecyclerView.layoutManager = layoutManager

        loadData()

    }

    private fun loadData(){
        val customerId = intent.getIntExtra("userId",0)
        //println("customerId : ${customerId}")
        if(customerId != 0){
            customerIdObject.addProperty("customerId",customerId)
            //println(customerIdObject)
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            compositeDisposable?.add(retrofit.getOneCustomerProperties(customerIdObject)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleCustomerClassResponse))
        }else{
            Toast.makeText(this,"Error during the get houses by class operation",Toast.LENGTH_SHORT).show()
        }

        /*
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseAPI::class.java)


        compositeDisposable?.add(retrofit.getHouses()
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))

         */

    }
    /*
    private fun handleResponse(houseList: List<HouseModel>){
        houseModels = ArrayList(houseList)

        houseModels?.let {
            housesRecyclerViewAdapter = HousesRecyclerViewAdapter(it,this@HousesActivity)
            housesRecyclerView.adapter = housesRecyclerViewAdapter
        }
    }

     */

    private fun handleCustomerClassResponse(customerWithClass : JsonObject){
        customerWithClassObject = customerWithClass
        val customerClass = customerWithClassObject.get("classOfCustomer").asString

        if(customerClass != null){ //Customer ın class'ına göre olan evleri getirecek bize
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseAPI::class.java)

            compositeDisposable?.add(retrofit.getHousesByClass(customerClass)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleGetHousesByClassResponse))
        }
    }

    private fun handleGetHousesByClassResponse(houseByClassList: List<HouseModel>){
        houseWithClassModel = ArrayList(houseByClassList)

        houseWithClassModel?.let {
            housesRecyclerViewAdapter = HousesRecyclerViewAdapter(it,this@HousesActivity)
            housesRecyclerView.adapter = housesRecyclerViewAdapter
        }
    }

    override fun onHouseItemClick(houseModel: HouseModel) {
        val customerId = intent.getIntExtra("userId",0)
        Toast.makeText(this,"Clicked : ${houseModel.houseId}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@HousesActivity,HouseSingleProfileActivity::class.java) //Evin detay bilgilerinin görüntüleneceği sayfaya yönlenecek.
        intent.putExtra("houseId",houseModel.houseId)
        intent.putExtra("customerId",customerId)
        startActivity(intent)
    }

    override fun onHouseOwnerItemClick(houseModel: HouseModel) {
        val intent = Intent(this@HousesActivity,SingleProfileActivity::class.java)
        intent.putExtra("fromHouseOwner","houseOwnerProfile")
        //println("HouseOwnerId : " + houseModel.owners[0].get("ownerId"))
        intent.putExtra("houseOwnerId", houseModel.owners[0].get("ownerId").asInt)
        startActivity(intent)
    }

    override fun onCustomerItemClick(houseModel: HouseModel) {
        val intent = Intent(this@HousesActivity,SingleProfileActivity::class.java)
        intent.putExtra("fromCustomer","customerProfile")
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
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
            val intent = Intent(this@HousesActivity,finish()::class.java)
            startActivity(intent)
            //onDestroy()
        }
        else if(item.itemId == R.id.profile){
            val customerOrOwner = intent.getStringExtra("customerOrOwner")
            //println("customerOrOwner : $customerOrOwner")
            if(customerOrOwner == "houseOwner"){
                val houseOwnerId = intent.getIntExtra("ownerId",0)
                //println("houseOwnerId : $houseOwnerId")
                if(houseOwnerId != 0){
                    intent = Intent(this@HousesActivity, SingleProfileActivity::class.java)
                    intent.putExtra("fromMainPageHouseOwner","houseOwnerLoginProfile")
                    intent.putExtra("houseOwnerLoginId",houseOwnerId)
                    startActivity(intent)
                }

            }else{ //customer giriş yapmışssa
                val userId = intent.getIntExtra("userId",1)
                intent = Intent(this@HousesActivity, SingleProfileActivity::class.java)//Şuanlık boş safyaya gider. username e göre unique kullanıcıyı alacak olan bir endpoint lazım
                intent.putExtra("fromMainPageCustomer","customerLoginProfile")
                intent.putExtra("customerLoginId",userId)
                startActivity(intent)
            }
            //onDestroy()
        }
        else if(item.itemId == R.id.likedHouses){ //Customer beğendiği evleri görüntüleyeceği sayfasına gidecek
            val customerId = intent.getIntExtra("userId",0)
            if(customerId != 0){
                val intent = Intent(this@HousesActivity,LikedHousesActivity::class.java)
                intent.putExtra("customerId",customerId)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@HousesActivity,"Some Errors Happened routing to Liked Houses Page", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this@HousesActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }



}