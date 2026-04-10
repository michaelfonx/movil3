package com.example.appinterface.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Usuario
import model.DTO.AfiliadoDTO
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

        val btnBuscar = view.findViewById<Button>(R.id.btnAgregarAfiliado)
        val input = view.findViewById<EditText>(R.id.inputUsuarioId)


        btnBuscar.setOnClickListener {

            val documento = input.text.toString().toIntOrNull()

            if (documento == null) {
                Toast.makeText(context, "Cédula inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            buscarUsuario(documento, view)
        }
    }


    private fun cargarAfiliados(view: View) {

        val layout = view.findViewById<LinearLayout>(R.id.layoutAfiliados)

        RetrofitInstance.afiliadoApi.obtenerAfiliados(contratoId)
            .enqueue(object : Callback<List<AfiliadoDTO>> {

                override fun onResponse(
                    call: Call<List<AfiliadoDTO>>,
                    response: Response<List<AfiliadoDTO>>
                ) {

                    layout.removeAllViews()

                    if (!response.isSuccessful) {
                        Toast.makeText(context, "Error HTTP: ${response.code()}", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val lista = response.body() ?: emptyList()

                    if (lista.isEmpty()) {
                        val txt = TextView(context)
                        txt.text = "Sin afiliados"
                        layout.addView(txt)
                        return
                    }

                    for (afiliado in lista) {

                        val card = layoutInflater.inflate(R.layout.item_afiliado, null)

                        val nombre = card.findViewById<TextView>(R.id.txtNombre)
                        val documento = card.findViewById<TextView>(R.id.txtDocumento)

                        nombre.text = "${afiliado.nombre} ${afiliado.apellido}"
                        documento.text = "Documento: ${afiliado.usuario_id}"

                        layout.addView(card)
                    }
                }

                override fun onFailure(call: Call<List<AfiliadoDTO>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun buscarUsuario(documento: Int, view: View) {

        val layout = view.findViewById<LinearLayout>(R.id.layoutAfiliados)

        RetrofitInstance.usuarioApi.buscarPorDocumento(documento)
            .enqueue(object : Callback<Usuario> {

                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {

                    if (!response.isSuccessful || response.body() == null) {
                        Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val usuario = response.body()!!

                    layout.removeAllViews()

                    val card = layoutInflater.inflate(R.layout.item_afiliado, null)

                    val nombre = card.findViewById<TextView>(R.id.txtNombre)
                    val documentoTxt = card.findViewById<TextView>(R.id.txtDocumento)

                    nombre.text =
                        "${usuario.usuario_primer_nombre} ${usuario.usuario_primer_apellido}"
                    documentoTxt.text = "Documento: ${usuario.usuario_documento}"

                    val btnAgregar = Button(requireContext())
                    btnAgregar.text = "Agregar afiliado"
                    btnAgregar.setBackgroundTintList(resources.getColorStateList(R.color.purple_700))
                    btnAgregar.setTextColor(resources.getColor(android.R.color.white))

                    btnAgregar.setOnClickListener {
                        agregarAfiliadoPorDocumento(usuario.usuario_documento, view)
                    }

                    val container = card.findViewById<LinearLayout>(R.id.containerCard)
                    container.addView(btnAgregar)

                    layout.addView(card)
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun agregarAfiliadoPorDocumento(documento: Int, view: View) {

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