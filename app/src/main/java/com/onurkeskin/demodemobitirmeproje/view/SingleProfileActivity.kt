package com.onurkeskin.demodemobitirmeproje.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_single_profile.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SingleProfileActivity : AppCompatActivity() /*, CustomerSingleProfileRecyclerViewAdapter.Listener*/ {
    private var isCustomer: String?=null
    private var isHouseOwner: String?=null
    private var isLoginCustomer : String? = null
    private var isLoginHouseOwner : String? = null
    private var isCustomerWhoLikesHouse : String? = null

    private var customerModel : CustomerModel? = null
    private var houseOwnerModel : HouseOwnerModel?=null

    private var compositeDisposable : CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_profile)

        compositeDisposable = CompositeDisposable()

        changeStatusBarColor()

        loadData()

    }

    fun onUpdateProfileClick(view: View){
        //customer mı yoksa house owner mı profil bilgilerini güncelleyecek bunun bilgisini alıp göndermek lazım
        val customerOrHouseOwner = singleProfileCustomerOrHouseOwner.text.toString()

        if(customerOrHouseOwner == "Customer"){ //Customer ise
            val intent = Intent(this@SingleProfileActivity,UpdateProfileActivity::class.java)
            intent.putExtra("customerId",customerModel!!.customerId)
            startActivity(intent)
        }else{ //HouseOwner ise
            val intent = Intent(this@SingleProfileActivity,UpdateProfileActivity::class.java)
            intent.putExtra("ownerId",houseOwnerModel!!.ownerId)
            startActivity(intent)
        }
        /*
        val intent = Intent(this@SingleProfileActivity,MainActivity::class.java)
        intent.putExtra("userId",customerModel!!.customerId)
        startActivity(intent)//updateProfileInfosSayfasınaGidecek

         */
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right) //animasyon
    }

    fun onUpdateAttributeClick(view: View){ //Attribute Güncelleme Sayfası
        val customerOrHouseOwner = singleProfileCustomerOrHouseOwner.text.toString()

        if(customerOrHouseOwner == "Customer"){ //Customer ise
            val intent = Intent(this@SingleProfileActivity,UpdatePropertiesFormActivity::class.java)
            intent.putExtra("customerId",customerModel!!.customerId)
            intent.putExtra("customerOrHouseOwner",customerOrHouseOwner)//customer
            intent.putExtra("updateAttributeUser-Name",customerModel!!.customerName)
            startActivity(intent)
        }else{ //HouseOwner ise
            val intent = Intent(this@SingleProfileActivity,UpdatePropertiesFormActivity::class.java)
            intent.putExtra("houseOwnerId",houseOwnerModel!!.ownerId)
            intent.putExtra("customerOrHouseOwner",customerOrHouseOwner)//houseOwner
            intent.putExtra("updateAttributeUser-Name",houseOwnerModel!!.ownerName)
            startActivity(intent)
        }

        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right)
    }

    private fun changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.register_bk_color)
        }
    }

    private fun loadData(){
        val intent = intent

        isCustomer = intent.getStringExtra("fromCustomer")
        isHouseOwner = intent.getStringExtra("fromHouseOwner")
        isLoginCustomer = intent.getStringExtra("fromMainPageCustomer")
        isLoginHouseOwner = intent.getStringExtra("fromMainPageHouseOwner")
        isCustomerWhoLikesHouse = intent.getStringExtra("fromCustomerWhoLikeHouse")

        if (isCustomer.equals("customerProfile")){//eğer customerProfiles kısmından profile gidilecekse
            val id = intent.getIntExtra("customerId",1)
            val customerId : String? = "$id"
            singleProfileUpdateInfoButton.isVisible = false
            singlePropertiesUpdateInfoButton.isVisible = false

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            if (customerId != null){

                compositeDisposable?.add(retrofit.getCustomerById(customerId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse))
            }
        }
        else if(isHouseOwner.equals("houseOwnerProfile")){ //eğer houseOwnerProfiles kısmından profile gidilecekse
            val id = intent.getIntExtra("houseOwnerId",1)
            val houseOwnerId : String? = "$id"
            singleProfileUpdateInfoButton.isVisible = false
            singlePropertiesUpdateInfoButton.isVisible = false

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            if(houseOwnerId!=null){
                compositeDisposable?.add(retrofit.getOwnerById(houseOwnerId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse))
            }

        }


        else if(isLoginCustomer.equals("customerLoginProfile")){ //eğer customer kendi profiline gidecekse
            val customerId = intent.getIntExtra("customerLoginId",0)

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            if (customerId != 0){

                compositeDisposable?.add(retrofit.getCustomerById(customerId.toString())
                    .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                    .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                    .subscribe(this::handleResponse))
            }

        }

        else if(isLoginHouseOwner.equals("houseOwnerLoginProfile")){ //eğer houseOwner kendi profiline gidecekse
            val ownerId = intent.getIntExtra("houseOwnerLoginId",0)

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)


            if (ownerId != 0){

                compositeDisposable?.add(retrofit.getOwnerById(ownerId.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse))
            }

        }

        else if(isCustomerWhoLikesHouse.equals("customerWhoLikesHouse")){ //eğer houseOwner evini beğenen customer' ın profiline gidecekse
            val customerId = intent.getIntExtra("customerId",0)

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalVariables.globalBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            if (customerId != 0){

                compositeDisposable?.add(retrofit.getCustomerById(customerId.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleCustomerWhoLikesHouseResponse))
            }

        }




        /*
        //API ile Retrofit'i birbirine bağlama işlemi 2. yol
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        println(retrofit)

        val service = retrofit.create(CustomerSingleProfileAPI::class.java)

        val call = service.getData()
        println(call)

        call.enqueue(object : Callback<CustomerModel> { //asenkron bir şekilde işlemler gerçekleştirilecek
            override fun onResponse(call: Call<CustomerModel>, response: Response<CustomerModel>) {
                if (response.isSuccessful){
                    println("Response geldi")
                    //eğer boş değilse parantez içini yap
                    response.body()?.let {
                        customerModel = it
                        customerModel?.let{
                            customerSingleProfileRecyclerViewAdapter = CustomerSingleProfileRecyclerViewAdapter(it,this@CustomerSingleProfileActivity)
                            customerSingleProfileRecyclerView.adapter = customerSingleProfileRecyclerViewAdapter
                        }
                            if (customerModel != null){
                                println("UserId : "+customerModel!!.userId)
                                println("Id : " + customerModel!!.id)
                                println("Title : " +customerModel!!.title)
                                println("Body :" + customerModel!!.body)
                            }



                    }
                }
                else{
                    println("Gelmedi")
                }
            }

            override fun onFailure(call: Call<CustomerModel>, t: Throwable) {
                t.printStackTrace()
            }

        })

         */

    }

    @SuppressLint("SetTextI18n")
    private fun handleResponse(customer: CustomerModel){
        customerModel = customer

        if(customerModel != null){
            singleProfileCustomerOrHouseOwner.text = "Customer"
            singleProfileNameSurnameAge.text = customerModel!!.customerName + " " +customer!!.customerSurname +  " , " + customer!!.customerAge.toString()
            singleProfileUsername.text = customerModel!!.customerUsername
            singleProfileHometown.text = customerModel!!.customerHometown
            singleProfileDepartmentGrade.text = customerModel!!.customerDepartment + " , " +customerModel!!.customerGrade.toString()
            //singleProfilePassword.text = customerModel!!.customerPassword.toString()
            singleProfilePhone.text = customerModel!!.customerPhone
            singleProfileEmail.text = customerModel!!.customerEmail
            singleProfileGender.text = customerModel!!.customerGender
        }else{
            Toast.makeText(this,"Error happened in SingleProfileActivity Customer Side" , Toast.LENGTH_LONG).show()
        }

    }

    private fun handleResponse(houseOwner: HouseOwnerModel){
        houseOwnerModel = houseOwner
        if(houseOwnerModel != null){
            singleProfileCustomerOrHouseOwner.text = "HouseOwner"
            singleProfileNameSurnameAge.text = houseOwnerModel!!.ownerName + houseOwnerModel!!.ownerSurname + " , " + houseOwnerModel!!.ownerAge.toString()
            singleProfileUsername.text = houseOwnerModel!!.ownerUsername
            singleProfileHometown.text = houseOwnerModel!!.ownerHometown
            singleProfileDepartmentGrade.text = houseOwnerModel!!.ownerDepartment + houseOwnerModel!!.ownerGrade
            singleProfilePhone.text = houseOwnerModel!!.ownerPhone
            singleProfileEmail.text = houseOwnerModel!!.ownerMail
            singleProfileGender.text = houseOwnerModel!!.ownerGender
        }else{
            Toast.makeText(this,"Error happened in SingleProfileActivity HouseOwner Side" , Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun handleCustomerWhoLikesHouseResponse(customer: CustomerModel){
        customerModel = customer

        if(customerModel != null){
            singleProfileCustomerOrHouseOwner.text = "Customer"
            singleProfileNameSurnameAge.text = customerModel!!.customerName + " " +customer!!.customerSurname +  " , " + customer!!.customerAge.toString()
            singleProfileUsername.text = customerModel!!.customerUsername
            singleProfileHometown.text = customerModel!!.customerHometown
            singleProfileDepartmentGrade.text = customerModel!!.customerDepartment + " , " +customerModel!!.customerGrade.toString()
            //singleProfilePassword.text = customerModel!!.customerPassword.toString()
            singleProfilePhone.text = customerModel!!.customerPhone
            singleProfileEmail.text = customerModel!!.customerEmail
            singleProfileGender.text = customerModel!!.customerGender
            singleProfileUpdateInfoButton.isVisible = false
            singlePropertiesUpdateInfoButton.isVisible = false
        }else{
            Toast.makeText(this,"Error happened in SingleProfileActivity Customer Side" , Toast.LENGTH_LONG).show()
        }

    }
    /*
    private fun handleLoginHouseOwnerResponse(houseOwner: HouseOwnerModel){
        houseOwnerModel = houseOwner
        if(houseOwnerModel != null){
            singleProfileCustomerOrHouseOwner.text = "HouseOwner"
            singleProfileNameSurnameAge.text = houseOwnerModel!!.ownerName + houseOwnerModel!!.ownerSurname + " , " + houseOwnerModel!!.ownerAge.toString()
            singleProfileUsername.text = houseOwnerModel!!.ownerUserName
            singleProfileHometown.text = houseOwnerModel!!.ownerHometown
            singleProfileDepartmentGrade.text = houseOwnerModel!!.ownerDepatment + houseOwnerModel!!.ownerGrade
            singleProfilePhone.text = houseOwnerModel!!.ownerPhone
            singleProfileEmail.text = houseOwnerModel!!.ownerMail
            singleProfileGender.text = houseOwnerModel!!.ownerGender
        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }

    }

     */




    /*
    override fun onItemClick(customerModel: CustomerModel) {
        Toast.makeText(this,"Clicked : ${customerModel.id}",Toast.LENGTH_LONG).show()
        //val intent = Intent(this@CustomerSingleProfileActivity,CustomerSingleProfileActivity::class.java)
        //startActivity(intent)
    }

     */
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflater xml ilemkodu bağlama
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.singleprofile_menu,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val xD = "Ayalarlar sayfasına gider"
        }
        else if(item.itemId == R.id.logout){
            val customerOrHouseOwner = singleProfileCustomerOrHouseOwner.text.toString()
            if(customerOrHouseOwner == "Customer"){
                val intent = Intent(this@SingleProfileActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@SingleProfileActivity,HouseOwnerLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        else if(item.itemId == R.id.resetPassword){
            val customerOrHouseOwner = singleProfileCustomerOrHouseOwner.text.toString()
            if(customerOrHouseOwner == "Customer"){
                val intent = Intent(this@SingleProfileActivity,ForgotPasswordActivity::class.java)
                intent.putExtra("customerOrHouseOwner","customer")
                intent.putExtra("inSingleProfile","inCustomerProfile")
                intent.putExtra("customerUsername", customerModel?.customerUsername)
                startActivity(intent)
            }else{
                val intent = Intent(this@SingleProfileActivity,ForgotPasswordActivity::class.java)
                intent.putExtra("customerOrHouseOwner","houseOwner")
                intent.putExtra("inSingleProfile","inHouseOwnerProfile")
                intent.putExtra("houseOwnerUsername", houseOwnerModel?.ownerUsername)
                startActivity(intent)
            }

        } else{
            Toast.makeText(this@SingleProfileActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

}