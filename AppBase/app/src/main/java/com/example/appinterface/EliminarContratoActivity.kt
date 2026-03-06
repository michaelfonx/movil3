package com.example.appinterface

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EliminarContratoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_contrato)

        val txtId = findViewById<EditText>(R.id.txtId)

        findViewById<Button>(R.id.btnEliminar).setOnClickListener {

            val id = txtId.text.toString().toInt()

            AlertDialog.Builder(this)
                .setTitle("Eliminar contrato")
                .setMessage("¿Seguro que desea eliminar?")
                .setPositiveButton("Sí") { _, _ ->

                    RetrofitInstance.api.eliminarContrato(id)
                        .enqueue(object : Callback<String> {

                            override fun onResponse(call: Call<String>, response: Response<String>) {

                                Toast.makeText(
                                    this@EliminarContratoActivity,
                                    "Contrato eliminado",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {}
                        })
                }
                .setNegativeButton("No", null)
                .show()
        }

        // BOTON VOLVER
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}