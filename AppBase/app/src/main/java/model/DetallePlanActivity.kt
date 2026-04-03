package com.example.appinterface.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.MiPlanDTO
import example.appinterface.model.AdquirirPlanRequest
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
            println("🔥 CLICK BOTÓN ADQUIRIR")
            validarSiYaTienePlan()
        }
    }

    private fun validarSiYaTienePlan() {

        val prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val clienteId = prefs.getInt("ID", 0)

        println("🔥 CLIENTE ID: $clienteId")

        if (clienteId <= 0) {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.api.obtenerMiPlan(clienteId)
            .enqueue(object : Callback<MiPlanDTO> {

                override fun onResponse(call: Call<MiPlanDTO>, response: Response<MiPlanDTO>) {

                    println("🔥 VALIDAR RESPONSE: ${response.code()}")

                    val data = response.body()

                    if (response.isSuccessful && data != null && (data.contrato_id ?: 0) > 0) {
                        Toast.makeText(
                            this@DetallePlanActivity,
                            "Ya tienes un plan adquirido",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        println("🔥 NO TIENE PLAN → VA A ADQUIRIR")
                        adquirirPlan()
                    }
                }

                override fun onFailure(call: Call<MiPlanDTO>, t: Throwable) {
                    println("🔥 ERROR VALIDAR: ${t.message}")
                    adquirirPlan()
                }
            })
    }

    private fun adquirirPlan() {

        val prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val clienteId = prefs.getInt("ID", 0)

        println("🔥 ENVIANDO A BACKEND clienteId=$clienteId planId=$planId")

        if (clienteId <= 0) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val body = AdquirirPlanRequest(
            cliente_id = clienteId,
            plan_id = planId,
            valor = planPrecio
        )

        println("🔥 BODY: $body")

        RetrofitInstance.api.adquirirPlan(body)
            .enqueue(object : Callback<Int> {

                override fun onResponse(call: Call<Int>, response: Response<Int>) {

                    println("🔥 RESPONSE CODE: ${response.code()}")
                    println("🔥 RESPONSE BODY: ${response.body()}")

                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@DetallePlanActivity,
                            "Error backend",
                            Toast.LENGTH_SHORT
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

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    println("🔥 ERROR REAL: ${t.message}")
                    Toast.makeText(
                        this@DetallePlanActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}