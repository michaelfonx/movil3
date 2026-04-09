package com.example.appinterface.activitys.tiendaActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.SubcategoriaAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.model.Subcategoria
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubcategoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subcategoria)

        val recycler = findViewById<RecyclerView>(R.id.recyclerSub)

        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.setHasFixedSize(true)

        // 🔥 ESPACIADO PRO
        recycler.addItemDecoration(
            GridSpacingItemDecoration(2, 30, true)
        )

        val categoriaId = intent.getIntExtra("CATEGORIA_ID", 0)

        cargarSubcategorias(categoriaId, recycler)
    }

    private fun cargarSubcategorias(id: Int, recycler: RecyclerView) {

        RetrofitInstance.subcategoriaApi.obtenerSubcategorias(id)
            .enqueue(object : Callback<List<Subcategoria>> {

                override fun onResponse(
                    call: Call<List<Subcategoria>>,
                    response: Response<List<Subcategoria>>
                ) {
                    if (response.isSuccessful) {
                        recycler.adapter =
                            SubcategoriaAdapter(response.body() ?: emptyList())
                    }
                }

                override fun onFailure(call: Call<List<Subcategoria>>, t: Throwable) {}
            })
    }
}