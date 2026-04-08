package com.example.appinterface.MainClienteActivity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TiendaFragment : Fragment(R.layout.fragment_tienda) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerProductos)
        val etBuscar = view.findViewById<EditText>(R.id.etBuscar)

        recycler.layoutManager = GridLayoutManager(context, 2)


        cargarProductos(recycler)


        etBuscar.addTextChangedListener {

            val texto = it.toString()

            if (texto.isNotEmpty()) {

                RetrofitInstance.productoApi.buscarProductos(texto)
                    .enqueue(object : Callback<List<Producto>> {

                        override fun onResponse(
                            call: Call<List<Producto>>,
                            response: Response<List<Producto>>
                        ) {
                            if (response.isSuccessful) {
                                val lista = response.body() ?: emptyList()
                                recycler.adapter = ProductoAdapter(lista)
                            }
                        }

                        override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                            Toast.makeText(context, "Error en búsqueda", Toast.LENGTH_SHORT).show()
                        }
                    })

            } else {
                cargarProductos(recycler)
            }
        }
    }


    fun cargarProductos(recycler: RecyclerView) {

        RetrofitInstance.productoApi.obtenerProductos()
            .enqueue(object : Callback<List<Producto>> {

                override fun onResponse(
                    call: Call<List<Producto>>,
                    response: Response<List<Producto>>
                ) {
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        recycler.adapter = ProductoAdapter(lista)
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    Toast.makeText(context, "Error cargando productos", Toast.LENGTH_SHORT).show()
                }
            })
    }
}