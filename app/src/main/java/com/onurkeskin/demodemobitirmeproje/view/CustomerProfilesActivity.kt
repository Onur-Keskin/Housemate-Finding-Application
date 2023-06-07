package com.onurkeskin.demodemobitirmeproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkeskin.demobitirmeproje.R
import com.onurkeskin.demodemobitirmeproje.adapter.CustomerRecyclerViewAdapter
import com.onurkeskin.demodemobitirmeproje.globalvariables.GlobalVariables
import com.onurkeskin.demodemobitirmeproje.model.CustomerModel
import com.onurkeskin.demodemobitirmeproje.service.CustomerAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_customer_profiles.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CustomerProfilesActivity : AppCompatActivity(), CustomerRecyclerViewAdapter.Listener {
    private var customerModels : ArrayList<CustomerModel>? = null
    private var customerRecyclerViewAdapter : CustomerRecyclerViewAdapter? = null

    //Disposable -> Tek kullanımlık-Kullan At
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profiles)

        compositeDisposable = CompositeDisposable()

        //RecyclerView bağlama
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        customersRecyclerView.layoutManager = layoutManager
        loadData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.customer_menu,menu)

        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                //println("onQueryTextChange query : " + query)
                customerRecyclerViewAdapter?.filter?.filter(query)
                return true
            }

        })
        item.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                Toast.makeText(this@CustomerProfilesActivity,"Action Expand", Toast.LENGTH_LONG).show()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                customerRecyclerViewAdapter?.filter?.filter("")
                Toast.makeText(this@CustomerProfilesActivity,"Action Collapse", Toast.LENGTH_LONG).show()
                return true
            }

        })


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.customer_page_settings){
            val xD = "Ayalarlar sayfasına gider"
        }
        else if(item.itemId == R.id.customer_page_logout){
            val intent = Intent(this@CustomerProfilesActivity,finish()::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this@CustomerProfilesActivity,"Some Errors Happened", Toast.LENGTH_LONG).show()
        }
        return super.onContextItemSelected(item)
    }


    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalVariables.globalBASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CustomerAPI::class.java)

        compositeDisposable?.add(retrofit.getCustomers()
            .subscribeOn(Schedulers.io())//asenkron bir şekilde ana thread'i bloklamadan işlem yapılacak
            .observeOn(AndroidSchedulers.mainThread())//fakat veri main thread'de işlenecek
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(customerList: List<CustomerModel>){
        customerModels = ArrayList(customerList)

        customerModels?.let {
            customerRecyclerViewAdapter = CustomerRecyclerViewAdapter(it,this@CustomerProfilesActivity)
            customersRecyclerView.adapter = customerRecyclerViewAdapter
        }
    }

    override fun onItemClick(customerModel: CustomerModel) {
        //Toast.makeText(this,"Clicked : ${customerModel.customerId}",Toast.LENGTH_LONG).show()
        val intent = Intent(this@CustomerProfilesActivity,SingleProfileActivity::class.java)
        intent.putExtra("fromCustomer","customerProfile")
        intent.putExtra("customerId",customerModel.customerId)
        startActivity(intent)
        //println(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }





}