package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var userLoginModel : CustomerModel? = null
    private var loginModel : JsonObject = JsonObject()
    private var fromRegister = ""
    //private var userLoginRecyclerViewAdapter : UserLoginRecyclerViewAdapter? = null

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for changing status bar icon color
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_main)

        compositeDisposable = CompositeDisposable()

    }

    fun signIn(view: View){

        loginModel.addProperty("username",userNameText.text.toString())
        loginModel.addProperty("password",passwordText.text.toString())

        loadData()
    }

    fun onLoginClick(view:View){
        startActivity(Intent(this@MainActivity,RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay)
    }


    private fun loadData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalVariables.globalBASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CustomerAPI::class.java)


        compositeDisposable?.add(retrofit.customerLogin(loginModel)
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))


    }

    private fun handleResponse(userLogin: CustomerModel){
        userLoginModel = userLogin
        //!!!!!!!!!!! kayıt olan username ler bir rakam içerince login olunamıyor
        if(userLoginModel!!.customerUsername == userNameText.editableText.toString()){
            intent = Intent(this@MainActivity , HousesActivity::class.java)
            intent.putExtra("userId", userLoginModel!!.customerId)
            intent.putExtra("customerOrOwner","Customer")
            startActivity(intent)

            userNameText.text.clear()
            passwordText.text.clear()


        } else{
            Toast.makeText(this,"Error Happened in CustomerLogin", Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity , MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun onHouseOwnerLoginPageClick(view:View){
        startActivity(Intent(this@MainActivity,HouseOwnerLoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay)
    }

    fun goToCheckEmailPart(view:View){
        val intent = Intent(this@MainActivity, CheckEmailActivity::class.java)
        intent.putExtra("checkEmailFrom","customerResetPassword")
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}