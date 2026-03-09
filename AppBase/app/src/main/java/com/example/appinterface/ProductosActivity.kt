package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ContratoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.BuscarContratoActivity
import com.example.appinterface.CrearContratoActivity
import com.example.appinterface.ActualizarContratoActivity
import com.example.appinterface.EliminarContratoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        recycler = findViewById(R.id.recyclerContratos)
        recycler.layoutManager = LinearLayoutManager(this)


        val btnBuscar = findViewById<Button>(R.id.btnBuscarId)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        btnBuscar.setOnClickListener {
            val intent = Intent(this, BuscarContratoActivity::class.java)
            startActivity(intent)
        }

        btnCrear.setOnClickListener {
            val intent = Intent(this, CrearContratoActivity::class.java)
            startActivity(intent)
        }

        btnActualizar.setOnClickListener {
            val intent = Intent(this, ActualizarContratoActivity::class.java)
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            val intent = Intent(this, EliminarContratoActivity::class.java)
            startActivity(intent)
        }

        obtenerCronogramas()
    }

    private fun obtenerCronogramas(){

        RetrofitInstance.api.obtenerCronogramas().enqueue(object :
            Callback<List<Contrato>> {

            override fun onResponse(
                call: Call<List<Contrato>>,
                response: Response<List<Contrato>>
            ) {

                if(response.isSuccessful){

                    val contratos = response.body()

                    if(contratos != null){
                        recycler.adapter = ContratoAdapter(contratos)
                    }

                } else {

                    Toast.makeText(
                        this@ProductosActivity,
                        "Error al traer datos",
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
        obtenerCronogramas()
    }
}