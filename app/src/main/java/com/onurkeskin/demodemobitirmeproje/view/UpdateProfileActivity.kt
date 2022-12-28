package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_single_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UpdateProfileActivity : AppCompatActivity() {

    private val BASE_URL = "http://192.168.1.21:8080/"
    private var compositeDisposable : CompositeDisposable? = null
    private var customerUpdateProfileResponseModel: JsonObject? = null
    private var houseOwnerUpdateProfileResponseModel: JsonObject? = null
    private val customerObject = JsonObject()
    private val houseOwnerObject = JsonObject()
    private var customerModel : CustomerModel? = null
    private var houseOwnerModel : HouseOwnerModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        compositeDisposable = CompositeDisposable()

        loadData()
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadData(){
        val intent = intent
        val customerId = intent.getIntExtra("customerId",0)
        val houseOwnerId = intent.getIntExtra("ownerId",0)

        if (customerId != 0){
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)

                compositeDisposable?.add(retrofit.getCustomerById(customerId.toString())
                    .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                    .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                    .subscribe(this::bringCustomerHandleResponse))
        }
        else if(houseOwnerId != 0){
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            compositeDisposable?.add(retrofit.getOwnerById(houseOwnerId.toString())
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::bringHouseOwnerHandleResponse))
        }


    }

    private fun bringCustomerHandleResponse(customer: CustomerModel){ //Update sayfasına customerın bilgisini getirecek
        customerModel = customer

        if(customerModel != null){
            updateTextCustomerOrHouseOwner.text = "Customer"
            updateEditTextName.setText(customerModel!!.customerName)
            updateEditTextSurname.setText(customerModel!!.customerSurname)
            updateEditTextUsername.setText(customerModel!!.customerUsername)
            updateEditTextAge.setText(customerModel!!.customerAge.toString())
            updateEditTextHomeTown.setText(customerModel!!.customerHometown)
            updateEditTextDepartment.setText(customerModel!!.customerDepartment)
            updateEditTextPassword.setText(customerModel!!.customerPassword)
            updateEditTextGrade.setText(customerModel!!.customerGrade.toString())
            updateEditTextPhone.setText(customerModel!!.customerPhone)
            updateEditTextEmail.setText(customerModel!!.customerEmail)
            updateEditTextGender.setText(customerModel!!.customerGender) //Burası radio button yapılacak
        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }

    }

    private fun bringHouseOwnerHandleResponse(houseOwner: HouseOwnerModel){ //Update sayfasına houseOwner'ın bilgisini getirecek
        houseOwnerModel = houseOwner
        //println(houseOwnerModel)
        if(houseOwnerModel != null){
            updateTextCustomerOrHouseOwner.text = "House Owner"
            updateEditTextName.setText(houseOwnerModel!!.ownerName)
            updateEditTextSurname.setText(houseOwnerModel!!.ownerSurname)
            updateEditTextUsername.setText(houseOwnerModel!!.ownerUsername)
            updateEditTextAge.setText(houseOwnerModel!!.ownerAge.toString())
            updateEditTextHomeTown.setText(houseOwnerModel!!.ownerHometown)
            updateEditTextDepartment.setText(houseOwnerModel!!.ownerDepartment)
            //updateEditTextPassword.hint = houseOwnerModel!!.customerPassword //burayı modelime eklicem
            updateEditTextGrade.setText(houseOwnerModel!!.ownerGrade.toString())
            updateEditTextPhone.setText(houseOwnerModel!!.ownerPhone)
            updateEditTextEmail.setText(houseOwnerModel!!.ownerMail)
            updateEditTextGender.setText(houseOwnerModel!!.ownerGender)  //Burası radio button yapılacak
        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }

    }

    fun updateProfile(view:View){

        val intent = intent
        val customerId = intent.getIntExtra("customerId",0)
        val houseOwnerId = intent.getIntExtra("ownerId",0)

        if(customerId != 0){ //customer bilgilerini güncelle
            updateTextCustomerOrHouseOwner.text = "Customer"
            customerObject.addProperty("customerId",customerId)
            customerObject.addProperty("customerName",updateEditTextName.text.toString())
            customerObject.addProperty("customerUsername",updateEditTextUsername.text.toString())
            customerObject.addProperty("customerSurname",updateEditTextSurname.text.toString())
            customerObject.addProperty("customerAge",updateEditTextAge.text.toString().toInt())
            customerObject.addProperty("customerHometown",updateEditTextHomeTown.text.toString())
            customerObject.addProperty("customerDepartment",updateEditTextDepartment.text.toString())
            customerObject.addProperty("customerPassword",updateEditTextPassword.text.toString())
            customerObject.addProperty("customerGrade",updateEditTextGrade.text.toString().toInt())
            customerObject.addProperty("customerPhone",updateEditTextPhone.text.toString())
            customerObject.addProperty("customerEmail",updateEditTextEmail.text.toString())
            customerObject.addProperty("customerGender",updateEditTextGender.text.toString())

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)

            compositeDisposable?.add(retrofit.updateOneCustomer(customerObject)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleCustomerUpdateClickResponse))
        }
        else if(houseOwnerId != 0){ //houseOwner bilgilerini güncelle
            updateTextCustomerOrHouseOwner.text = "HouseOwner"
            houseOwnerObject.addProperty("houseOwnerId",houseOwnerId)
            houseOwnerObject.addProperty("houseOwnerName",updateEditTextName.text.toString())
            houseOwnerObject.addProperty("houseOwnerSurname",updateEditTextSurname.text.toString())
            houseOwnerObject.addProperty("houseOwnerUsername",updateEditTextUsername.text.toString())
            houseOwnerObject.addProperty("houseOwnerAge",updateEditTextAge.editableText.toString().toInt())
            houseOwnerObject.addProperty("houseOwnerHometown",updateEditTextHomeTown.text.toString())
            houseOwnerObject.addProperty("houseOwnerDepartment",updateEditTextDepartment.text.toString())
            //houseOwnerObject.addProperty("ownerPassword",updateEditTextPassword.text.toString())
            houseOwnerObject.addProperty("houseOwnerGrade",updateEditTextGrade.editableText.toString().toInt())
            houseOwnerObject.addProperty("houseOwnerPhone",updateEditTextPhone.text.toString())
            houseOwnerObject.addProperty("houseOwnerEmail",updateEditTextEmail.text.toString()) //burası ownermail olabilir
            houseOwnerObject.addProperty("houseOwnerGender",updateEditTextGender.text.toString())
            houseOwnerObject.addProperty("houseId",1)//burası güncellenecek

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            compositeDisposable?.add(retrofit.updateOneHouseOwner(houseOwnerObject)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleHouseOwnerUpdateClickResponse))


        }
        else{
            Toast.makeText(this@UpdateProfileActivity,"HATA" , Toast.LENGTH_LONG).show()
        }
    }

    private fun handleCustomerUpdateClickResponse(userUpdate: JsonObject){
        customerUpdateProfileResponseModel = userUpdate
        //println(userUpdateProfileResponseModel)

        if(customerUpdateProfileResponseModel!!.get("customerId").asInt != null ){//password de kontrol edilecek ama önce api de olması şart
            val intent = Intent(this@UpdateProfileActivity , MainPageActivity::class.java)
            intent.putExtra("userId", customerUpdateProfileResponseModel!!.get("customerId").asInt)
            intent.putExtra("customerOrOwner","customer")
            //intent.putExtra("fromRegisterPage","firstLogin")
            //intent.putExtra("registeredUser-Name",userRegisterResponseModel!!.get("customerName").toString())
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@UpdateProfileActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleHouseOwnerUpdateClickResponse(houseOwnerUpdate: JsonObject){
        houseOwnerUpdateProfileResponseModel = houseOwnerUpdate
        println("houseOwnerUpdateProfileResponseModel : ")
        println(houseOwnerUpdateProfileResponseModel)
        val houseOwnerId = houseOwnerUpdateProfileResponseModel!!.get("ownerId").asInt

        if(houseOwnerId != null ){//password de kontrol edilecek ama önce api de olması şart
            val intent = Intent(this@UpdateProfileActivity , MainPageActivity::class.java)
            intent.putExtra("ownerId",houseOwnerId)
            intent.putExtra("customerOrOwner","houseOwner")
            //intent.putExtra("fromRegisterPage","firstLogin")
            //intent.putExtra("registeredUser-Name",userRegisterResponseModel!!.get("customerName").toString())
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@UpdateProfileActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}