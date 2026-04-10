package com.example.appinterface.MainClienteActivity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.Adapter.PagoAdapter
import com.example.appinterface.model.Pago
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagosFragment : Fragment(R.layout.fragment_pagos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPagos)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val sharedPref = requireContext()
            .getSharedPreferences("APP_PREFS", AppCompatActivity.MODE_PRIVATE)

        val usuarioId = sharedPref.getInt("USUARIO_ID", 0)

        if (usuarioId == 0) {
            Toast.makeText(requireContext(), "Debe iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.carritoApi.historial(usuarioId)
            .enqueue(object : Callback<List<Map<String, Any>>> {

                override fun onResponse(
                    call: Call<List<Map<String, Any>>>,
                    response: Response<List<Map<String, Any>>>
                ) {

                    if (response.isSuccessful) {

                        val listaMap = response.body() ?: emptyList()

                        val listaPagos = listaMap.map {

                            val pagoId = (it["pago_id"] as? Double)?.toInt() ?: 0
                            val metodo = it["pago_metodo"]?.toString() ?: ""
                            val fecha = it["pago_fecha"]?.toString() ?: ""
                            val contratoId = (it["contrato_id"] as? Double)?.toInt() ?: 0

                            Pago(
                                pago_id = pagoId,
                                pago_metodo = metodo,
                                pago_fecha = fecha,
                                contrato_id = contratoId
                            )
                        }

                        recycler.adapter = PagoAdapter(listaPagos)
                    }
                }

                override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Error cargando historial",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}