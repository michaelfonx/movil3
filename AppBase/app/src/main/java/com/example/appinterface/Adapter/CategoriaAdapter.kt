package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.model.Categoria

class CategoriaAdapter(
    private val lista: List<Categoria>,
    private val onClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val imgCategoria: ImageView = view.findViewById(R.id.imgCategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.txtNombre.text = item.categoria_nombre

        val nombre = item.categoria_nombre.lowercase()

        val imagen = when {
            nombre.contains("ataud") -> R.drawable.cat_ataudes
            nombre.contains("urna") -> R.drawable.cat_urnas
            nombre.contains("arreglo") -> R.drawable.cat_flores
            nombre.contains("lapida") -> R.drawable.cat_lapidas
            else -> R.drawable.img_default
        }

        holder.imgCategoria.setImageResource(imagen)

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }


}