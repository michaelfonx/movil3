package com.example.appinterface.activitys

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.CarritoAdapter
import com.example.appinterface.manager.CarritoManager
import com.example.appinterface.R

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCarrito)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnPagar = findViewById<TextView>(R.id.btnPagar)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = CarritoAdapter(CarritoManager.lista)

        val total = CarritoManager.total()
        txtTotal.text = "Total: $ $total"

        btnPagar.setOnClickListener {
            Toast.makeText(this, "Pago simulado: $ $total", Toast.LENGTH_LONG).show()
        }
    }
}