package com.example.snepapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class AdmenMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admen_main)

        val p= intent.getIntExtra("productsPurchasedCount",0)
        val q= intent.getIntExtra("countPro",0)
        val r=intent.getStringExtra("product_name")

        val allPurchasesCount= findViewById<TextView>(R.id.allPurchasesCount)
        val productnametv= findViewById<TextView>(R.id.productnametv)
        val certainproductcounttv= findViewById<TextView>(R.id.certainproductcounttv)
        allPurchasesCount.text= p.toString()
        productnametv.text=r
        certainproductcounttv.text=q.toString()
        Log.e("cont admin",p.toString())

        val btnCategories= findViewById<Button>(R.id.btnCategories)
        btnCategories.setOnClickListener {
            startActivity(Intent(this,CategoriesAdminActivity::class.java))
        }
        val btnProduct= findViewById<Button>(R.id.btnProducts)
        btnProduct.setOnClickListener {
            startActivity(Intent(this, ProductAdminActivity::class.java))
        }

    }
}