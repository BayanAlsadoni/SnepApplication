package com.example.snepapplication

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.snepapplication.categories.CategoriesItems
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import java.util.*

class AddProductActivity : AppCompatActivity(),IPickResult {

    lateinit var db:FirebaseFirestore
    lateinit var storage:FirebaseStorage
    lateinit var reference: StorageReference
    lateinit var pathe:String
    lateinit var imageAddProduct:ImageView
    lateinit var addprospinner:Spinner
    private lateinit var progressDialog: ProgressDialog

    private var latitude: Double?=null
    private var longtude: Double?=null


    private val cat= arrayListOf<String>()
    private var itemcat=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val la= intent.getDoubleExtra("product_latitude",31.32535135135135)
        val lo= intent.getDoubleExtra("product_longitude",34.29015667840477)
        Log.e("bbb", la.toString()+"//"+lo.toString())

        val ll= Intent(this,ProductDetailesLocationMapActivity::class.java)
        ll.putExtra("lattpro",la)
        ll.putExtra("lontpro",lo)

        val prolocationbtn= findViewById<ImageButton>(R.id.prolocationbtn)
        prolocationbtn.setOnClickListener {
            val lp= Intent(this,ProductMapsActivity::class.java)
            startActivity(lp)

            //getLastLocation()
        }

        db= Firebase.firestore
        storage= Firebase.storage
        reference= storage.reference

        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

        addprospinner= findViewById(R.id.addprospinner)
        addprospinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                itemcat=cat[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        getCat()

        imageAddProduct= findViewById(R.id.imageAddProduct)
        val nameAddProduct= findViewById<EditText>(R.id.nameAddProduct)
        val locationAddProduct= findViewById<EditText>(R.id.locationAddProduct)
        val priceAddProduct= findViewById<EditText>(R.id.priceAddProduct)
        val descriptionAddProduct= findViewById<EditText>(R.id.discriptionAddProduct)
        val rateAddProduct= findViewById<EditText>(R.id.rateAddProduct)
        val buttonAddProduct= findViewById<Button>(R.id.buttonAddProduct)

        buttonAddProduct.setOnClickListener {
            addProduct(nameAddProduct.text.toString(), priceAddProduct.text.toString(),
                locationAddProduct.text.toString(),descriptionAddProduct.text.toString(),
            rateAddProduct.text.toString())
        }
        imageAddProduct.setOnClickListener {
            PickImageDialog.build(PickSetup()).show(this)
        }

    }

    override fun onPickResult(r: PickResult?) {
        imageAddProduct.setImageBitmap(r!!.bitmap)
        uploadImage(r.uri)
    }

    private fun uploadImage(uri:Uri){
        progressDialog.show()
        reference.child("productImage/"+UUID.randomUUID().toString()).putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                Toast.makeText(this, "image upload successfully", Toast.LENGTH_SHORT).show()
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    pathe= it.toString()
                    Log.e("byn",pathe)
                }
            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(this, "fail to upload image", Toast.LENGTH_SHORT).show()
                Log.e("byn","fail ${it.message}")
            }

    }


    private fun addProduct(productName:String, productPrice:String, productLocation:String,
                           productDescription:String, productRate:String){

        val la= intent.getDoubleExtra("product_latitude",31.32535135135135)
        val lo= intent.getDoubleExtra("product_longitude",34.29015667840477)


        progressDialog.show()
        val productMap= hashMapOf<String,Any>(
            "productName" to productName, "productPrice" to productPrice, "productAddress" to productLocation,
            "productDescription" to productDescription, "productRate" to productRate,
            "ProductImage" to pathe, "productNameSearch" to productName.lowercase(),
            "productCategory" to itemcat,"product_location" to GeoPoint(la,lo)
        )
        db.collection("products").add(productMap).addOnSuccessListener {
            db.collection("products").whereEqualTo("productName",productName).get()
            progressDialog.dismiss()
            Toast.makeText(this, "product added successfully", Toast.LENGTH_SHORT).show()

            finish()

        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this, "failed to add product", Toast.LENGTH_SHORT).show()
        }
    }// بالنسبة للصورة جطيت مكتبة البيكاسو في الادابتر وحطيت هين الصورة وكمان فنكشن فما بعرف يمكن يسبب مشاكل

    private fun getCat(){
        db.collection("categories").get().addOnSuccessListener {querySnapshot ->
            for (doc in querySnapshot){
                cat.add(doc.data.get("categoryName").toString())

            }
            addprospinner.adapter= ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cat)

        }
    }



    private fun getLastLocation(){//
        val locaton= LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locaton.lastLocation
            .addOnSuccessListener {ll ->
                if(ll != null){
                    latitude= ll.latitude
                    longtude= ll.longitude
                    Log.e("byn",longtude.toString())
                    Log.e("byn",latitude.toString())
                    Log.e("byn",ll.provider)
                }else{
                    Log.e("byn","location is null")
                    Log.e("byn",ll.toString())
                    Log.e("byn",ll?.latitude.toString())
                }
                Log.e("byn","success")
            }.addOnFailureListener {
                Log.e("byn","error in location")
            }
        Log.e("byn","end")

    }



}