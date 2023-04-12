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

class AdapterProductUser(var context: Context, var data:ArrayList<ProductItems>):RecyclerView.Adapter<AdapterProductUser.MyViewHolder>() {
    class MyViewHolder(var view:View):RecyclerView.ViewHolder(view){
        val userProductImage= view.findViewById<ImageView>(R.id.userProductImage)
        val userProductName= view.findViewById<TextView>(R.id.userProductName)
        val userProductPrice= view.findViewById<TextView>(R.id.userProductPrice)
        val userproductcat= view.findViewById<TextView>(R.id.userproductcat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val root= LayoutInflater.from(context).inflate(R.layout.user_product_item,parent,false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itm= data[position]
        holder.userProductName.text=itm.productName
        holder.userproductcat.text=itm.productCat
        holder.userProductPrice.text=itm.productPrice
        Picasso.get().load(itm.productImage).into(holder.userProductImage)
        holder.userProductImage.setOnClickListener{
            val i= Intent(context,DetailsUserProductActivity::class.java)
            i.putExtra("product_details",itm)
            i.putExtra("product_details_cat",itm.productCat)
            i.putExtra("product_details_address",itm.productLocation)
            context.startActivity(i)

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}