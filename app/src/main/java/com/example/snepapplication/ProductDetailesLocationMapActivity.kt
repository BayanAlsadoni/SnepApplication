package com.example.snepapplication

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.snepapplication.databinding.ActivityProductDetailesLocationMapBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolylineOptions

class ProductDetailesLocationMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityProductDetailesLocationMapBinding

    private var latitude: Double?=null
    private var longtude: Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLastLocation()
        binding = ActivityProductDetailesLocationMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
       // getLastLocation()
        // Add a marker in Sydney and move the camera

        val laa= intent.getDoubleExtra("laa",31.37535135135135)
        val loo= intent.getDoubleExtra("loo",34.28015667840477)

        val sydney = LatLng(laa, loo)
        mMap.addMarker(MarkerOptions().position(sydney).title("your location"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10f))


        val la=intent.getDoubleExtra("lattpro",31.32535135135135)
        val lo=intent.getDoubleExtra("lontpro",34.29015667840477)


        val product = LatLng(la!!, lo!!)
        mMap.addMarker(MarkerOptions().icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ROSE)).position(product).title("product location"))


        mMap.addPolyline(
            PolylineOptions()
                .add(sydney)
                .add(product)
                .color(Color.CYAN)
                .visible(true)
        )


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


}