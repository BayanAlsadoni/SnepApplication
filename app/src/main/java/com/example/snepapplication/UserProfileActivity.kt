package com.example.snepapplication

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

class UserProfileActivity : AppCompatActivity(), IPickResult {
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    private lateinit var reference: StorageReference

    lateinit var path:String
    private lateinit var progressDialog: ProgressDialog

    lateinit var userProfilePhone:EditText
    lateinit var userProfileAddress:EditText
    lateinit var userProfileName:EditText
    lateinit var userProfileEmail:EditText
    lateinit var userProfileImage:ImageView

    private var latitude: Double?=null
    private var longtude: Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val btnUserProfile=findViewById<Button>(R.id.btnUserProfile)
        val locationbtn= findViewById<ImageButton>(R.id.locationbtn)
        locationbtn.setOnClickListener {
            getLastLocation()
        }

        auth= Firebase.auth
        db= Firebase.firestore
        storage= Firebase.storage
        reference= storage.reference

        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

         userProfileImage=findViewById(R.id.userProfileImage)
         userProfileEmail= findViewById(R.id.userProfileEmail)
         userProfileName= findViewById(R.id.userProfileName)
         userProfileAddress= findViewById(R.id.userProfileAddress)
         userProfilePhone= findViewById(R.id.userProfilePhone)



        userProfileImage.setOnClickListener{
            PickImageDialog.build(PickSetup()).show(this)
        }
        btnUserProfile.setOnClickListener {
            updateUserInformation()
        }
        getUserProfile()

    }

    private fun getUserProfile(){
        db.collection("users").whereEqualTo("id",auth.currentUser!!.uid).get()
            .addOnSuccessListener {querySnapshot ->
                userProfileEmail.setText(querySnapshot.documents.get(0).get("email").toString())
                userProfileName.setText(querySnapshot.documents.get(0).get("name").toString())
                userProfileAddress.setText(querySnapshot.documents.get(0).get("address").toString())
                userProfilePhone.setText(querySnapshot.documents.get(0).get("phone").toString())
                Picasso.get().load(querySnapshot.documents.get(0).get("image").toString()).into(userProfileImage)
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "fail to get user $exception", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onPickResult(r: PickResult?) {
        userProfileImage.setImageBitmap(r!!.bitmap)
        uploadImageUserProfile(r.uri)
    }

    private fun uploadImageUserProfile(uri: Uri){
        progressDialog?.show()
        reference.child("userProfile/"+ UUID.randomUUID().toString()).putFile(uri)
            .addOnSuccessListener{taskSnapshot ->
                progressDialog?.hide()
                Toast.makeText(this, "image upload successfully", Toast.LENGTH_SHORT).show()

                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    path= uri.toString()
                }

            }.addOnFailureListener{exception ->
                progressDialog?.hide()
                Toast.makeText(this, "fail to upload image", Toast.LENGTH_SHORT).show()
                Log.e("byn","fail $exception")
            }
    }

    private fun updateUserInformation(){
        db.collection("users").whereEqualTo("id",auth.currentUser!!.uid).get()
            .addOnSuccessListener { snapshot->
                val hash:Map<String,Any> = hashMapOf("name" to userProfileName.text.toString(),
                    "email" to userProfileEmail.text.toString(),/*,"image" to path,//is img path true?*/
                    "address" to userProfileAddress.text.toString(), "phone" to userProfilePhone.text.toString(),
                    "geoPoint" to GeoPoint(latitude!!,longtude!!))
                db.collection("users").document(snapshot.documents.get(0).id).update(hash)
                    .addOnSuccessListener { finish() }//
                Toast.makeText(this, "update successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
    }// tي مشكلة ما بيسترجع الصورة وانما بس بجيبها فاضية لكن بالفيرستور موجودة



    private fun getLastLocation(){//
        val locaton= LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
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