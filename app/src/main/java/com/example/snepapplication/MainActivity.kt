package com.example.snepapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.product.ProductItems
import com.example.snepapplication.productUser.AdapterProductUser
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var naveView: NavigationView

    lateinit var firestore:FirebaseFirestore
    lateinit var storage:FirebaseStorage
    lateinit var reference: StorageReference
    lateinit var data:ArrayList<ProductItems>
    lateinit var adaper:AdapterProductUser
    lateinit var rvGetUserProduct:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestore= Firebase.firestore
        storage= Firebase.storage
        reference= storage.reference
        data= ArrayList()

        rvGetUserProduct= findViewById(R.id.rvGetUserProduct)
        rvGetUserProduct.layoutManager= GridLayoutManager(this,2)

        getProductsUser()

         drawerLayout= findViewById<DrawerLayout>(R.id.drawerLayout)
         naveView= findViewById<NavigationView>(R.id.navigationViw)

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        naveView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile -> startActivity(Intent(this,UserProfileActivity::class.java))
                R.id.purchases -> startActivity(Intent(this, UserPurchasesActivity::class.java))
                R.id.search -> startActivity(Intent(this,SearchActivity::class.java))
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProductsUser(){
        firestore.collection("products").get()
            .addOnSuccessListener { querSnapshot ->
                for (document in querSnapshot){
                    document.data
                    val productItems= ProductItems(document.get("productName").toString(), document.get("productRate").toString(),
                        document.get("productDescription").toString(), document.get("productLocation").toString(),
                        document.get("productPrice").toString(),document.get("ProductImage").toString(), document.get("productCategory").toString())
                    data.add(productItems)
                }
                rvGetUserProduct.adapter=AdapterProductUser(this,data)
            }.addOnFailureListener {
                Toast.makeText(this, "failed to get products", Toast.LENGTH_SHORT).show()
                Log.e("byn","failed to get product to user")
            }
    }



}