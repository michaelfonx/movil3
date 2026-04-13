package com.example.appinterface.activitys.tiendaActivity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ProductoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.model.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        val recycler = findViewById<RecyclerView>(R.id.recyclerProductos)
        recycler.layoutManager = GridLayoutManager(this, 2)

        val btnAtras = findViewById<Button>(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish()
        }

        val subcategoriaId = intent.getIntExtra("SUBCATEGORIA_ID", 0)

        RetrofitInstance.productoApi.obtenerProductosPorSubcategoria(subcategoriaId)
            .enqueue(object : Callback<List<Producto>> {

                override fun onResponse(
                    call: Call<List<Producto>>,
                    response: Response<List<Producto>>
                ) {
                    var productos = response.body() ?: emptyList()

                    // Si no hay productos, agregar algunos de ejemplo
                    if (productos.isEmpty()) {
                        productos = listOf(
                            Producto(
                                producto_id = 1,
                                producto_nombre = "Ataud Ejemplo",
                                producto_descripcion = "Descripción de ejemplo",
                                producto_precio = 1000.0,
                                producto_stock = 10,
                                producto_estado = true,
                                subcategoria_id = subcategoriaId
                            ),
                            Producto(
                                producto_id = 2,
                                producto_nombre = "Urna Ejemplo",
                                producto_descripcion = "Otra descripción",
                                producto_precio = 2000.0,
                                producto_stock = 5,
                                producto_estado = true,
                                subcategoria_id = subcategoriaId
                            )
                        )
                    }

                    recycler.adapter = ProductoAdapter(productos)
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {}
            })
    }
}