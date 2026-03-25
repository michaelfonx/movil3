package com.example.appinterface.activitys

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ServicioAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.model.Servicio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiciosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios)

        recyclerView = findViewById(R.id.recyclerServicios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        obtenerServicios()
    }

    private fun obtenerServicios() {

        RetrofitInstance.apiServicios.obtenerServicios()
            .enqueue(object : Callback<List<Servicio>> {

                override fun onResponse(
                    call: Call<List<Servicio>>,
                    response: Response<List<Servicio>>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        val lista = response.body()!!

                        // 🔥 AQUÍ ESTÁ LA CORRECCIÓN
                        recyclerView.adapter = ServicioAdapter(
                            lista,

                            onEditar = { servicio ->
                                Toast.makeText(
                                    this@ServiciosActivity,
                                    "Editar ${servicio.servicioNombre}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },

                            onEliminar = { servicio ->
                                eliminarServicio(servicio.servicioId!!)
                            }
                        )

                    } else {

                        Toast.makeText(
                            this@ServiciosActivity,
                            "Error en la API",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {

                    Toast.makeText(
                        this@ServiciosActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // 🔥 MÉTODO ELIMINAR
    private fun eliminarServicio(id: Int) {

        RetrofitInstance.apiServicios.eliminarServicio(id)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ServiciosActivity,
                            "Servicio eliminado",
                            Toast.LENGTH_SHORT
                        ).show()

                        obtenerServicios() // 🔄 recargar lista
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    Toast.makeText(
                        this@ServiciosActivity,
                        "Error al eliminar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}