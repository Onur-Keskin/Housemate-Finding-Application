package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.activity_single_profile.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    private val BASE_URL = "http://192.168.1.21:8080/"
    private var compositeDisposable : CompositeDisposable? = null
    private var userRegisterModel : CustomerModel? = null
    private var userRegisterResponseModel: CustomerModel? = null
    private lateinit var isCustomerOrHouseOwner:String
    private val customerObject = JsonObject()
    private val houseOwnerObject = JsonObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        changeStatusBarColor()
        compositeDisposable = CompositeDisposable()
    }

    fun onLoginClick(view:View){
        startActivity(Intent(this@RegisterActivity,MainActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right)
    }

    fun changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.register_bk_color)
        }
    }

    fun customerHouseOwnerRadioButtonHandler(view:View){
        if(radioCustomerHouseOwnerGroup.radioCustomerButton.isChecked){
            isCustomerOrHouseOwner = "customer"
        }else if(radioCustomerHouseOwnerGroup.radioHouseOwnerButton.isChecked){
            isCustomerOrHouseOwner = "houseOwner"
        }else{
            println("Hata")
        }
    }

    fun radioButtonHandler(view: View){
        if(radioGroup.radioMaleButton.isChecked){
            if(isCustomerOrHouseOwner == "customer"){
                customerObject.addProperty("customerGender",radioGroup.radioMaleButton.text.toString())
            }else{
                houseOwnerObject.addProperty("ownerGender",radioGroup.radioMaleButton.text.toString())
            }
        }else if(radioGroup.radioFemaleButton.isChecked){
            if(isCustomerOrHouseOwner == "customer"){
                customerObject.addProperty("customerGender",radioGroup.radioMaleButton.text.toString())
            }else{
                houseOwnerObject.addProperty("ownerGender",radioGroup.radioMaleButton.text.toString())
            }
        }else{
            println("hata")
        }
    }



    fun registerUser(view:View){

        if(isCustomerOrHouseOwner == "customer"){
            customerObject.addProperty("customerId",editTextId.editableText.toString().toInt())
            customerObject.addProperty("customerName",editTextName.text.toString())
            customerObject.addProperty("customerUserName",editTextUsername.text.toString())
            customerObject.addProperty("customerSurname",editTextSurname.text.toString())
            customerObject.addProperty("customerAge",editTextAge.editableText.toString().toInt())
            customerObject.addProperty("customerHometown",editTextHomeTown.text.toString())
            customerObject.addProperty("customerDepartment",editTextDepartment.text.toString())
            customerObject.addProperty("customerGrade",editTextGrade.editableText.toString().toInt())
            customerObject.addProperty("customerPhone",editTextMobile.text.toString())
            customerObject.addProperty("customerEmail",editTextEmail.text.toString())
            //jsonObject.addProperty("customerGender",radioGroup.)

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)

            compositeDisposable?.add(retrofit.addCustomer(customerObject)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleResponse))
        }
        else{//HosueOwner Kaydedilecek
            houseOwnerObject.addProperty("ownerId",editTextId.editableText.toString().toInt())
            customerObject.addProperty("ownerAge",editTextAge.editableText.toString().toInt())
            customerObject.addProperty("ownerDepartment",editTextDepartment.text.toString())
            customerObject.addProperty("ownerGrade",editTextGrade.editableText.toString().toInt())
            customerObject.addProperty("ownerHometown",editTextHomeTown.text.toString())
            customerObject.addProperty("ownerMail",editTextDepartment.text.toString())
            customerObject.addProperty("ownerName",editTextName.editableText.toString())
            customerObject.addProperty("ownerPhone",editTextMobile.text.toString())
            customerObject.addProperty("ownerSurname",editTextSurname.text.toString())
            //customerObject.addProperty("houseId",editTextEmail.text.toString())
            //customerObject.addProperty("ownerPassword",editTextEmail.text.toString())
        }

    }

    private fun handleResponse(userRegister: CustomerModel){
        userRegisterResponseModel = userRegister

        if(userRegisterResponseModel != null && userRegisterResponseModel!!.customerName != null){
            if(userRegisterResponseModel!!.customerUserName == userNameText.text.toString()){//password de kontrol edilecek ama önce api de olması şart
                val intent = Intent(this@RegisterActivity , MainActivity::class.java)
                intent.putExtra("userId", userRegisterResponseModel!!.customerId)
                intent.putExtra("fromRegisterPage","firstLogin")
                startActivity(intent)
                //finish()

            } else{
                Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterActivity , MainActivity::class.java)
                startActivity(intent)
            }
        }




    }


}