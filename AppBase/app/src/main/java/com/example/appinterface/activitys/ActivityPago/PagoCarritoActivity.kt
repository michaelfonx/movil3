package com.example.appinterface.activitys.ActivityPago

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Carrito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagoCarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_carrito)

        val total = intent.getDoubleExtra("TOTAL", 0.0)

        val txtTotal = findViewById<TextView>(R.id.txtTotalPago)
        val btnPse = findViewById<Button>(R.id.btnPse)
        val btnCredito = findViewById<Button>(R.id.btnCredito)
        val btnDebito = findViewById<Button>(R.id.btnDebito)
        val btnEfectivo = findViewById<Button>(R.id.btnEfectivo)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)

        txtTotal.text = "Total a pagar: $ $total"

        var metodoSeleccionado = ""

        btnPse.setOnClickListener {
            metodoSeleccionado = "PSE"
            abrirLink("https://www.pse.com.co")
        }

        btnCredito.setOnClickListener {
            metodoSeleccionado = "CREDITO"
            abrirLink("https://www.visa.com.co")
        }

        btnDebito.setOnClickListener {
            metodoSeleccionado = "DEBITO"
            abrirLink("https://www.mastercard.com.co")
        }

        btnEfectivo.setOnClickListener {
            metodoSeleccionado = "EFECTIVO"
            Toast.makeText(this, "Acércate a una sede para pagar", Toast.LENGTH_LONG).show()
        }

        btnConfirmar.setOnClickListener {

            if (metodoSeleccionado.isEmpty()) {
                Toast.makeText(this, "Selecciona un método de pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("USUARIO_ID", 0)

            if (usuarioId == 0) {
                Toast.makeText(this, "Debe iniciar sesión", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            confirmarCompra(usuarioId, metodoSeleccionado)
        }
    }

    private fun abrirLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun confirmarCompra(usuarioId: Int, metodo: String) {

        RetrofitInstance.carritoApi.confirmarPago(usuarioId, metodo)
            .enqueue(object : Callback<Map<String, String>> {

                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    if (response.isSuccessful) {

                        val mensaje = response.body()?.get("mensaje")
                            ?: "Pago realizado correctamente 💳"

                        Toast.makeText(
                            this@PagoCarritoActivity,
                            mensaje,
                            Toast.LENGTH_LONG
                        ).show()

                        finish()

                    } else {
                        Toast.makeText(
                            this@PagoCarritoActivity,
                            "Error al procesar pago",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Map<String, String>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@PagoCarritoActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
    }