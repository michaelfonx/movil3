package com.example.appinterface.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.model.Plan

class PlanAdapter(
    private val lista: List<Plan>
) : RecyclerView.Adapter<PlanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val precio: TextView = view.findViewById(R.id.txtPrecio)
        val imagen: ImageView = view.findViewById(R.id.imgPlan)
        val badge: TextView = view.findViewById(R.id.badgePrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val plan = lista[position]

        // 🔥 TEXTO
        holder.nombre.text = plan.plan_nombre
        holder.precio.text = "$${plan.plan_precio} al mes"

        // 🔥 BADGE
        holder.badge.text = "$${plan.plan_precio}"

        // 🔥 IMÁGENES ROTATIVAS
        val imagenes = listOf(
            R.drawable.plan1,
            R.drawable.plan2,
            R.drawable.plan3
        )

        holder.imagen.setImageResource(
            imagenes[position % imagenes.size]
        )

        // 🔥 CLICK
        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                plan.plan_nombre,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}