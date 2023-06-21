package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_check_email.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ForgotPasswordActivity : AppCompatActivity() {
    var customerResetPasswordModel : JsonObject = JsonObject()
    var HouseOwnerResetPasswordModel : JsonObject = JsonObject()
    private var compositeDisposable : CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        compositeDisposable = CompositeDisposable()
    }

    //Parola Resetleme Fonksiyonu
    fun resetPassword(view: View){
        var customerOrHouseOwner = intent.getStringExtra("customerOrHouseOwner")

        if(customerOrHouseOwner == "customer"){
            val customerUserName = intent.getStringExtra("customerUsername")
            customerResetPasswordModel.addProperty("username",customerUserName)

            if(editTextNewPassword.text.toString() == editTextNewPasswordConfirm.text.toString()){
                println("Parolalar Eşleşiyor in ForgorPasswordActivity Customer")
                customerResetPasswordModel.addProperty("password",editTextNewPassword.text.toString())
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            compositeDisposable?.add(retrofit.updateCustomerPasswordByUsername(customerResetPasswordModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleCustomerWithNewPasswordResponse))


        }
        else if(customerOrHouseOwner == "houseOwner"){
            val houseOwnerUserName = intent.getStringExtra("houseOwnerUsername")

            HouseOwnerResetPasswordModel.addProperty("username",houseOwnerUserName)

            if(editTextNewPassword.text.toString() == editTextNewPasswordConfirm.text.toString()){
                println("Parolalar Eşleşiyor in ForgotPasswordActivity HouseOwner")
                HouseOwnerResetPasswordModel.addProperty("password",editTextNewPassword.text.toString())
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)


            compositeDisposable?.add(retrofit.updateHouseOwnerPasswordByUsername(HouseOwnerResetPasswordModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleHouseOwnerWithNewPasswordResponse))
        }else{
            Toast.makeText(this@ForgotPasswordActivity,"Error in ForgotPasswordActivity", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleCustomerWithNewPasswordResponse(customerModel: CustomerModel){
        Toast.makeText(this@ForgotPasswordActivity,"New Password Setted Customer", Toast.LENGTH_LONG).show()
        val intent = Intent(this@ForgotPasswordActivity, MainActivity::class.java)
        startActivity(intent)

    }

    private fun handleHouseOwnerWithNewPasswordResponse(houseOwnerModel: HouseOwnerModel){
        Toast.makeText(this@ForgotPasswordActivity,"New Password Setted HouseOwner", Toast.LENGTH_LONG).show()
        val intent = Intent(this@ForgotPasswordActivity, HouseOwnerLoginActivity::class.java)
        startActivity(intent)
    }
}