package com.example.appinterface.activitys.tiendaActivity

import android.os.Bundle
import android.widget.Button
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

        val btnAtras = findViewById<Button>(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish()
        }

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
                    var subcategorias = response.body() ?: emptyList()

                    // Si no hay subcategorías, agregar algunas de ejemplo
                    if (subcategorias.isEmpty()) {
                        subcategorias = listOf(
                            Subcategoria(subcategoria_id = 1, subcategoria_nombre = "Tamaño Grande", categoria_id = id),
                            Subcategoria(subcategoria_id = 2, subcategoria_nombre = "Tamaño Mediano", categoria_id = id),
                            Subcategoria(subcategoria_id = 3, subcategoria_nombre = "Tamaño Pequeño", categoria_id = id)
                        )
                    }

                    recycler.adapter =
                        SubcategoriaAdapter(subcategorias)
                }

                override fun onFailure(call: Call<List<Subcategoria>>, t: Throwable) {}
            })
    }
}