package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.JsonObject
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demobitirmeproje.view.MainPageActivity
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_properties_form.*
import kotlinx.android.synthetic.main.content_properties_form.view.*
import kotlinx.android.synthetic.main.content_properties_form.welcomeText
import kotlinx.android.synthetic.main.content_saving_house_form.*
import kotlinx.android.synthetic.main.content_saving_house_form.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SaveHouseActivity : AppCompatActivity() {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private val savingHouseFormObject = JsonObject()
    private lateinit var isCustomerOrHouseOwner:String
    private var compositeDisposable : CompositeDisposable? = null
    private var registerHouseResponseModel: JsonObject? = null
    private var houseOwnerUpdateObject : JsonObject = JsonObject()
    private var houseOwnerUpdateResponseObject : JsonObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_house)

        isCustomerOrHouseOwner = intent.getStringExtra("fromRegisterPage").toString()//Formu kaydedilecek olan kişi customer mı houseOwner mı?

        val registeredUserName = intent.getStringExtra("registeredUser-Name") // yeni kayıt olan kullanıcının adı
        welcomeText.text = "Hoşgeldin! ${registeredUserName}! Şimdi evini kaydetme vakti"

        val countOfBathroomSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.countOfBSO,android.R.layout.simple_spinner_item)
        countOfBathroomSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerCountBathroom.adapter = countOfBathroomSpinnerAdapter

        val countOfBedroomSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.countOfBSO,android.R.layout.simple_spinner_item)
        countOfBedroomSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerCountBedroom.adapter = countOfBedroomSpinnerAdapter

        val countOfSalonSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.countOfBSO,android.R.layout.simple_spinner_item)
        countOfBedroomSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerCountSaloon.adapter = countOfSalonSpinnerAdapter


        val countOfOwnerSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.countOfBSO,android.R.layout.simple_spinner_item)
        countOfOwnerSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerCountOwner.adapter = countOfOwnerSpinnerAdapter


        compositeDisposable = CompositeDisposable()

    }
    /*
    fun toPropertiesForm(view:View){
        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)
        val houseOwnerName = intent.getStringExtra("registeredUser-Name")

        if(houseOwnerId != 0){
            val intent = Intent(this@SaveHouseActivity , PropertiesFormActivity::class.java)
            intent.putExtra("houseOwnerId", houseOwnerId)
            intent.putExtra("fromRegisterPage","houseOwner")
            intent.putExtra("registeredUser-Name", houseOwnerName)
            startActivity(intent)
            //finish()
        }else{
            Toast.makeText(this@SaveHouseActivity,"There is a error in SaveHouseActivity",Toast.LENGTH_LONG).show()
        }
    }

     */

    fun sendHouseForm(view: View){ //İlk başta ev oluşturulur ve post request ile kaydedilecek sonra houseOwner'ın houseId si güncellenecek

        savingHouseFormObject.addProperty("houseAddress",houseAdress.text.toString())
        savingHouseFormObject.addProperty("floor",floor.text.toString().toInt())
        savingHouseFormObject.addProperty("rent",rent.text.toString().toInt())

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseAPI::class.java)

        handleDropdownMenues()

        compositeDisposable?.add(retrofit.saveOneHouse(savingHouseFormObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSavingNewHouseResponse))


    }

    private fun handleSavingNewHouseResponse(houseResponse:JsonObject){

        println("handleSavingNewHouseResponse içindeyiz compositeDisposable : " +compositeDisposable?.isDisposed)

        registerHouseResponseModel = houseResponse
        //println(registerHouseResponseModel)

        val houseId = registerHouseResponseModel!!.get("houseId").asInt
        val houseOwnerId = intent.getIntExtra("houseOwnerId",0)
        val houseOwnerName = intent.getStringExtra("registeredUser-Name")
        val houseOwnerSurname = intent.getStringExtra("registeredUser-Surname")
        val houseOwnerUsername = intent.getStringExtra("registeredUser-Username")
        val houseOwnerEmail = intent.getStringExtra("registeredUser-Email")
        val houseOwnerPassword = intent.getStringExtra("registeredUser-Password")


        println("houseOwnerId : $houseOwnerId")
        println("houseOwnerName : $houseOwnerName")
        println("houseOwnerSurname : $houseOwnerSurname")
        println("houseOwnerUsername : $houseOwnerUsername")
        println("houseOwnerEmail : $houseOwnerEmail")
        println("password : $houseOwnerPassword")



        if(houseId!= 0){//ev oluşturulduysa

            houseOwnerUpdateObject.addProperty("houseOwnerId",houseOwnerId)
            houseOwnerUpdateObject.addProperty("houseOwnerName",houseOwnerName.toString())
            houseOwnerUpdateObject.addProperty("houseOwnerSurname",houseOwnerSurname.toString())
            houseOwnerUpdateObject.addProperty("houseOwnerUsername",houseOwnerUsername.toString())
            houseOwnerUpdateObject.addProperty("houseOwnerEmail",houseOwnerEmail.toString())

            //houseOwnerUpdateModel?.addProperty("houseOwnerPassword",houseOwnerPassword)

            houseOwnerUpdateObject.addProperty("houseOwnerAge",0)
            houseOwnerUpdateObject.addProperty("houseOwnerHometown","")
            houseOwnerUpdateObject.addProperty("houseOwnerDepartment","")
            houseOwnerUpdateObject.addProperty("houseOwnerGrade",0)
            houseOwnerUpdateObject.addProperty("houseOwnerPhone","")
            houseOwnerUpdateObject.addProperty("houseOwnerGender","")

            houseOwnerUpdateObject.addProperty("houseId", houseId)

            println(houseOwnerUpdateObject)

            updateHouseOwner(houseOwnerUpdateObject)




        } else{
            Toast.makeText(this," Update House Owner Error ", Toast.LENGTH_LONG).show()
            val intent = Intent(this@SaveHouseActivity , MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun updateHouseOwner(houseOwnerUpdateObject:JsonObject){
        println("updateHouseOwner fonk. çalıştı")
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(HouseOwnerAPI::class.java)

        println(retrofit)


        compositeDisposable?.add(retrofit.updateOneHouseOwner(houseOwnerUpdateObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleHouseOwnerUpdateResponse))

        println("updateHouseOwner içindeyiz compositeDisposable : " +compositeDisposable?.isDisposed)
    }

    fun handleHouseOwnerUpdateResponse(updateHouseOwner: JsonObject){ //Tekrar güncellenen houseOwner (houseId güncellendi)
        println("handleHouseOwnerUpdateResponse fonk çalıştı")
        houseOwnerUpdateResponseObject = updateHouseOwner
        println("houseOwnerUpdateProfileResponseModel : ")
        println(houseOwnerUpdateResponseObject)
        val houseOwnerId = houseOwnerUpdateResponseObject!!.get("ownerId").asInt
        println("House Owner Id : " + houseOwnerId)

        if(houseOwnerId != 0 ){
            val intent = Intent(this@SaveHouseActivity , PropertiesFormActivity::class.java)
            intent.putExtra("ownerId",houseOwnerId)
            //intent.putExtra("fromRegisterPage","firstLogin")
            //intent.putExtra("registeredUser-Name",userRegisterResponseModel!!.get("customerName").toString())
            startActivity(intent)
            //finish()

        } else{
            Toast.makeText(this,"Updated HouseOwner and Save House Error", Toast.LENGTH_LONG).show()
            val intent = Intent(this@SaveHouseActivity , MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleDropdownMenues(){

        savingHouseFormObject.addProperty("countOfBathroom",spinnerCountBathroom.selectedItem.toString().toInt())
        //println("Selected luxury : " + spinnerLuxuryImportance.selectedItem.toString().toInt())
        savingHouseFormObject.addProperty("countOfBedroom",spinnerCountBedroom.selectedItem.toString().toInt())
        savingHouseFormObject.addProperty("countOfSalon",spinnerCountSaloon.selectedItem.toString().toInt())
        savingHouseFormObject.addProperty("countOfOwner",spinnerCountOwner.selectedItem.toString().toInt())

    }



    fun useHeatResourceHandler(view:View){ //radio button
        if(radioUseHeatResourceGroup.radioGasButton.isChecked){ //doğal gaz
            savingHouseFormObject.addProperty("heatResource","gas")
        }
        else if(radioUseHeatResourceGroup.radioCoalButton.isChecked){ //kömür
            savingHouseFormObject.addProperty("heatResource","coal")
        }
        else if(radioUseHeatResourceGroup.radioOtherHeatResourceButton.isChecked){ //diğer
            savingHouseFormObject.addProperty("heatResource","other")
        }
        else{
            Toast.makeText(this@SaveHouseActivity,"HeatResource",Toast.LENGTH_LONG).show()
        }
    }
    fun isFurnitureHandler(view:View){//radio button
        if(radioIsFurnitureGroup.radioYesFurnitureButton.isChecked){ //eşyalı
            savingHouseFormObject.addProperty("furnished","yes")
        }
        else if(radioIsFurnitureGroup.radioNoFurnitureButton.isChecked){ //eşyasız
            savingHouseFormObject.addProperty("furnished","no")
        }
        else{
            Toast.makeText(this@SaveHouseActivity,"Furnished Error",Toast.LENGTH_LONG).show()
        }
    }

    fun internetPavedHandler(view:View){//radio button
        if(radioInternetPavedGroup.radioYesInternetPavedButton.isChecked){ //internet dahil
            savingHouseFormObject.addProperty("internetPaved","yes")
        }
        else if(radioInternetPavedGroup.radioNoInternetPavedButton.isChecked){ //eşyasız
            savingHouseFormObject.addProperty("internetPaved","no")
        }
        else{
            Toast.makeText(this@SaveHouseActivity,"Internet Paved Error",Toast.LENGTH_LONG).show()
        }
    }
    /*
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

     */
}