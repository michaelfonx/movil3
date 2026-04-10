package com.example.appinterface.activitys.tiendaActivity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.CarritoAdapter
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.activitys.ActivityPago.PagoCarritoActivity
import com.example.appinterface.model.Carrito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCarrito)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnPagar = findViewById<TextView>(R.id.btnPagar)

        recycler.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val usuarioId = sharedPref.getInt("USUARIO_ID", 0)

        if (usuarioId == 0) {
            Toast.makeText(this, "Debe iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.carritoApi.obtenerCarrito(usuarioId)
            .enqueue(object : Callback<List<Carrito>> {

                override fun onResponse(
                    call: Call<List<Carrito>>,
                    response: Response<List<Carrito>>
                ) {
                    if (response.isSuccessful) {

                        val lista = (response.body() ?: emptyList()).toMutableList()

                        val adapter = CarritoAdapter(lista) { total ->
                            txtTotal.text = "Total: $ $total"
                        }

                        recycler.adapter = adapter

                        btnPagar.setOnClickListener {
                            val intent = Intent(this@CarritoActivity, PagoCarritoActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Carrito>>, t: Throwable) {
                    Toast.makeText(this@CarritoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }
}