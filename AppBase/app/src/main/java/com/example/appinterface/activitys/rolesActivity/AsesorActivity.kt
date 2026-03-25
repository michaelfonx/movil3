package com.example.appinterface.activitys.asesor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.activitys.ServiciosActivity
import com.example.appinterface.activitys.activityContrato.ProductosActivity

class AsesorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesor)

        val btnServicios = findViewById<Button>(R.id.btnServicios)
        val btnContratos = findViewById<Button>(R.id.btnContratos)

        btnServicios.setOnClickListener {
            startActivity(Intent(this, ServiciosActivity::class.java))
        }

        btnContratos.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }
    }
}