package com.example.appinterface.Api

import com.example.appinterface.model.Producto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductoApiService {

    @GET("api/productos")
    fun obtenerProductos(): Call<List<Producto>>
    @GET("api/productos/buscar")
    fun buscarProductos(@Query("nombre") nombre: String): Call<List<Producto>>
}