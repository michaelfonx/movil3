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
                    if (response.isSuccessful) {

                        val lista = response.body() ?: emptyList()

                        recycler.adapter = CategoriaAdapter(lista) { categoria ->

                            val intent = Intent(
                                requireContext(),
                                SubcategoriaActivity::class.java
                            )

                            intent.putExtra("CATEGORIA_ID", categoria.categoria_id)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {}
            })
    }
}