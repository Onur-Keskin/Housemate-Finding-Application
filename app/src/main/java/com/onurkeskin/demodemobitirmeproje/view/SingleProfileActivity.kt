package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.model.HouseOwnerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import com.onurkeskin.demodemobitirmeproje.service.HouseOwnerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_single_profile.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SingleProfileActivity : AppCompatActivity() /*, CustomerSingleProfileRecyclerViewAdapter.Listener*/ {
    private val BASE_URL = "http://192.168.1.21:8080/"
    private var isCustomer: String?=null
    private var isHouseOwner: String?=null
    private var isLoginUser : String? = null
    private var customerModel : CustomerModel? = null
    private var houseOwnerModel : HouseOwnerModel?=null
    private var compositeDisposable : CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_profile)

        compositeDisposable = CompositeDisposable()


        loadData()

    }
    /*
    private fun bindRecyclerView()
    {
        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        userLoginRecyclerView.layoutManager = layoutManager

    }

     */

    private fun loadData(){
        val intent = intent

        isCustomer = intent.getStringExtra("fromCustomer")
        isHouseOwner = intent.getStringExtra("fromHouseOwner")
        isLoginUser = intent.getStringExtra("fromMainPage")
        if (isCustomer.equals("customerProfile")){//eğer customer kısmından profile gidilecekse
            val id = intent.getIntExtra("customerId",1)
            val customerId : String? = "$id"
            singleProfileUpdateInfoButton.isVisible = false

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            if (customerId != null){

                compositeDisposable?.add(retrofit.getCustomerById(customerId)
                    .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                    .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                    .subscribe(this::handleResponse))
            }
        }
        else if(isHouseOwner.equals("houseOwnerProfile")){
            val id = intent.getIntExtra("houseOwnerId",1)
            val houseOwnerId : String? = "$id"
            singleProfileUpdateInfoButton.isVisible = false

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HouseOwnerAPI::class.java)

            if(houseOwnerId!=null){
                compositeDisposable?.add(retrofit.getOwnerById(houseOwnerId)
                    .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                    .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                    .subscribe(this::handleResponse))
            }

        }


        else if(isLoginUser.equals("userLoginProfile")){
            val id = intent.getIntExtra("userLoginId",1)
            val customerId : String? = "$id"

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(CustomerAPI::class.java)


            if (customerId != null){

                compositeDisposable?.add(retrofit.getCustomerById(customerId)
                    .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
                    .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
                    .subscribe(this::handleResponse))
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

    private fun handleResponse(customer: CustomerModel){
        customerModel = customer

        if(customerModel != null){
            singleProfileNameSurnameAge.text = customerModel!!.customerName + customer!!.customerSurname +  " , " + customer!!.customerAge.toString()
            singleProfileUsername.text = customerModel!!.customerUserName
            singleProfileHometown.text = customerModel!!.customerHometown
            singleProfileDepartmentGrade.text = customerModel!!.customerDepartment + " , " +customerModel!!.customerGrade.toString()
            singleProfilePhone.text = customerModel!!.customerPhone
            singleProfileEmail.text = customerModel!!.customerEmail
            singleProfileGender.text = customerModel!!.customerGender
        }else{
            Toast.makeText(this,"Error happened" , Toast.LENGTH_LONG).show()
        }


        /*
        customerModel?.let {
            customerSingleProfileRecyclerViewAdapter = CustomerSingleProfileRecyclerViewAdapter(it)
            userLoginRecyclerView.adapter = customerSingleProfileRecyclerViewAdapter
        }

         */


    }

    private fun handleResponse(houseOwner: HouseOwnerModel){
        houseOwnerModel = houseOwner
        if(houseOwnerModel != null){
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
        /*
        houseOwnerModel?.let {
            houseOwnerSingleProfileRecyclerViewAdapter = HouseOwnerSingleProfileRecyclerViewAdapter(it)
            userLoginRecyclerView.adapter = houseOwnerSingleProfileRecyclerViewAdapter
        }

         */

    }




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
        menuInflater.inflate(R.menu.category_menu,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val xD = "Ayalarlar sayfasına gider"
        }
        else if(item.itemId == R.id.logout){
            val intent = Intent(this@SingleProfileActivity,finish()::class.java)
            startActivity(intent)
            //onDestroy()
        }
        else{
            Toast.makeText(this@SingleProfileActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

}