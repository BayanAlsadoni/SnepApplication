package com.example.snepapplication.product

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.R
import com.example.snepapplication.UpdateProductActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import java.util.*
import kotlin.collections.ArrayList

class AdapterProducts(var context:Context, var data:ArrayList<ProductItems>):RecyclerView.Adapter<AdapterProducts.MyViewHolder>() {
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val proImage= itemView.findViewById<ImageView>(R.id.proImage)
        val proName= itemView.findViewById<TextView>(R.id.proName)
        val proPrice= itemView.findViewById<TextView>(R.id.proPrice)
        val proDescription= itemView.findViewById<TextView>(R.id.proDescription)
        val proitcat= itemView.findViewById<TextView>(R.id.proitcat)
        val proLocation= itemView.findViewById<TextView>(R.id.proLocation)
        val proRate= itemView.findViewById<TextView>(R.id.proRate)
        val proDeleteBtn= itemView.findViewById<ImageButton>(R.id.proDeleteBtn)
        val proUpdatebtn= itemView.findViewById<ImageButton>(R.id.proUpdatebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root= LayoutInflater.from(context).inflate(R.layout.product_item,parent,false)
        return MyViewHolder(root)
    }

    val db= Firebase.firestore
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataItem= data[position]
        holder.proRate.text= dataItem.productRate
        holder.proName.text= dataItem.productName
        holder.proitcat.text= dataItem.productCat
        holder.proPrice.text= dataItem.productPrice.toString()
        holder.proDescription.text= dataItem.productDescription
        holder.proLocation.text= dataItem.productLocation
        Picasso.get().load(dataItem.productImage).into(holder.proImage)

        holder.proUpdatebtn.setOnClickListener{
            val i=Intent(context,UpdateProductActivity::class.java)
//            i.putExtra("name",dataItem.productName)
//            i.putExtra("name",dataItem.productRate)
//            i.putExtra("name",dataItem.productPrice)
//            i.putExtra("name",dataItem.productDescription)
//            i.putExtra("name",dataItem.productLocation)////u must do serializaple or parcelaple
            i.putExtra("product",dataItem)
            context.startActivity(i)

//            val dialog= Dialog(context)
//            dialog.setContentView(R.layout.dialog_update_product)
//            val btnUpPro= dialog.findViewById<Button>(R.id.btnUpPro)
//            val nameUpPro= dialog.findViewById<EditText>(R.id.nameUpPro)
//            val locationUpPro= dialog.findViewById<EditText>(R.id.locationUpPro)
//            val priceUpPro= dialog.findViewById<EditText>(R.id.priceUpPro)
//            val rateUpPro= dialog.findViewById<EditText>(R.id.rateUpPro)
//            val descrepionUpPro= dialog.findViewById<EditText>(R.id.descrepionUpPro)
//
//            val imgUpPro= dialog.findViewById<ImageView>(R.id.imgUpPro)
//
//            imgUpPro.setOnClickListener{
////                PickImageDialog.build(PickSetup()).show(context)
//            }
//
//            btnUpPro.setOnClickListener {//
//                db.collection("products").whereEqualTo("productName",holder.proName.text).get()
//                    .addOnSuccessListener { querySnapshot ->
////                        nameUpPro.setText(///
////                            querySnapshot.documents.get(0).get("productName").toString()
////                        )
//                            val productMap= hashMapOf<String,Any>("productName" to nameUpPro.text.toString(), "productPrice" to priceUpPro.text.toString(),
//                                "productLocation" to locationUpPro.text.toString(), "productDescription" to descrepionUpPro.text.toString(),
//                                "productRate" to rateUpPro.text.toString())
//
//                            db.collection("products").document(querySnapshot.documents.get(0).id).update(productMap)
//                            Toast.makeText(context, "update successfully", Toast.LENGTH_SHORT).show()
//
//                        dialog.dismiss()
//                        notifyDataSetChanged()
//                    }.addOnFailureListener {
//                        Toast.makeText(context, "update failed", Toast.LENGTH_SHORT).show()
//                    }
//        }
//        dialog.show()
//        notifyDataSetChanged()
//
        }

        holder.proDeleteBtn.setOnClickListener {
            val alertDialog= AlertDialog.Builder(context)
            alertDialog.setTitle("Delete product")
            alertDialog.setMessage("are you sure?")
            alertDialog.setPositiveButton("YES"){_,_ ->
                db.collection("products").whereEqualTo("productName",holder.proName.text).get()
                    .addOnSuccessListener { querySnapshot ->
                        db.collection("products").document(querySnapshot.documents.get(0).id).delete()
                        Log.e("byn","deleted successfully")
                        data.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "product deleted successfully", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Log.e("byn","deleted failed")
                    }
            }
            alertDialog.setNegativeButton("NO"){d,_ ->
                d.dismiss()
            }
            alertDialog.create().show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }





}