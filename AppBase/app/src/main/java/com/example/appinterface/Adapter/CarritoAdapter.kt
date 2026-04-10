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

    private val seleccionados = mutableSetOf<Int>()

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

        holder.txtProducto.text = "Producto #${item.producto_id}"
        holder.txtCantidad.text = item.cantidad.toString()
        holder.txtPrecio.text = "$ ${item.precio_unitario * item.cantidad}"

        holder.btnMas.setOnClickListener {
            val nuevaCantidad = item.cantidad + 1
            lista[position] = item.copy(cantidad = nuevaCantidad)
            notifyItemChanged(position)
            calcularTotal()
        }

        holder.btnMenos.setOnClickListener {
            if (item.cantidad > 1) {
                val nuevaCantidad = item.cantidad - 1
                lista[position] = item.copy(cantidad = nuevaCantidad)
                notifyItemChanged(position)
                calcularTotal()
            }
        }

        holder.btnEliminar.setOnClickListener {

            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val itemActual = lista[pos]

            RetrofitInstance.carritoApi.eliminar(
                itemActual.usuario_id,
                itemActual.producto_id
            ).enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    lista.removeAt(pos)
                    notifyDataSetChanged()
                    calcularTotal()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })
        }

        holder.check.setOnCheckedChangeListener(null)
        holder.check.isChecked = seleccionados.contains(position)

        holder.check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) seleccionados.add(position)
            else seleccionados.remove(position)
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