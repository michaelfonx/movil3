package com.example.appinterface.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.DetallePlanDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallePlanActivity : AppCompatActivity() {

    private var planId: Int = 0
    private var planPrecio: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_plan)

        planId = intent.getIntExtra("PLAN_ID", 0)

        cargarDetallePlan()

        val btnAdquirir = findViewById<Button>(R.id.btnAdquirir)
        btnAdquirir.setOnClickListener {
            validarSiYaTienePlan()
        }
    }

    private fun cargarDetallePlan() {

        RetrofitInstance.planApi.obtenerDetallePlan(planId)
            .enqueue(object : Callback<DetallePlanDTO> {

                override fun onResponse(
                    call: Call<DetallePlanDTO>,
                    response: Response<DetallePlanDTO>
                ) {
                    if (response.isSuccessful) {

                        val data = response.body()

                        if (data != null) {

                            val nombre = data.plan.plan_nombre

                            findViewById<TextView>(R.id.txtNombre).text = nombre

                            findViewById<TextView>(R.id.txtPrecio).text =
                                "$${data.plan.plan_precio}"

                            val descripcionPro = obtenerDescripcionPlan(nombre)

                            findViewById<TextView>(R.id.txtDescripcion).text =
                                descripcionPro

                            planPrecio = data.plan.plan_precio

                            val serviciosTexto = data.servicios.joinToString("\n") {
                                "✔ ${it.servicioNombre}"
                            }

                            findViewById<TextView>(R.id.txtServicios).text =
                                serviciosTexto

                            val productosTexto = if (!data.productos.isNullOrEmpty()) {
                                data.productos.joinToString("\n") {
                                    "✔ ${it.producto_nombre}"
                                }
                            } else {
                                obtenerProductosPorPlan(nombre)
                            }

                            findViewById<TextView>(R.id.txtProductos).text =
                                productosTexto
                        }
                    } else {
                        Toast.makeText(
                            this@DetallePlanActivity,
                            "Error en respuesta del servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DetallePlanDTO>, t: Throwable) {
                    Toast.makeText(
                        this@DetallePlanActivity,
                        "Error al cargar detalle",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun obtenerProductosPorPlan(nombre: String): String {
        return when (nombre.lowercase()) {

            "básico" -> """
✔ Ataúd básico
✔ Urna
✔ Traslado
✔ Preparación
            """.trimIndent()

            "estándar" -> """
✔ Ataúd estándar
✔ Urna decorada
✔ Flores
✔ Recordatorios
✔ Traslado familiar
            """.trimIndent()

            "premium" -> """
✔ Ataúd premium
✔ Urna especial
✔ Flores premium
✔ Recordatorios personalizados
✔ Transporte familiar
✔ Acompañamiento completo
            """.trimIndent()

            "vip" -> """
✔ Ataúd de lujo
✔ Urna exclusiva
✔ Flores premium
✔ Recordatorios personalizados
✔ Transporte VIP
✔ Atención prioritaria
✔ Servicios adicionales exclusivos
            """.trimIndent()

            else -> "No incluye productos"
        }
    }

    private fun obtenerDescripcionPlan(nombre: String): String {
        return when (nombre.lowercase()) {

            "básico" -> """
Incluye servicios esenciales para acompañarte en momentos difíciles.

• Atención inmediata 24 horas
• Traslado del cuerpo
• Preparación básica
• Ceremonia religiosa
            """.trimIndent()

            "estándar" -> """
Mayor cobertura y beneficios adicionales.

Incluye todo lo del plan básico más:

• Ataúd estándar
• Flores y arreglos
• Recordatorios
• Acompañamiento familiar
            """.trimIndent()

            "premium" -> """
Plan completo con atención preferencial.

Incluye todo lo anterior más:

• Ataúd premium
• Transporte familiar
• Servicio personalizado
• Mayor cobertura en ceremonia
            """.trimIndent()

            "vip" -> """
El plan más completo y exclusivo.

Incluye todo lo anterior más:

• Atención prioritaria 24/7
• Servicios exclusivos
• Cobertura ampliada
• Beneficios adicionales familiares
            """.trimIndent()

            else -> "Información no disponible"
        }
    }

    private fun validarSiYaTienePlan() {

        val prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val clienteId = prefs.getInt("ID", 0)

        if (clienteId <= 0) {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.api.obtenerMiPlan(clienteId)
            .enqueue(object : Callback<Map<String, Any>> {

                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {

                    val data = response.body()

                    if (response.isSuccessful && data != null) {

                        val contratoId = (data["contrato_id"] as? Double)?.toInt() ?: 0

                        if (contratoId > 0) {

                            val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                            prefs.edit().putInt("CONTRATO_ID", contratoId).apply()

                            Toast.makeText(
                                this@DetallePlanActivity,
                                "Ya tienes un plan adquirido",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            abrirPantallaPago(clienteId)
                        }

                    } else {
                        abrirPantallaPago(clienteId)
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    abrirPantallaPago(clienteId)
                }
            })
    }

    private fun abrirPantallaPago(clienteId: Int) {
        val intent = Intent(this, PagoActivity::class.java)
        intent.putExtra("CLIENTE_ID", clienteId)
        intent.putExtra("PLAN_ID", planId)
        intent.putExtra("VALOR", planPrecio)
        startActivity(intent)
    }
}