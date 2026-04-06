package com.example.appinterface.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.AfiliadoDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AfiliadosFragment : Fragment(R.layout.fragment_afiliados) {

    private var contratoId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity()
            .getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)

        contratoId = prefs.getInt("CONTRATO_ID", 0)

        if (contratoId == 0) {
            Toast.makeText(context, "Error: contrato no válido", Toast.LENGTH_LONG).show()
            return
        }

        cargarAfiliados(view)

        val btnAgregar = view.findViewById<Button>(R.id.btnAgregarAfiliado)
        val input = view.findViewById<EditText>(R.id.inputUsuarioId)

        btnAgregar.setOnClickListener {

            val documento = input.text.toString().toIntOrNull()

            if (documento == null) {
                Toast.makeText(context, "Cédula inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            agregarAfiliadoPorDocumento(documento, view)
        }
    }

    private fun cargarAfiliados(view: View) {

        RetrofitInstance.afiliadoApi.obtenerAfiliados(contratoId)
            .enqueue(object : Callback<List<AfiliadoDTO>> {

                override fun onResponse(
                    call: Call<List<AfiliadoDTO>>,
                    response: Response<List<AfiliadoDTO>>
                ) {

                    if (!response.isSuccessful) {
                        Toast.makeText(context, "Error HTTP: ${response.code()}", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val lista = response.body() ?: emptyList()

                    val texto = if (lista.isEmpty()) "Sin afiliados"
                    else lista.joinToString("\n") {
                        "• ${it.nombre} ${it.apellido}"
                    }

                    view.findViewById<TextView>(R.id.txtAfiliados).text = texto
                }

                override fun onFailure(call: Call<List<AfiliadoDTO>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun agregarAfiliadoPorDocumento(documento: Int, view: View) {

        if (contratoId == 0) {
            Toast.makeText(context, "Contrato inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val body = mapOf(
            "contrato_id" to contratoId,
            "documento" to documento
        )

        RetrofitInstance.afiliadoApi.agregarAfiliadoPorDocumento(body)
            .enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if (!response.isSuccessful) {
                        val error = response.errorBody()?.string()
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                        return
                    }

                    val mensaje = response.body()?.string() ?: "Sin respuesta"

                    Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                    cargarAfiliados(view)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}