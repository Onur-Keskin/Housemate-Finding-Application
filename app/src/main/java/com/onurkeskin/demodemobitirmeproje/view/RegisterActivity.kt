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
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    private val BASE_URL = "http://192.168.1.21:8080/"
    private var compositeDisposable : CompositeDisposable? = null
    private var userRegisterModel : CustomerModel? = null
    private var customerRegisterResponseModel: JsonObject? = null
    private var houseOwnerRegisterResponseModel: JsonObject? = null
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

    /*
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

     */



    fun registerUser(view:View){

        if(isCustomerOrHouseOwner == "customer"){
            //customerObject.addProperty("customerId",editTextId.editableText.toString().toInt())
            customerObject.addProperty("customerName",editTextName.text.toString())
            customerObject.addProperty("customerUsername",editTextUsername.text.toString())
            customerObject.addProperty("customerSurname",editTextSurname.text.toString())
            customerObject.addProperty("customerPassword",editTextPassword.text.toString())
            customerObject.addProperty("customerEmail",editTextEmail.text.toString())

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)

            compositeDisposable?.add(retrofit.saveOneCustomer(customerObject)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleCustomerRegisterResponse))
        }
        else{//HosueOwner Kaydedilecek
            //houseOwnerObject.addProperty("ownerId",editTextId.editableText.toString().toInt())
            houseOwnerObject.addProperty("houseOwnerName",editTextName.editableText.toString())
            houseOwnerObject.addProperty("houseOwnerSurname",editTextSurname.text.toString())
            houseOwnerObject.addProperty("houseOwnerEmail",editTextEmail.text.toString())
            houseOwnerObject.addProperty("houseOwnerPassword",editTextPassword.text.toString())
            houseOwnerObject.addProperty("houseOwnerUsername",editTextUsername.text.toString())

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            compositeDisposable?.add(retrofit.saveOneHouseOwner(houseOwnerObject)
                .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                .subscribe(this::handleHouseOwnerRegisterResponse))

        }

    }

    private fun handleCustomerRegisterResponse(customerRegister: JsonObject){ //customer için post request handle eder
        customerRegisterResponseModel = customerRegister
        //println(customerRegisterResponseModel)

        if(customerRegisterResponseModel!!.get("customerId").asInt != null ){//password de kontrol edilecek ama önce api de olması şart
            val intent = Intent(this@RegisterActivity , PropertiesFormActivity::class.java)
            intent.putExtra("customerId", customerRegisterResponseModel!!.get("customerId").asInt)
            intent.putExtra("fromRegisterPage","customer")
            intent.putExtra("registeredUser-Name",customerRegisterResponseModel!!.get("customerName").toString())
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@RegisterActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleHouseOwnerRegisterResponse(houseOwnerRegister: JsonObject){ //houseOwner için post request handle eder
        houseOwnerRegisterResponseModel = houseOwnerRegister
        //println(houseOwnerRegisterResponseModel)
        val houseOwnerId = houseOwnerRegisterResponseModel!!.get("ownerId").asInt

        if(houseOwnerId != 0 ){//önce form sayfasına sayfasına sonra ev kaydetme sayfasına gidecek

            val intent = Intent(this@RegisterActivity,PropertiesFormActivity::class.java)
            intent.putExtra("fromRegisterPage","houseOwner")
            intent.putExtra("houseOwnerId",houseOwnerRegisterResponseModel!!.get("ownerId").asInt)
            intent.putExtra("registeredUser-Name",houseOwnerRegisterResponseModel!!.get("ownerName").asString)
            intent.putExtra("registeredUser-Surname",houseOwnerRegisterResponseModel!!.get("ownerSurname").asString)
            intent.putExtra("registeredUser-Username",houseOwnerRegisterResponseModel!!.get("ownerUsername").asString)
            intent.putExtra("registeredUser-Email",houseOwnerRegisterResponseModel!!.get("ownerMail").asString)
            intent.putExtra("registeredUser-Password",houseOwnerRegisterResponseModel!!.get("ownerPassword").toString())
            startActivity(intent)


            /*
            val intent = Intent(this@RegisterActivity,SaveHouseActivity::class.java)
            intent.putExtra("houseOwnerId",houseOwnerRegisterResponseModel!!.get("ownerId").asInt)
            intent.putExtra("registeredUser-Name",houseOwnerRegisterResponseModel!!.get("ownerName").asString)
            intent.putExtra("registeredUser-Surname",houseOwnerRegisterResponseModel!!.get("ownerSurname").asString)
            intent.putExtra("registeredUser-Username",houseOwnerRegisterResponseModel!!.get("ownerUsername").asString)
            intent.putExtra("registeredUser-Email",houseOwnerRegisterResponseModel!!.get("ownerMail").asString)
            intent.putExtra("registeredUser-Password",houseOwnerRegisterResponseModel!!.get("ownerPassword").toString())
            startActivity(intent)

             */

        } else{
            Toast.makeText(this,"Error Happened", Toast.LENGTH_LONG).show()
            val intent = Intent(this@RegisterActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }


}