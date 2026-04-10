package com.example.appinterface

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.model.Carrito

class CarritoAdapter(
    private val lista: List<Carrito>
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtProducto: TextView = view.findViewById(R.id.txtProducto)
        val txtCantidad: TextView = view.findViewById(R.id.txtCantidad)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.txtProducto.text = "Producto ID: ${item.producto_id}"
        holder.txtCantidad.text = "Cantidad: ${item.cantidad}"
        holder.txtPrecio.text = "$ ${item.precio_unitario}"
    }
}