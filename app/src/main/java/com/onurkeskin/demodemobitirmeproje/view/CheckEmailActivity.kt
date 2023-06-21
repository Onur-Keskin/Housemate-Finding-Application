package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CheckEmailActivity : AppCompatActivity() {
    private var compositeDisposable : CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_email)

        compositeDisposable = CompositeDisposable()
    }

    //Kullanıcının emailini kullanarak bilgileri getirilecek
    fun checkEmail(view: View){
        var fromWhere  = intent.getStringExtra("checkEmailFrom")

        if (fromWhere == "customerResetPassword"){
            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            compositeDisposable?.add(retrofit.getOneCustomerByMail(editTextCheckEmail.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleBringCustomerByMailResponse))
        }
        else if(fromWhere == "houseOwnerResetPassword"){
            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)


            compositeDisposable?.add(retrofit.getOneHouseOwnerByMail(editTextCheckEmail.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleBringHouseOwnerByMailResponse))

        }else{
            Toast.makeText(this@CheckEmailActivity,"Error in CheckEmailActivity",Toast.LENGTH_LONG).show()
        }
    }

    private fun handleBringCustomerByMailResponse(customerModel: CustomerModel){
        val intent = Intent(this@CheckEmailActivity,ForgotPasswordActivity::class.java)
        intent.putExtra("customerOrHouseOwner","customer")
        intent.putExtra("customerUsername",customerModel.customerUsername)
        startActivity(intent)
    }

    private fun handleBringHouseOwnerByMailResponse(houseOwnerModel: HouseOwnerModel){
        val intent = Intent(this@CheckEmailActivity,ForgotPasswordActivity::class.java)
        intent.putExtra("customerOrHouseOwner","houseOwner")
        intent.putExtra("houseOwnerUsername",houseOwnerModel.ownerUsername)
        startActivity(intent)
    }
}