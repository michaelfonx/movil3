package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.model.Servicio

class ServicioAdapter(
    private val listaServicios: List<Servicio>,
    private val onEditar: (Servicio) -> Unit,
    private val onEliminar: (Servicio) -> Unit
) : RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder>() {

    class ServicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombre: TextView = view.findViewById(R.id.nombreServicio)
        val precio: TextView = view.findViewById(R.id.precioServicio)

        // 🔥 NUEVO
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_servicio, parent, false)

        return ServicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {

        val servicio = listaServicios[position]

        holder.nombre.text = servicio.servicioNombre
        holder.precio.text = "Precio: ${servicio.servicioPrecio}"

        // 🔥 ACCIONES
        holder.btnEditar.setOnClickListener {
            onEditar(servicio)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(servicio)
        }
    }

    override fun getItemCount(): Int {
        return listaServicios.size
    }
}