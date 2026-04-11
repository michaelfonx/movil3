package com.example.appinterface.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Producto
import com.example.appinterface.model.Carrito
import com.example.appinterface.model.DetalleProductoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        holder.img.setImageResource(R.drawable.img_default)

        holder.btnAgregar.setOnClickListener {

            val context = holder.itemView.context
            val sharedPref = context.getSharedPreferences("APP_PREFS", 0)

            val usuarioId = sharedPref.getInt("USUARIO_ID", 0)

            if (usuarioId == 0) {
                Toast.makeText(context, "Debe iniciar sesión", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val carrito = Carrito(
                usuario_id = usuarioId,
                producto_id = item.producto_id,
                cantidad = 1,
                precio_unitario = item.producto_precio
            )

            RetrofitInstance.carritoApi.agregar(carrito)
                .enqueue(object : Callback<String> {

                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        Toast.makeText(
                            context,
                            "Agregado al carrito ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Agregado al carrito ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleProductoActivity::class.java)
            intent.putExtra("producto", item)
            context.startActivity(intent)
        }
    }
}