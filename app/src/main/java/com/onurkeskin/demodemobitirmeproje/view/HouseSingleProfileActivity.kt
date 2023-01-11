package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.HouseModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
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
    private val customerLikeHomeObject = JsonObject()
    private var likedHouse: JsonObject? = null

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

    @SuppressLint("SetTextI18n")
    private fun handleResponse(houseModel: HouseModel){
        house = houseModel
        if(house != null){ //always true geliyor farkındayım
            val isHouseLiked = intent.getStringExtra("fromLikedHouse")

            if(isHouseLiked == "liked"){ //Ev beğenilmişse
                singleHouseId.text = "Id : " + house!!.houseId.toString()
                singleHouseAddress.text = "Adress : " + house!!.houseAddress
                singleHouseType.text = "Ev Tipi : ${house!!.houseType}"
                //singleHouseCountOfBathroom.text = "Count of Bathrom : " + house!!.countOfBathroom.toString()
                //singleHouseCountOfBedroom.text = "Count of Bedroom : " + house!!.countOfBedroom.toString()
                //singleHouseCountOfSalon.text = "Count of Salon : " + house!!.countOfSalon.toString()
                singleHouseCountOfOwner.text = "Count of Owner : " + house!!.owners.size
                singleHouseHeatResource.text = "Count of Heat Resource : " + house!!.heatResource
                singleHouseInternetPaved.text = "Internet Paved?  : " + house!!.internetPaved
                singleHouseFloor.text = "Floor : " + house!!.floor.toString()
                singleHouseRent.text = "Rent: " + house!!.rent.toString()
                likeButton.setImageResource(R.drawable.ic_baseline_liked)

                if(house!!.owners.size == 1){
                    singleHouseOwner1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString
                }
                else if(house!!.owners.size == 2){
                    singleHouseOwner1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString
                    singleHouseOwner2.text = house!!.owners[1].get("ownerName").asString + " " +house!!.owners[1].get("ownerSurname").asString
                }
                else if(house!!.owners.size == 3){
                    singleHouseOwner1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString
                    singleHouseOwner2.text = house!!.owners[1].get("ownerName").asString + " " +house!!.owners[1].get("ownerSurname").asString
                    singleHouseOwner3.text = house!!.owners[2].get("ownerName").asString + " " +house!!.owners[2].get("ownerSurname").asString
                }
            }
            else{
                singleHouseId.text = "Id : " + house!!.houseId.toString()
                singleHouseAddress.text = "Adress : " + house!!.houseAddress
                singleHouseType.text = "Ev Tipi : ${house!!.houseType}"
                //singleHouseCountOfBathroom.text = "Count of Bathrom : " + house!!.countOfBathroom.toString()
                //singleHouseCountOfBedroom.text = "Count of Bedroom : " + house!!.countOfBedroom.toString()
                //singleHouseCountOfSalon.text = "Count of Salon : " + house!!.countOfSalon.toString()
                singleHouseCountOfOwner.text = "Count of Owner : " + house!!.owners.size
                singleHouseHeatResource.text = "Count of Heat Resource : " + house!!.heatResource
                singleHouseInternetPaved.text = "Internet Paved?  : " + house!!.internetPaved
                singleHouseFloor.text = "Floor : " + house!!.floor.toString()
                singleHouseRent.text = "Rent: " + house!!.rent.toString()

                if(house!!.owners.size == 1){
                    singleHouseOwner1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString
                }
                else if(house!!.owners.size == 2){
                    singleHouseOwner1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString
                    singleHouseOwner2.text = house!!.owners[1].get("ownerName").asString + " " +house!!.owners[1].get("ownerSurname").asString
                }
                else if(house!!.owners.size == 3){
                    singleHouseOwner1.text = house!!.owners[0].get("ownerName").asString + " " +house!!.owners[0].get("ownerSurname").asString
                    singleHouseOwner2.text = house!!.owners[1].get("ownerName").asString + " " +house!!.owners[1].get("ownerSurname").asString
                    singleHouseOwner3.text = house!!.owners[2].get("ownerName").asString + " " +house!!.owners[2].get("ownerSurname").asString
                }
            }
        }

    }

    fun goToOwnerProfile1(view:View){
        Toast.makeText(this,"Clicked ${house!!.owners[0].get("ownerId").asInt}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@HouseSingleProfileActivity , SingleProfileActivity::class.java)
        intent.putExtra("fromHouseOwner","houseOwnerProfile")
        intent.putExtra("houseOwnerId",house!!.owners[0].get("ownerId").asInt)
        startActivity(intent)

    }
    fun goToOwnerProfile2(view:View){
        Toast.makeText(this,"Clicked ${house!!.owners[1].get("ownerId").asInt}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@HouseSingleProfileActivity , SingleProfileActivity::class.java)
        intent.putExtra("fromHouseOwner","houseOwnerProfile")
        intent.putExtra("houseOwnerId",house!!.owners[1].get("ownerId").asInt)
        startActivity(intent)

    }
    fun goToOwnerProfile3(view:View){
        Toast.makeText(this,"Clicked ${house!!.owners[2].get("ownerId").asInt}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@HouseSingleProfileActivity , SingleProfileActivity::class.java)
        intent.putExtra("fromHouseOwner","houseOwnerProfile")
        intent.putExtra("houseOwnerId",house!!.owners[2].get("ownerId").asInt)
        startActivity(intent)

    }

    fun customerLikeHouse(view:View){
        val customerId = intent.getIntExtra("customerId",1)
        customerLikeHomeObject.addProperty("customerId",customerId)
        customerLikeHomeObject.addProperty("houseId",house!!.houseId)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CustomerAPI::class.java)

        compositeDisposable?.add(retrofit.customerLikeHouse(customerLikeHomeObject)
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleLikeResponse))
    }

    private fun handleLikeResponse(likedHouseResponseModel: JsonObject){
        likedHouse = likedHouseResponseModel
        val intent = Intent(this@HouseSingleProfileActivity, HousesActivity::class.java)
        intent.putExtra("customerId", likedHouse!!.get("customer").asJsonObject.get("customerId").asInt)
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
            val intent = Intent(this@HouseSingleProfileActivity,finish()::class.java)
            startActivity(intent)
            //onDestroy()
        }
        else{
            Toast.makeText(this@HouseSingleProfileActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

}