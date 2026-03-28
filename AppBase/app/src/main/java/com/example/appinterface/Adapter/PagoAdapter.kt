package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.model.Pago

class PagoAdapter(private val lista: List<Pago>) :
    RecyclerView.Adapter<PagoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMetodo: TextView = view.findViewById(R.id.txtMetodo)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pago, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pago = lista[position]
        holder.txtMetodo.text = " ${pago.pago_metodo}"
        holder.txtFecha.text = " ${pago.pago_fecha}"
    }

    override fun getItemCount() = lista.size
}