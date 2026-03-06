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

class ActualizarContratoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_contrato)

        val txtId = findViewById<EditText>(R.id.txtId)
        val txtValor = findViewById<EditText>(R.id.txtValor)

        findViewById<Button>(R.id.btnActualizar).setOnClickListener {

            val id = txtId.text.toString().toInt()

            val contrato = Contrato(
                id,
                true,
                txtValor.text.toString().toDouble(),
                1
            )

            RetrofitInstance.api.actualizarContrato(id, contrato)
                .enqueue(object : Callback<String> {

                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        Toast.makeText(
                            this@ActualizarContratoActivity,
                            "Contrato actualizado",
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