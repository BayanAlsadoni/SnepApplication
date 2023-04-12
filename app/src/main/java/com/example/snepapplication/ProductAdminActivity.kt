package com.example.snepapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.product.AdapterProducts
import com.example.snepapplication.product.ProductItems
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.listeners.IPickResult
import java.util.*
import kotlin.collections.ArrayList

class ProductAdminActivity : AppCompatActivity()/*,IPickResult*/ {
    private lateinit var rvProducts:RecyclerView
    private lateinit var adap:AdapterProducts
    private lateinit var data:ArrayList<ProductItems>
    private lateinit var db:FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var reference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_admin)

        db= Firebase.firestore
        storage=Firebase.storage
        reference=storage.reference

        data= ArrayList<ProductItems>()
        val btnAddProduct= findViewById<Button>(R.id.btnAddProduct)
         rvProducts = findViewById(R.id.rvProducts)
        rvProducts.layoutManager= LinearLayoutManager(this)

        btnAddProduct.setOnClickListener {
         startActivity(Intent(this, AddProductActivity::class.java))
        }
        Log.e("byn","${getAllProducts()}")

    }

    private fun getAllProducts(){
        db.collection("products").get().addOnSuccessListener { snapshot ->
            for (document in snapshot){
                val doc= document.data
                val productItems= ProductItems(doc.get("productName").toString(),
                    doc.get("productRate").toString(),doc.get("productDescription").toString(),
                    doc.get("productAddress").toString(),doc.get("productPrice").toString(),
                    doc.get("ProductImage").toString(),doc.get("productCategory").toString())
                data.add(productItems)
            }
            adap= AdapterProducts(this,data)
            rvProducts.adapter= adap
        }.addOnFailureListener {
            Toast.makeText(this, "fail to get products", Toast.LENGTH_SHORT).show()
            Log.e("byn","${it.message}")
        }
    }


//    override fun onPickResult(r: PickResult?) {
//        imgUpPro.setImageBitmap(r!!.bitmap)
//        uploadImage(r.uri)
//    }
//
//    private fun uploadImage(uri: Uri){
//        reference.child("productImage/"+ UUID.randomUUID().toString()).putFile(uri)
//            .addOnSuccessListener { taskSnapshot ->
//                Toast.makeText(this, "image upload successfully", Toast.LENGTH_SHORT).show()
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
//                    path= it.toString()
//                }
//            }.addOnFailureListener{
//                Toast.makeText(this, "fail to upload image", Toast.LENGTH_SHORT).show()
//                Log.e("byn","fail ${it.message}")
//            }
//    }



}