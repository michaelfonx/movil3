package com.example.appinterface.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.model.Subcategoria
import com.example.appinterface.R
import com.example.appinterface.activitys.tiendaActivity.ProductoActivity

class SubcategoriaAdapter(
    private val lista: List<Subcategoria>
) : RecyclerView.Adapter<SubcategoriaAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txt = v.findViewById<TextView>(R.id.txtNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.txt.text = item.subcategoria_nombre

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, ProductoActivity::class.java)

            intent.putExtra("SUBCATEGORIA_ID", item.subcategoria_id)

            holder.itemView.context.startActivity(intent)
        }
    }
}