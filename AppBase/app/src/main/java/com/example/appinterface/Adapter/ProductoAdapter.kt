package com.example.appinterface.MainClienteActivity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.manager.CarritoManager
import com.example.appinterface.model.Producto
import com.example.appinterface.model.DetalleProductoActivity

class ProductoAdapter(private val lista: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
        val btnAgregar: TextView = view.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]


        holder.txtNombre.text = item.producto_nombre
        holder.txtPrecio.text = "$ ${item.producto_precio}"


        holder.btnAgregar.setOnClickListener {
            CarritoManager.agregar(item)

            Toast.makeText(
                holder.itemView.context,
                "Agregado al carrito 🛒",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(context, DetalleProductoActivity::class.java)


            intent.putExtra("producto", item)

            context.startActivity(intent)
        }
    }
}