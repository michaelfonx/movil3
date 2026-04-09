package com.example.appinterface.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.activitys.tiendaActivity.ProductoActivity
import com.example.appinterface.model.Subcategoria

class SubcategoriaAdapter(
    private val lista: List<Subcategoria>
) : RecyclerView.Adapter<SubcategoriaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val imgSubcategoria: ImageView = view.findViewById(R.id.imgSubcategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subcategoria, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]


        holder.txtNombre.text = item.subcategoria_nombre

        val nombre = item.subcategoria_nombre.trim().lowercase()

        val imagen = when {
            nombre.contains("grande") -> R.drawable.cat_ataudes
            nombre.contains("mediano") -> R.drawable.cat_urnas
            nombre.contains("peque") -> R.drawable.cat_flores
            else -> R.drawable.logo
        }

        holder.imgSubcategoria.setImageResource(imagen)


        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(context, ProductoActivity::class.java)
            intent.putExtra("SUBCATEGORIA_ID", item.subcategoria_id ?: 0)

            context.startActivity(intent)
        }
    }
}