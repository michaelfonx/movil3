package com.example.appinterface.MainClienteActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.appinterface.R

class SedesFragment : Fragment(R.layout.fragment_sedes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btnBogota = view.findViewById<Button>(R.id.btnBogota)
        val btnMedellin = view.findViewById<Button>(R.id.btnMedellin)

        btnBogota.setOnClickListener {
            abrirMapa("Funeraria Almasoft Bogotá")
        }

        btnMedellin.setOnClickListener {
            abrirMapa("Funeraria Almasoft Medellín")
        }
    }

    private fun abrirMapa(ubicacion: String) {
        val uri = Uri.parse("geo:0,0?q=$ubicacion")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}