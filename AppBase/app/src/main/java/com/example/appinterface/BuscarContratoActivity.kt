package com.example.appinterface

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarContratoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_contrato)

        val txtId = findViewById<EditText>(R.id.txtId)
        val resultado = findViewById<TextView>(R.id.txtResultado)

        findViewById<Button>(R.id.btnBuscar).setOnClickListener {

            val id = txtId.text.toString().toInt()

            RetrofitInstance.api.buscarPorId(id).enqueue(object : Callback<Contrato> {

                override fun onResponse(
                    call: Call<Contrato>,
                    response: Response<Contrato>
                ) {

                    val contrato = response.body()

                    if (contrato != null) {

                        resultado.text =
                            "ID: ${contrato.contrato_id}\n" +
                                    "Estado: ${contrato.contrato_estado}\n" +
                                    "Valor: ${contrato.contrato_valor}\n" +
                                    "Cliente ID: ${contrato.cliente_id}"

                    } else {
                        resultado.text = "Contrato no encontrado"
                    }
                }

                override fun onFailure(call: Call<Contrato>, t: Throwable) {

                    resultado.text = "Error al buscar contrato"
                }
            })
        }


        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}