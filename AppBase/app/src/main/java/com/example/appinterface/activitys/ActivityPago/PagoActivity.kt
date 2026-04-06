package com.example.appinterface.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Pago
import example.appinterface.model.AdquirirPlanRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.example.appinterface.MainClienteActivity.MainClienteActivity

class PagoActivity : AppCompatActivity() {

    private var clienteId: Int = 0
    private var planId: Int = 0
    private var valor: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        clienteId = intent.getIntExtra("CLIENTE_ID", 0)
        planId = intent.getIntExtra("PLAN_ID", 0)
        valor = intent.getDoubleExtra("VALOR", 0.0)

        if (clienteId == 0 || planId == 0) {
            Toast.makeText(this, "Error datos", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val btnPagar = findViewById<Button>(R.id.btnPagar)

        btnPagar.setOnClickListener {
            btnPagar.isEnabled = false
            realizarPago(btnPagar)
        }
    }

    private fun realizarPago(btn: Button) {

        val request = AdquirirPlanRequest(
            cliente_id = clienteId,
            plan_id = planId,
            valor = valor
        )

        RetrofitInstance.api.adquirirPlan(request)
            .enqueue(object : Callback<Map<String, Int>> {

                override fun onResponse(
                    call: Call<Map<String, Int>>,
                    response: Response<Map<String, Int>>
                ) {

                    val contratoId = response.body()?.get("contrato_id") ?: 0

                    if (!response.isSuccessful || contratoId == 0) {
                        btn.isEnabled = true
                        Toast.makeText(this@PagoActivity, "Error al crear contrato", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                    prefs.edit().putInt("CONTRATO_ID", contratoId).apply()

                    crearPago(contratoId, btn)
                }

                override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                    btn.isEnabled = true
                    Toast.makeText(this@PagoActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun crearPago(contratoId: Int, btn: Button) {

        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val pago = Pago(
            pago_id = null,
            pago_metodo = "EFECTIVO",
            pago_fecha = fecha,
            contrato_id = contratoId
        )

        RetrofitInstance.pagoApi.crearPago(pago)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    btn.isEnabled = true

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@PagoActivity,
                            "Pago realizado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this@PagoActivity, MainClienteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@PagoActivity,
                            "Error registrando pago",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    btn.isEnabled = true

                    Toast.makeText(
                        this@PagoActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}