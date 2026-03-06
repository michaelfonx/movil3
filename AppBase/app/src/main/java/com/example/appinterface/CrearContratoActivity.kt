package com.example.appinterface

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearContratoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_contrato)

        val valor = findViewById<EditText>(R.id.txtValor)
        val cliente = findViewById<EditText>(R.id.txtCliente)

        findViewById<Button>(R.id.btnCrear).setOnClickListener {

            val contrato = Contrato(
                null,
                true,
                valor.text.toString().toDouble(),
                cliente.text.toString().toInt()
            )

            RetrofitInstance.api.crearContrato(contrato)
                .enqueue(object : Callback<String> {

                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        Toast.makeText(
                            this@CrearContratoActivity,
                            "Contrato creado",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {}
                })
        }

        // BOTON VOLVER
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}