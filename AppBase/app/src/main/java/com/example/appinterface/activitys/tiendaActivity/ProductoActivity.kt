package com.example.appinterface.activitys.tiendaActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ProductoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.model.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        val recycler = findViewById<RecyclerView>(R.id.recyclerProductos)
        recycler.layoutManager = GridLayoutManager(this, 2)

        val subcategoriaId = intent.getIntExtra("SUBCATEGORIA_ID", 0)

        RetrofitInstance.productoApi.obtenerProductosPorSubcategoria(subcategoriaId)
            .enqueue(object : Callback<List<Producto>> {

                override fun onResponse(
                    call: Call<List<Producto>>,
                    response: Response<List<Producto>>
                ) {
                    if (response.isSuccessful) {
                        recycler.adapter = ProductoAdapter(response.body() ?: emptyList())
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {}
            })
    }
}