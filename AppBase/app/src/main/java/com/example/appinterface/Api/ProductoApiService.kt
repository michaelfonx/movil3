package com.example.appinterface.Api

import com.example.appinterface.model.Producto
import retrofit2.Call
import retrofit2.http.*

interface ProductoApiService {

    @GET("api/productos")
    fun obtenerProductos(): Call<List<Producto>>

    @GET("api/productos/{id}")
    fun obtenerProductoPorId(
        @Path("id") id: Int
    ): Call<Producto>

    @GET("api/productos/subcategoria/{id}")
    fun obtenerProductosPorSubcategoria(
        @Path("id") id: Int
    ): Call<List<Producto>>

    @POST("api/productos")
    fun crearProducto(@Body producto: Producto): Call<String>

    @PUT("api/productos/{id}")
    fun actualizarProducto(
        @Path("id") id: Int,
        @Body producto: Producto
    ): Call<String>

    @DELETE("api/productos/{id}")
    fun eliminarProducto(
        @Path("id") id: Int
    ): Call<String>

    @GET("api/productos/buscar")
    fun buscarProductos(
        @Query("nombre") nombre: String
    ): Call<List<Producto>>
}
