package com.example.appinterface.model

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.manager.CarritoManager
import model.DTO.MiPlanDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

        val producto = intent.getSerializableExtra("producto") as? Producto

        if (producto == null) {
            Toast.makeText(this, "Error cargando producto", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.txtNombre).text = producto.producto_nombre
        findViewById<TextView>(R.id.txtPrecio).text = "$ ${producto.producto_precio}"
        findViewById<TextView>(R.id.txtDescripcion).text = producto.producto_descripcion

        findViewById<TextView>(R.id.txtStock).text =
            if (producto.producto_estado) "Disponible" else "No disponible"

        findViewById<Button>(R.id.btnCarrito).setOnClickListener {
            CarritoManager.agregar(producto)
            Toast.makeText(this, "Agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnContrato).setOnClickListener {

            val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val clienteId = prefs.getInt("ID", 0)

            if (clienteId == 0) {
                Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitInstance.api.obtenerMiPlan(clienteId)
                .enqueue(object : Callback<MiPlanDTO> {

                    override fun onResponse(
                        call: Call<MiPlanDTO>,
                        response: Response<MiPlanDTO>
                    ) {

                        val contratoId = response.body()?.contrato_id ?: 0

                        if (contratoId == 0) {
                            Toast.makeText(
                                this@DetalleProductoActivity,
                                "No tienes contrato activo",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        val body = mapOf(
                            "contrato_id" to contratoId,
                            "producto_id" to producto.producto_id
                        )

                        RetrofitInstance.api.agregarProductoContrato(body)
                            .enqueue(object : Callback<Map<String, String>> {

                                override fun onResponse(
                                    call: Call<Map<String, String>>,
                                    response: Response<Map<String, String>>
                                ) {
                                    if (response.isSuccessful) {

                                        val mensaje = response.body()?.get("mensaje")
                                            ?: "Producto agregado"

                                        Toast.makeText(
                                            this@DetalleProductoActivity,
                                            mensaje,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else {
                                        Toast.makeText(
                                            this@DetalleProductoActivity,
                                            "Error servidor: ${response.code()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<Map<String, String>>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        this@DetalleProductoActivity,
                                        "Error: ${t.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    }

                    override fun onFailure(
                        call: Call<MiPlanDTO>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "Error obteniendo contrato",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}