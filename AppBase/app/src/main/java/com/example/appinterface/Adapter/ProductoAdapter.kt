package com.example.appinterface.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        val btnAgregar: Button = view.findViewById(R.id.btnAgregar)
        val img: ImageView = view.findViewById(R.id.imgProducto)
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

        val nombre = item.producto_nombre
            .trim()
            .lowercase()

        val imagen = when {
            nombre.contains("ataud") -> R.drawable.cat_ataudes
            nombre.contains("urna") -> R.drawable.cat_urnas
            nombre.contains("flor") -> R.drawable.cat_flores
            nombre.contains("lapida") -> R.drawable.cat_lapidas
            else -> R.drawable.img_default
        }

        holder.img.setImageResource(imagen)

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