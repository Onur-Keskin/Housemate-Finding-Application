package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
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
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var userLoginModel : CustomerModel? = null
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
        val userName = userNameText.text.toString()
        val password = passwordText.text.toString()

        loadData(userName)

    }

    fun onLoginClick(view:View){
        startActivity(Intent(this@MainActivity,RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay)
    }


    private fun loadData(userName:String){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CustomerAPI::class.java)


        compositeDisposable?.add(retrofit.getCustomerByUsername(userName)
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))

        //println(userLoginModel)
    }

    private fun handleResponse(userLogin: CustomerModel){
        userLoginModel = userLogin
        //println(userLoginModel)


        if(userLoginModel!!.customerUsername == userNameText.text.toString()){//password de kontrol edilecek ama önce api de olması şart

            if(fromRegister == "firstLogin"){
                val intent = Intent(this@MainActivity,PropertiesFormActivity::class.java)
                intent.putExtra("firstLoginCustomerId",userLoginModel!!.customerId)
                startActivity(intent)
            }else{
                intent = Intent(this@MainActivity , MainPageActivity::class.java)
                intent.putExtra("userId", userLoginModel!!.customerId)
                startActivity(intent)
                //finish()
            }
            userNameText.text.clear()
            passwordText.text.clear()


        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity , MainActivity::class.java)
            startActivity(intent)
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}