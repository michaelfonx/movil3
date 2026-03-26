package com.example.appinterface.activitys.activityContrato

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ContratoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.model.Contrato
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ContratoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        recycler = findViewById(R.id.recyclerContratos)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        val btnBuscar = findViewById<Button>(R.id.btnBuscarId)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        btnBuscar.setOnClickListener {
            startActivity(Intent(this, BuscarContratoActivity::class.java))
        }

        btnCrear.setOnClickListener {
            startActivity(Intent(this, CrearContratoActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            startActivity(Intent(this, ActualizarContratoActivity::class.java))
        }

        btnEliminar.setOnClickListener {
            startActivity(Intent(this, EliminarContratoActivity::class.java))
        }

        cargarContratos()
    }

    private fun cargarContratos() {

        RetrofitInstance.api.obtenerContratos()
            .enqueue(object : Callback<List<Contrato>> {

                override fun onResponse(
                    call: Call<List<Contrato>>,
                    response: Response<List<Contrato>>
                ) {

                    if (response.isSuccessful) {

                        val contratos = response.body()

                        if (!contratos.isNullOrEmpty()) {

                            adapter = ContratoAdapter(contratos)
                            recycler.adapter = adapter

                        } else {

                            Toast.makeText(
                                this@ProductosActivity,
                                "No hay contratos registrados",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        Toast.makeText(
                            this@ProductosActivity,
                            "Error en la respuesta del servidor",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Contrato>>, t: Throwable) {

                    Toast.makeText(
                        this@ProductosActivity,
                        "Error de conexión",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        cargarContratos()
    }
}