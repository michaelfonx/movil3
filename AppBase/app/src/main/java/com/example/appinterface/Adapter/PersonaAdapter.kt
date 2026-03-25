package com.example.appinterface.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.model.Usuario

class PersonaAdapter(private val personas: List<Usuario>) :
    RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return PersonaViewHolder(view as TextView)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        holder.bind(personas[position])
    }

    override fun getItemCount(): Int = personas.size

    class PersonaViewHolder(private val textView: TextView) :
        RecyclerView.ViewHolder(textView) {

        fun bind(usuario: Usuario) {
            textView.text =
                "${usuario.usuario_primer_nombre} ${usuario.usuario_primer_apellido}"
        }
    }
}