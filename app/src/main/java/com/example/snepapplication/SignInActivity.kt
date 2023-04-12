package com.example.snepapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class SignInActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var progressBarIn:ProgressBar

    private lateinit var reference: StorageReference
    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        db= Firebase.firestore
        auth= Firebase.auth
        val edTPassIn= findViewById<EditText>(R.id.edTPassIn)
        val edTEmailIn= findViewById<EditText>(R.id.edTEmailIn)
        val btnSignIn= findViewById<Button>(R.id.btnSignIn)
        progressBarIn= findViewById(R.id.progressBarIn)

        storage= Firebase.storage
        reference= storage.reference

        val btnToSignup= findViewById<Button>(R.id.btnToSignup)
        btnToSignup.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        btnSignIn.setOnClickListener {
            signInUser(edTEmailIn.text.toString(),edTPassIn.text.toString())
        }

    }

    private fun signInUser(email:String, password:String){
        progressBarIn.visibility=View.VISIBLE
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener (this){task->
            if (task.isSuccessful){
            db.collection("users").whereEqualTo("id",auth.currentUser!!.uid).get()
                .addOnSuccessListener {querySnapshot ->
                    progressBarIn.visibility=View.GONE
                if(querySnapshot.documents.get(0).get("isUser") !=null){
                    startActivity(Intent(this,MainActivity::class.java))
                    Log.e("byn","user")
                }
                if (querySnapshot.documents.get(0).get("isAdmin") !=null){
                    startActivity(Intent(this,AdmenMainActivity::class.java))
                    Log.e("byn","admin")
                }
                progressBarIn.visibility= View.INVISIBLE
                Toast.makeText(this, "sign in successful", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                    progressBarIn.visibility= View.INVISIBLE
                Log.e("byn","${it.message}")
            }

            }else{
                progressBarIn.visibility= View.INVISIBLE
                Toast.makeText(this, "fail in sign in", Toast.LENGTH_SHORT).show()
            }
        }
    }


}