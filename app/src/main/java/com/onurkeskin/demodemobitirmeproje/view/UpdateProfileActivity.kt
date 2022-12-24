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
    private var userUpdateProfileResponseModel: JsonObject? = null
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
        if(houseOwnerModel != null){
            updateTextCustomerOrHouseOwner.text = "House Owner"
            updateEditTextName.hint = houseOwnerModel!!.ownerName
            updateEditTextSurname.hint = houseOwnerModel!!.ownerSurname
            updateEditTextUsername.hint = houseOwnerModel!!.ownerUserName
            updateEditTextAge.hint = houseOwnerModel!!.ownerAge.toString()
            updateEditTextHomeTown.hint = houseOwnerModel!!.ownerHometown
            updateEditTextDepartment.hint = houseOwnerModel!!.ownerDepatment
            //updateEditTextPassword.hint = houseOwnerModel!!.customerPassword //burayı modelime eklicem
            updateEditTextGrade.hint = houseOwnerModel!!.ownerGrade.toString()
            updateEditTextPhone.hint = houseOwnerModel!!.ownerPhone
            updateEditTextEmail.hint = houseOwnerModel!!.ownerMail
            updateEditTextGender.hint = houseOwnerModel!!.ownerGender //Burası radio button yapılacak
        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }

    }

    fun updateProfile(view:View){

        val intent = intent
        val customerId = intent.getIntExtra("customerId",0)
        val houseOwnerId = intent.getIntExtra("ownerId",0)

        println(customerId)

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
            houseOwnerObject.addProperty("ownerId",houseOwnerId)
            houseOwnerObject.addProperty("ownerName",updateEditTextName.text.toString())
            houseOwnerObject.addProperty("ownerSurname",updateEditTextSurname.text.toString())
            houseOwnerObject.addProperty("ownerUsername",updateEditTextUsername.text.toString())
            houseOwnerObject.addProperty("ownerAge",updateEditTextAge.editableText.toString().toInt())
            houseOwnerObject.addProperty("ownerHometown",updateEditTextHomeTown.text.toString())
            houseOwnerObject.addProperty("ownerDepartment",updateEditTextDepartment.text.toString())
            houseOwnerObject.addProperty("ownerPassword",updateEditTextPassword.text.toString())
            houseOwnerObject.addProperty("ownerGrade",updateEditTextGrade.editableText.toString().toInt())
            houseOwnerObject.addProperty("ownerPhone",updateEditTextPhone.text.toString())
            houseOwnerObject.addProperty("ownerEmail",updateEditTextEmail.text.toString()) //burası ownermail olabilir


        }
        else{
            Toast.makeText(this@UpdateProfileActivity,"HATA" , Toast.LENGTH_LONG).show()
        }
    }

    private fun handleCustomerUpdateClickResponse(userUpdate: JsonObject){
        userUpdateProfileResponseModel = userUpdate
        //println(userUpdateProfileResponseModel)

        if(userUpdateProfileResponseModel!!.get("customerId").asInt != null ){//password de kontrol edilecek ama önce api de olması şart
            val intent = Intent(this@UpdateProfileActivity , MainPageActivity::class.java)
            intent.putExtra("userId", userUpdateProfileResponseModel!!.get("customerId").asInt)
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