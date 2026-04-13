package com.example.appinterface

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Carrito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarritoAdapter(
    private val lista: MutableList<Carrito>,
    private val onTotalChange: (Double) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    private val seleccionados = lista.mapNotNull { it.carrito_id ?: it.producto_id }.toMutableSet()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtProducto: TextView = view.findViewById(R.id.txtProducto)
        val txtCantidad: TextView = view.findViewById(R.id.txtCantidad)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
        val btnMas: TextView = view.findViewById(R.id.btnMas)
        val btnMenos: TextView = view.findViewById(R.id.btnMenos)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminar)
        val check: CheckBox = view.findViewById(R.id.checkSeleccion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = lista[position]
        val itemId = item.carrito_id ?: item.producto_id

        holder.txtProducto.text = item.producto_nombre?.takeIf { it.isNotBlank() }
            ?: "Producto #${item.producto_id}"
        holder.txtCantidad.text = item.cantidad.toString()
        holder.txtPrecio.text = "$ ${item.precio_unitario * item.cantidad}"

        holder.btnMas.setOnClickListener {
            RetrofitInstance.carritoApi.agregar(
                Carrito(
                    carrito_id = item.carrito_id,
                    usuario_id = item.usuario_id,
                    producto_id = item.producto_id,
                    cantidad = 1,
                    precio_unitario = item.precio_unitario
                )
            ).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val nuevaCantidad = item.cantidad + 1
                        lista[position] = item.copy(cantidad = nuevaCantidad)
                        notifyItemChanged(position)
                        calcularTotal()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Error al actualizar cantidad", Toast.LENGTH_SHORT).show()
                }
            })
        }

        holder.btnMenos.setOnClickListener {
            if (item.cantidad > 1) {
                RetrofitInstance.carritoApi.agregar(
                    Carrito(
                        carrito_id = item.carrito_id,
                        usuario_id = item.usuario_id,
                        producto_id = item.producto_id,
                        cantidad = -1,
                        precio_unitario = item.precio_unitario
                    )
                ).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            val nuevaCantidad = item.cantidad - 1
                            lista[position] = item.copy(cantidad = nuevaCantidad)
                            notifyItemChanged(position)
                            calcularTotal()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(holder.itemView.context, "Error al actualizar cantidad", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                val itemActual = item
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                RetrofitInstance.carritoApi.eliminar(
                    itemActual.usuario_id,
                    itemActual.producto_id
                ).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        lista.removeAt(currentPosition)
                        seleccionados.remove(itemId)
                        notifyItemRemoved(currentPosition)
                        calcularTotal()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(holder.itemView.context, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        holder.btnEliminar.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            val itemActual = lista[currentPosition]
            val itemActualId = itemActual.carrito_id ?: itemActual.producto_id

            RetrofitInstance.carritoApi.eliminar(
                itemActual.usuario_id,
                itemActual.producto_id
            ).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    lista.removeAt(currentPosition)
                    seleccionados.remove(itemActualId)
                    notifyItemRemoved(currentPosition)
                    calcularTotal()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                }
            })
        }

        holder.check.setOnCheckedChangeListener(null)
        holder.check.isChecked = seleccionados.contains(itemId)

        holder.check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) seleccionados.add(itemId)
            else seleccionados.remove(itemId)
            calcularTotal()
        }

        calcularTotal()
    }

    private fun calcularTotal() {
        val total = seleccionados.sumOf {
            val item = lista[it]
            item.precio_unitario * item.cantidad
        }
        onTotalChange(total)
    }
}