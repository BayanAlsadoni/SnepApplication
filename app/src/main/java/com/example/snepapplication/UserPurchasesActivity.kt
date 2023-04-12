package com.example.snepapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.product.ProductItems
import com.example.snepapplication.productUser.AdapterPurchasesUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class UserPurchasesActivity : AppCompatActivity() {
    lateinit var rvPurchases:RecyclerView
    private lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var reference: StorageReference
    private lateinit var path:String
    private lateinit var data:ArrayList<ProductItems>
    lateinit var auth:FirebaseAuth
    //lateinit var i:ProductItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_purchases)
        rvPurchases=findViewById(R.id.rvPurchases)
        rvPurchases.layoutManager=LinearLayoutManager(this)

        db= Firebase.firestore
        storage= Firebase.storage
        reference= storage.reference
        auth= Firebase.auth

        // val i= intent.getParcelableExtra<ProductItems>("product_details")!!

        data= ArrayList()
        getPurchasesUser()

    }

    private fun getPurchasesUser(){
//        db.collection("users").get()
//            .addOnSuccessListener {snapshot ->
                //db.collection("users").document(auth.currentUser!!.uid).collection("purchases")
                db.collectionGroup("purchases")//get document that is inside collection that it is inside document that is inside collection
//                    .whereEqualTo("name",name)
                   .get()
                    .addOnSuccessListener {
                        for (d in it){
                            val s= d.data
                            val itemd=  ProductItems(s.get("name").toString(),s.get("rate").toString(),
                                s.get("description").toString(), s.get("location").toString(),
                                s.get("price").toString(),s.get("image").toString())
                            Log.e("byn",itemd.toString())
                            data.add(itemd)
                            Log.e("byn",data.toString())
                            Log.e("byn",d.id+"/////"+s)
                            Log.e("byn","0000")

                        }
                        rvPurchases.adapter= AdapterPurchasesUser(this,data)
                    }


//
//                for (document in snapshot){
//                    val doc= document.data.get("products_shopping")
//
//
//
//                   // Log.e("bbb", doc)
//val ll=db.collection("users").document("products_shopping").get()
//val k=    ll.getResult().get("name").toString()
//                    Log.e("bnn",k)
////                    ll.result
//                    Log.e("bbb", db.collection("users").document("products_shopping").get().toString())
//                   // Log.e("bbb", db.collection("users").document("products_shopping").get().toString())
//        //            Log.e("bbb",ll.result.toString())



            .addOnFailureListener {
                Log.e("byn","getPurchasesUser failed")
            }
    }

}