package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Contrato
import com.example.appinterface.R

class ContratoAdapter(private val lista: List<Contrato>) :
    RecyclerView.Adapter<ContratoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtContrato: TextView = view.findViewById(R.id.txtContrato)
        val txtValor: TextView = view.findViewById(R.id.txtValor)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contrato, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contrato = lista[position]

        holder.txtContrato.text = "Contrato ID: ${contrato.contrato_id}"
        holder.txtValor.text = "Valor: ${contrato.contrato_valor}"
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}