package com.example.appinterface.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Contrato
import com.example.appinterface.model.ContratoPlan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallePlanActivity : AppCompatActivity() {

    private var planId: Int = 0
    private var planNombre: String = ""
    private var planPrecio: Double = 0.0
    private var planDescripcion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_plan)

        planId = intent.getIntExtra("PLAN_ID", 0)
        planNombre = intent.getStringExtra("PLAN_NOMBRE") ?: ""
        planPrecio = intent.getDoubleExtra("PLAN_PRECIO", 0.0)
        planDescripcion = intent.getStringExtra("PLAN_DESC") ?: ""

        findViewById<TextView>(R.id.txtNombre).text = planNombre
        findViewById<TextView>(R.id.txtPrecio).text = "$$planPrecio"
        findViewById<TextView>(R.id.txtDescripcion).text = planDescripcion

        val btnAdquirir = findViewById<Button>(R.id.btnAdquirir)

        btnAdquirir.setOnClickListener {
            crearContrato()
        }
    }

    private fun crearContrato() {

        val prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val clienteId = prefs.getInt("ID", 0)

        if (clienteId == 0) {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        val contrato = Contrato(
            contrato_estado = true,
            contrato_valor = planPrecio,
            cliente_id = clienteId
        )

        RetrofitInstance.api.crearContrato(contrato)
            .enqueue(object : Callback<Int> {

                override fun onResponse(call: Call<Int>, response: Response<Int>) {

                    if (!response.isSuccessful || response.body() == null) {
                        Toast.makeText(this@DetallePlanActivity,
                            "Error al crear contrato",
                            Toast.LENGTH_SHORT).show()
                        return
                    }

                    val contratoId = response.body()!!
                    asignarPlan(contratoId)
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(this@DetallePlanActivity,
                        "Error conexión: ${t.message}",
                        Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun asignarPlan(contratoId: Int) {

        val relacion = ContratoPlan(
            contrato_id = contratoId,
            plan_id = planId
        )

        RetrofitInstance.api.crearContratoPlan(relacion)
            .enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@DetallePlanActivity,
                            "Error backend: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    Toast.makeText(
                        this@DetallePlanActivity,
                        "Plan adquirido correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        this@DetallePlanActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}