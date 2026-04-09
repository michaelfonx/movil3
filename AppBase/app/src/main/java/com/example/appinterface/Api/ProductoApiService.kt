package com.example.appinterface.Api

import com.example.appinterface.model.Producto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductoApiService {

    @GET("api/productos")
    fun obtenerProductos(): Call<List<Producto>>

    @GET("api/productos/subcategoria/{id}")
    fun obtenerProductosPorSubcategoria(
        @Path("id") id: Int
    ): Call<List<Producto>>
}