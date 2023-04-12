package com.example.snepapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.snepapplication.product.ProductItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

var count=0
var countPro=0

class DetailsUserProductActivity : AppCompatActivity() {
    lateinit var db:FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var reference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_user_product)

        db= Firebase.firestore
        auth= Firebase.auth
        storage=Firebase.storage
        reference= storage.reference

        val pathlocation= findViewById<ImageButton>(R.id.pathlocation)
        pathlocation.setOnClickListener {
            startActivity(Intent(this, ProductDetailesLocationMapActivity::class.java))
        }

        val i= intent.getParcelableExtra<ProductItems>("product_details")
        val cati= intent.getStringExtra("product_details_cat")

        Log.e("bj",cati!!)

        val userProImg= findViewById<ImageView>(R.id.userProImg)
        val userProName= findViewById<TextView>(R.id.userProName)
        val userProRaite= findViewById<TextView>(R.id.userProRaite)
        val userProPrice= findViewById<TextView>(R.id.userProPrice)
        val userprodcat= findViewById<TextView>(R.id.userprodcat)
        val userProLocation= findViewById<TextView>(R.id.userProLocation)
        val userProDescription= findViewById<TextView>(R.id.userProDescription)
        val shopbtn= findViewById<Button>(R.id.shopbtn)

        userProName.setText(i!!.productName)
        userProDescription.setText(i.productDescription)
        userProLocation.setText(i.productLocation)
        userProPrice.setText(i.productPrice)
        userProRaite.setText(i.productRate)
        userprodcat.setText(cati.toString())
        Picasso.get().load(i.productImage).into(userProImg)

        val o= Intent(this,AdmenMainActivity::class.java)

        //  var count=0
        shopbtn.setOnClickListener {
            count++
            Log.e("byn count",count.toString())
            addProductUser(i!!.productName,i.productDescription,i.productPrice,i.productRate,i.productImage,i.productLocation,i.productCat)
            o.putExtra("countPro", countPro)
            o.putExtra("product_name", i!!.productName)
            Log.e("bbb",i.productCat)

        }

        o.putExtra("productsPurchasedCount", count)

        /*db.collection("users").document(it.documents.get(0).id)
            .collection("purchases").add(pro)
*/
//
//        val vc= count-1
//        db.collection("productsPurchasedCount").whereEqualTo("all_purchased", vc).get().addOnSuccessListener {
//            val hc= hashMapOf<String,Any>("all_purchased" to count)
//            db.collection("productsPurchasedCount")
//                .document(it.documents.get(0).id)
//                .update(hc).addOnSuccessListener {
//                    Log.e("byn","count updated successfully $count")
//                }
//        }


    }

    private fun addProductUser(name:String,description:String,price:String,rate:String, image:String,location:String, category:String){
        var c=0
        val pro= hashMapOf<String,Any>("name" to name, "description" to description, "rate" to rate, "price" to price,
            "productAddress" to location, "image" to image,"productCategory" to category)
        db.collection("users").whereEqualTo("id",auth.currentUser!!.uid).get()
            .addOnSuccessListener {
               // db.collection("users").document(it.documents.get(0).id).update("products_shopping",pro)
              //  db.collection("users").document().set(pro)
                db.collection("users").document(it.documents.get(0).id)
                    .collection("purchases").add(pro)//.document("message1")
                    .addOnSuccessListener {
//                        val oi= Intent(this,AdmenMainActivity::class.java)
//                        oi.putExtra(name,c++)
                        Toast.makeText(this, "shopping successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }.addOnFailureListener {
                Log.e("byn","addProductUser failed")
            }

    }




}