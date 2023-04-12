package com.example.snepapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snepapplication.product.AdapterProducts
import com.example.snepapplication.product.ProductItems
import com.example.snepapplication.productUser.AdapterPurchasesUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class SearchActivity : AppCompatActivity() {
    private lateinit var rvProducts: RecyclerView
    private lateinit var adap: AdapterProducts
    private lateinit var data:ArrayList<ProductItems>
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var reference: StorageReference
    private lateinit var rvSearch: RecyclerView
    private lateinit var etSearch:EditText
    private lateinit var dat:ArrayList<ProductItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        db= Firebase.firestore
        storage= Firebase.storage
        reference=storage.reference

        rvSearch= findViewById(R.id.rvSearch)
        etSearch= findViewById(R.id.etSearch)
        dat= ArrayList()

        rvSearch.hasFixedSize()
        rvSearch.layoutManager= LinearLayoutManager(this)

        etSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val st= etSearch.text.toString()
                 searchInFirestore(st.lowercase())
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    private fun searchInFirestore(searchText:String){
        db.collection("products").orderBy("productNameSearch")
            .startAt(searchText).endAt("$searchText\uf8ff").get()
            .addOnCompleteListener {
//                val lll= hashMapOf<String,Any>()
//                dat= it.result.toObjects(ProductItems::class.java) as ArrayList<ProductItems>//
//                rvSearch.adapter =AdapterPurchasesUser(this,dat)
//

                if (searchText.isEmpty()){
                    val g= ArrayList<ProductItems>()
                    rvSearch.adapter=AdapterPurchasesUser(this,g)
                    Log.e("byn","text search is empty:")
                }else{
                    for (document in it.result){
                        document.data
                        val productItems= ProductItems(document.get("productName").toString(), document.get("productRate").toString(),
                            document.get("productDescription").toString(), document.get("productLocation").toString(),
                            document.get("productPrice").toString(),document.get("ProductImage").toString())
                        dat.add(productItems)
                    }
                    rvSearch.adapter =AdapterPurchasesUser(this,dat)
                    Log.e("byn","successful to search data:")
                }
            }
//            .addOnSuccessListener {
//                if (searchText.isEmpty()){
//                    val g= ArrayList<ProductItems>()
//                    rvSearch.adapter=AdapterPurchasesUser(this,g)
//                    Log.e("byn","text search is empty:")
//                }else{
//                    for (document in it){
//                        document.data
//                        val productItems= ProductItems(document.get("productName").toString(), document.get("productRate").toString(),
//                            document.get("productDescription").toString(), document.get("productLocation").toString(),
//                            document.get("productPrice").toString(),document.get("ProductImage").toString())
//                        dat.add(productItems)
//                    }
//                    rvSearch.adapter =AdapterPurchasesUser(this,dat)
//                    Log.e("byn","successful to search data:")
//                }


//                val lll= hashMapOf<String,Any>()
//                dat= it.toObjects(ProductItems::class.java) as ArrayList<ProductItems>//


//            }.addOnFailureListener {
//                Log.e("byn","fail to search data: ${it.message}")
//            }
    }

//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_menu,menu)
//        val item= menu!!.findItem(R.id.action_search)
//        val searchView= MenuItemCompat.getActionProvider(item) as SearchView//if sth wrong just convert the shearchview to another shearchview
//        searchView.setOnQueryTextFocusChangeListener{p,o ->
//           // searchData(p)
//        }
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    private fun searchData(s:String){
//        db.collection("products").whereEqualTo("productNameSearch",s.lowercase()).get()
//            .addOnSuccessListener { querySnapshot ->
//
//
//            }.addOnFailureListener {
//
//            }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

}