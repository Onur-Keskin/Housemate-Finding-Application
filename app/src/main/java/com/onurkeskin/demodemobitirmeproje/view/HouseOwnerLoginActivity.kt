package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_house_owner_login.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HouseOwnerLoginActivity : AppCompatActivity() {
    private var ownerLoginModel : HouseOwnerModel? = null

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for changing status bar icon color
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_house_owner_login)

        compositeDisposable = CompositeDisposable()
    }

    fun ownerSignIn(view: View){
        val username = ownerUsernameText.text.toString()
        val password = ownerPasswordText.text.toString()

        loadData(username)

    }

    fun onOwnerLoginClick(view:View){
        startActivity(Intent(this@HouseOwnerLoginActivity,RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay)
    }

    fun goToResetPasswordPageHouseOwnerEmailPart(view:View){
        //val intent = Intent(this@HouseOwnerLoginActivity, CheckEmailActivity::class.java)
        //intent.putExtra("checkEmailFrom","houseOwnerLogin")
        //startActivity(intent)
    }

    private fun loadData(houseOwnerUsername:String){

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalVariables.globalBASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseOwnerAPI::class.java)


        compositeDisposable?.add(retrofit.getOwnerByUsername(houseOwnerUsername)
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))

        //println(userLoginModel)
    }

    private fun handleResponse(ownerLogin: HouseOwnerModel){
        ownerLoginModel = ownerLogin

        //!!!!!!!!!!! kayıt olan username ler bir rakam içerince login olunamıyor
        if(ownerLoginModel!!.ownerUsername == ownerUsernameText.editableText.toString()){//password de kontrol edilecek ama önce api de olması şart
            intent = Intent(this@HouseOwnerLoginActivity , MainPageActivity::class.java)
            intent.putExtra("ownerId", ownerLoginModel!!.ownerId)
            intent.putExtra("customerOrOwner","houseOwner")
            startActivity(intent)
            //finish()

            ownerUsernameText.text.clear()
            ownerPasswordText.text.clear()


        } else{
            Toast.makeText(this,"House Owner Login Error ", Toast.LENGTH_LONG).show()
            val intent = Intent(this@HouseOwnerLoginActivity , MainActivity::class.java)
            startActivity(intent)
        }


    }

}