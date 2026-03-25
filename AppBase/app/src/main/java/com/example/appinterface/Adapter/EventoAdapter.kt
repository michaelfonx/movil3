package com.example.appinterface.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.model.Evento
import com.example.appinterface.R

class EventoAdapter(private val lista: List<Evento>) :
    RecyclerView.Adapter<EventoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.titulo.text = item.titulo
        holder.descripcion.text = item.descripcion

        // 🎨 COLORES POR TIPO
        when (item.titulo) {

            "Contrato creado" -> holder.titulo.setTextColor(Color.parseColor("#2E7D32")) // verde

            "Servicio incluido" -> holder.titulo.setTextColor(Color.parseColor("#1565C0")) // azul

            "Afiliado" -> holder.titulo.setTextColor(Color.parseColor("#6A1B9A")) // morado

            "Pago" -> holder.titulo.setTextColor(Color.parseColor("#EF6C00")) // naranja

            else -> holder.titulo.setTextColor(Color.BLACK)
        }
    }
}