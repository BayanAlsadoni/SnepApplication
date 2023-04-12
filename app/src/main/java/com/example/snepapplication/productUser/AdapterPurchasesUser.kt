package com.example.snepapplication.productUser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.DetailsUserProductActivity
import com.example.snepapplication.R
import com.example.snepapplication.product.ProductItems
import com.squareup.picasso.Picasso

class AdapterPurchasesUser(var context: Context, var data:ArrayList<ProductItems>): RecyclerView.Adapter<AdapterPurchasesUser.MyViewHolder>() {
    class MyViewHolder(var view: View): RecyclerView.ViewHolder(view){
    val purchImage= view.findViewById<ImageView>(R.id.purchImage)
        val purchPrice= view.findViewById<TextView>(R.id.purchPrice)
        val purchLocation= view.findViewById<TextView>(R.id.purchLocation)
        val purchDescription= view.findViewById<TextView>(R.id.purchDescription)
        val purchRate= view.findViewById<TextView>(R.id.purchRate)
        val purchName= view.findViewById<TextView>(R.id.purchName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root= LayoutInflater.from(context).inflate(R.layout.user_purchases_item,parent,false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val d= data[position]
        holder.purchName.text= d.productName
        holder.purchPrice.text=d.productPrice
        holder.purchDescription.text=d.productDescription
        holder.purchRate.text=d.productRate
        holder.purchLocation.text=d.productLocation
        Picasso.get().load(d.productImage).into(holder.purchImage)
        holder.purchImage.setOnClickListener {
            val i= Intent(context,DetailsUserProductActivity::class.java)
            i.putExtra("product_details",d)
            context.startActivity(i)
        }
        //val i= Intent(context,)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}