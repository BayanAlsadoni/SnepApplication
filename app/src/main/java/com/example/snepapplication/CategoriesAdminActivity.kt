package com.example.snepapplication

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.categories.AdapterCategories
import com.example.snepapplication.categories.CategoriesItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoriesAdminActivity : AppCompatActivity() {

    lateinit var db:FirebaseFirestore
    lateinit var auth:FirebaseAuth
    lateinit var adapter:AdapterCategories
    lateinit var rvGetCat:RecyclerView
    private lateinit var progressDialog: ProgressDialog
    val data= ArrayList<CategoriesItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_admin)

        val btnAddCat= findViewById<Button>(R.id.btnAddCat)
        db= Firebase.firestore
        auth=Firebase.auth

        rvGetCat=findViewById(R.id.rvGetCat)

        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

        btnAddCat.setOnClickListener {
            val dialog= Dialog(this)
            dialog.setContentView(R.layout.dialog_add_cat)
            val btnAddDialog= dialog.findViewById<Button>(R.id.btndialogAddCat)
            val edtAddCatName= dialog.findViewById<EditText>(R.id.edtAddCatName)
            btnAddDialog.setOnClickListener {
                addCategory(edtAddCatName.text.toString())
                dialog.dismiss()
            }
            dialog.show()

        }
        getCategory()
       // deleteCategory("books")
    }

    private fun addCategory( categoryName:String){
        progressDialog.show()
        val category= hashMapOf("categoryName" to categoryName)
        db.collection("categories").add(category).addOnSuccessListener {
            Toast.makeText(this, "category added successfully", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }.addOnFailureListener {
            Toast.makeText(this, "fail to add category", Toast.LENGTH_SHORT).show()
            Log.e("byn",it.toString())
            progressDialog.dismiss()
        }
    }

    private fun getCategory(){
        progressDialog.show()
        db.collection("categories").get()
            .addOnSuccessListener { querySnapshot ->
                for(document in querySnapshot.documents){
                    val doc=document.data
                    val dt= CategoriesItems(doc!!.get("categoryName").toString())
                    data.add(dt)
                }
                progressDialog.dismiss()
                adapter= AdapterCategories(this,data)
                adapter.notifyDataSetChanged()
                rvGetCat.adapter= adapter
                rvGetCat.layoutManager=LinearLayoutManager(this)

            }.addOnFailureListener {
                progressDialog.dismiss()
                Log.e("byn",it.toString())
                Toast.makeText(this, "fail to get categories", Toast.LENGTH_SHORT).show()
            }
    }
    fun deleteCategory(catName:String){
        db.collection("categories").document(catName).delete()
            //.update("categories",FieldValue.delete())
            .addOnSuccessListener {
                Log.e("byn","deleted successfully")
            }.addOnFailureListener {
                Log.e("byn","deleted faild")
            }
    }

    fun updateCategory(cateName:String){
        val cat= hashMapOf<String,Any>()
        cat["categoryName"]=""
        db.collection("categories").document(cateName).update(cat)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }

    }


}