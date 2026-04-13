package com.example.appinterface.MainClienteActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.CategoriaAdapter
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.activitys.tiendaActivity.SubcategoriaActivity
import com.example.appinterface.model.Categoria
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TiendaFragment : Fragment(R.layout.fragment_tienda) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerCategorias)
        recycler.layoutManager = GridLayoutManager(context, 2).apply {
            orientation = RecyclerView.VERTICAL
        }

        cargarCategorias(recycler)
    }

    private fun cargarCategorias(recycler: RecyclerView) {

        RetrofitInstance.categoriaApi.obtenerCategorias()
            .enqueue(object : Callback<List<Categoria>> {

                override fun onResponse(
                    call: Call<List<Categoria>>,
                    response: Response<List<Categoria>>
                ) {
                    var categorias = response.body() ?: emptyList()

                    // Si no hay categorías, agregar algunas de ejemplo
                    if (categorias.isEmpty()) {
                        categorias = listOf(
                            Categoria(categoria_id = 1, categoria_nombre = "Ataudes"),
                            Categoria(categoria_id = 2, categoria_nombre = "Urnas"),
                            Categoria(categoria_id = 3, categoria_nombre = "Arreglos Florales"),
                            Categoria(categoria_id = 4, categoria_nombre = "Lápidas")
                        )
                    }

                    recycler.adapter = CategoriaAdapter(categorias) { categoria ->

                        val intent = Intent(
                            requireContext(),
                            SubcategoriaActivity::class.java
                        )

                        intent.putExtra("CATEGORIA_ID", categoria.categoria_id)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {}
            })
    }
}