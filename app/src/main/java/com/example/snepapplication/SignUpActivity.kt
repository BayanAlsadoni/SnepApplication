package com.example.snepapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var progressBarUp: ProgressBar
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore // this is type

    private lateinit var edTNameUp: EditText
    private lateinit var edTAddressUp: EditText
    private lateinit var edTPhonUp: EditText
    lateinit var edTEmailUp: EditText
    lateinit var edTpassUp: EditText
    lateinit var locationClient: FusedLocationProviderClient
     private var latitude: Double?=null
     private var longtude: Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        progressBarUp= ProgressBar(this)

         locationClient=LocationServices.getFusedLocationProviderClient(this)

        auth= Firebase.auth
        db= Firebase.firestore
        edTAddressUp= findViewById(R.id.aderessUp)
        edTEmailUp= findViewById(R.id.emailUp)
        edTPhonUp= findViewById(R.id.phoneUp)
        edTNameUp= findViewById(R.id.nameUp)
        edTpassUp= findViewById(R.id.passUp)
        val btnlocationup=findViewById<ImageButton>(R.id.btnlocationup)


        val btnSignUp= findViewById<Button>(R.id.btnSignUp)
        val btnSign= findViewById<Button>(R.id.btnSign)
        btnSign.setOnClickListener{
            val btnToSignup= findViewById<Button>(R.id.btnToSignup)
            btnToSignup.setOnClickListener{
                startActivity(Intent(this,SignInActivity::class.java))
            }
        }
        btnSignUp.setOnClickListener {
            createNewUser(edTEmailUp.text.toString(),edTpassUp.text.toString())
        }

        btnlocationup.setOnClickListener {
            getLastLocation()
        }

        val o= Intent(this,ProductDetailesLocationMapActivity::class.java)
        o.putExtra("laa",latitude)
        o.putExtra("loo",longtude)

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
//                Log.e("byn",ll.latitude.toString())
                Log.e("byn","success")
            }.addOnFailureListener {
                Log.e("byn","error in location")
            }
        Log.e("byn","end")

    }//

    private fun createNewUser(email:String, password:String){//authentication
        progressBarUp.visibility=View.VISIBLE
        val ge= GeoPoint(latitude!!,longtude!!)
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener (this){ task ->
            if (task.isSuccessful){
                progressBarUp.visibility= View.GONE
                val user= auth.currentUser
                addUser(user!!.uid,edTNameUp.text.toString(),edTEmailUp.text.toString(), edTAddressUp.text.toString(),
                    edTPhonUp.text.toString(),
                    ge)//
                Log.e("byn",GeoPoint(latitude!!,longtude!!).toString())//

                Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
                val i= Intent(this, MainActivity::class.java)
                startActivity(i)
            }else{
                progressBarUp.visibility= View.GONE
                Toast.makeText(this, "Fail In Sign Up", Toast.LENGTH_SHORT).show()
                Log.e("byn","fail to create new user")
                Log.e("byn",ge.toString())
            }

        }
    }

    private fun addUser(id:String, name:String,email: String, address:String, phone:String,geoPoint: GeoPoint){//firestore
        val user= hashMapOf("id" to id, "name" to name, "email" to email, "address" to address,
            "phone" to  phone,
            "geoPoint" to geoPoint, "isUser" to "1")//
        db.collection("users").add(user).addOnSuccessListener { documentReference ->
            // documentReference.id   // this is the random id uto generated when the insert operation success
            Toast.makeText(this, "user added successfully", Toast.LENGTH_SHORT).show()
            Log.e("byn","user added successfully")
            progressBarUp.visibility= View.INVISIBLE
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "fail to add user", Toast.LENGTH_SHORT).show()
            Log.e("byn","fail add user")
        }
    }

}