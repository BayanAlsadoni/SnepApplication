package com.example.snepapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.snepapplication.product.ProductItems
import com.google.firebase.firestore.FirebaseFirestore
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

class UpdateProductActivity : AppCompatActivity(),IPickResult {

   private lateinit var db:FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var reference: StorageReference
    private  var path:String?=null
    private  lateinit var spinnerupdatepro:Spinner

    lateinit var btnUpPro:Button
    lateinit var nameUpPro:EditText
    lateinit var locationUpPro:EditText
    lateinit var priceUpPro:EditText
    lateinit var rateUpPro:EditText
    lateinit var descrepionUpPro:EditText
    lateinit var imgUpPro:ImageView
    private val cat= arrayListOf<String>()
    private var itemcat=""

    lateinit var pro:ProductItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)
        db= Firebase.firestore
        storage= Firebase.storage
        reference= storage.reference

        pro= intent.getParcelableExtra<ProductItems>("product")!!

         btnUpPro= findViewById(R.id.btnUpPro)
         nameUpPro=findViewById(R.id.nameUpPro)
         locationUpPro= findViewById(R.id.locationUpPro)
         priceUpPro= findViewById(R.id.priceUpPro)
         rateUpPro= findViewById(R.id.rateUpPro)
         descrepionUpPro=findViewById(R.id.descrepionUpPro)
         imgUpPro= findViewById(R.id.imgUpPro)

        spinnerupdatepro=findViewById(R.id.spinnerupdatepro)

        spinnerupdatepro.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                itemcat=cat[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        UpdateCat()
//////////////////
//        db.collection("products").whereEqualTo("productName",pro.productName).get()
//            .addOnSuccessListener { querySnapshot ->
//
//            }
        getpro()

//نستقبل الببيانات لحتى نحطها بال edt
//        Picasso.get().load(pro!!.productImage).into(imgUpPro)
//        priceUpPro.setText(pro.productPrice)
//        nameUpPro.setText(pro.productName)
//        locationUpPro.setText(pro.productLocation)
//        rateUpPro.setText(pro.productRate)
//        descrepionUpPro.setText(pro.productDescription)
//        path= pro.productImage


        imgUpPro.setOnClickListener{
            PickImageDialog.build(PickSetup()).show(this)
        }
        btnUpPro.setOnClickListener {
            updateProduct()
        }

    }

    private fun getpro(){
        db.collection("products").whereEqualTo("productName",pro.productName).get()
            .addOnSuccessListener {querySnapshot ->
                nameUpPro.setText(querySnapshot.documents.get(0).get("productName").toString())
                locationUpPro.setText(querySnapshot.documents.get(0).get("productAddress").toString())
                priceUpPro.setText(querySnapshot.documents.get(0).get("productPrice").toString())
                rateUpPro.setText(querySnapshot.documents.get(0).get("productRate").toString())
                descrepionUpPro.setText(querySnapshot.documents.get(0).get("productDescription").toString())
                Picasso.get().load(querySnapshot.documents.get(0).get("ProductImage").toString()).into(imgUpPro)
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "fail to get user $exception", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onPickResult(r: PickResult?) {
        imgUpPro.setImageBitmap(r!!.bitmap)
        uploadImage(r.uri)
    }

    private fun uploadImage(uri: Uri){
        reference.child("productImage/"+ UUID.randomUUID().toString()).putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "image upload successfully", Toast.LENGTH_SHORT).show()
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    path= it.toString()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "fail to upload image", Toast.LENGTH_SHORT).show()
                Log.e("byn","fail ${it.message}")
            }
    }


    private fun updateProduct(){

        db.collection("products").whereEqualTo("productName",pro.productName).get()
            .addOnSuccessListener { querySnapshot ->
                val productMap= hashMapOf<String,Any>("productName" to nameUpPro.text.toString()
                    , "productPrice" to priceUpPro.text.toString(),
                    "productAddress" to locationUpPro.text.toString(),
                    "productDescription" to descrepionUpPro.text.toString(),
                    "productRate" to rateUpPro.text.toString(),//"ProductImage" to path!!,
                    "productNameSearch" to nameUpPro.text.toString().lowercase(),
                    "productCategory" to itemcat
                   /* "ProductImage" to Picasso.get().load(imgUpPro).into("ProductImage")*/)

                db.collection("products").document(querySnapshot.documents.get(0).id)
                    .update(productMap)
                    .addOnSuccessListener { finish() }
                Toast.makeText(this, "update successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show()

       }
    }

    private fun UpdateCat(){
        db.collection("categories").get().addOnSuccessListener {querySnapshot ->
            for (doc in querySnapshot){
                cat.add(doc.data.get("categoryName").toString())

            }
            spinnerupdatepro.adapter= ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cat)

        }
    }





}