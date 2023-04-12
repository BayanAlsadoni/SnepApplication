package com.example.snepapplication.categories

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.ProductAdminActivity
import com.example.snepapplication.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterCategories(var activity:Activity, var data:ArrayList<CategoriesItems>):RecyclerView.Adapter<AdapterCategories.MyViewHolder>(){
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val catTv= itemView.findViewById<TextView>(R.id.catTv)
        val btnUpdateCat= itemView.findViewById<Button>(R.id.btnUpdateCat)
        val btndeletCat= itemView.findViewById<Button>(R.id.btnDeleteCat)
    }

    val db=Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root=LayoutInflater.from(activity).inflate(R.layout.categories_item,parent,false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.catTv.text= data[position].cateName
        holder.catTv.setOnClickListener {
            activity.startActivity(Intent(activity,ProductAdminActivity::class.java))
        }

        holder.btnUpdateCat.setOnClickListener {
            val dialog=Dialog(activity)
            dialog.setContentView(R.layout.dialog_update_cat)
            val btnUpCat= dialog.findViewById<Button>(R.id.btndialogUpdateCat)
            val edtUpCat= dialog.findViewById<EditText>(R.id.edtdialogUpdateCat)

            btnUpCat.setOnClickListener {
                db.collection("categories").whereEqualTo("categoryName",holder.catTv.text).get()
                    .addOnSuccessListener { querySnapshot ->
                        edtUpCat.setText(
                            querySnapshot.documents.get(0).get("categoryName").toString()
                        )
                    }
                // db firebase to update cat
                val cat= hashMapOf<String,Any>("categoryName" to edtUpCat.text.toString())
                db.collection("categories").whereEqualTo("categoryName",holder.catTv.text).get()
                    .addOnSuccessListener {
                        db.collection("categories").document(it.documents.get(0).id).update(cat)
                        holder.catTv.text= edtUpCat.text.toString()
                        Toast.makeText(activity, "update successfully", Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(activity, "update failed", Toast.LENGTH_SHORT).show()
                    }
                dialog.dismiss()
                notifyDataSetChanged()
            }
            dialog.show()
            notifyDataSetChanged()
        }

        holder.btndeletCat.setOnClickListener {
            val alertDialog= AlertDialog.Builder(activity)
            alertDialog.setTitle("Delete Category")
            alertDialog.setMessage("are you sure?")
            alertDialog.setPositiveButton("YES"){_,_ ->
                db.collection("categories").whereEqualTo("categoryName",holder.catTv.text).get()
                    .addOnSuccessListener { querySnapshot ->
                            //.update("categories",FieldValue.delete())
                                db.collection("categories").document(querySnapshot.documents.get(0).id).delete()
                                Log.e("byn","deleted successfully")
                                data.removeAt(position)
                                notifyDataSetChanged()
                                Toast.makeText(activity, "category deleted successfully", Toast.LENGTH_SHORT).show()

                            }.addOnFailureListener {
                                Log.e("byn","deleted faild")
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


//    fun deleteCategory(catName:String){
//        db.collection("categories").document(catName).delete()
//            //.update("categories",FieldValue.delete())
//            .addOnSuccessListener {
//                Log.e("byn","deleted successfully")
//            }.addOnFailureListener {
//                Log.e("byn","deleted faild")
//            }
//    }

}

