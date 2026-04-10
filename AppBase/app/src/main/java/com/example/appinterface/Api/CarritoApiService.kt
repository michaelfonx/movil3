package com.example.appinterface.Api

import com.example.appinterface.model.Carrito
import retrofit2.Call
import retrofit2.http.*

interface CarritoApiService {

    @GET("api/carrito/{usuarioId}")
    fun obtenerCarrito(@Path("usuarioId") usuarioId: Int): Call<List<Carrito>>

    @POST("api/carrito")
    fun agregar(@Body carrito: Carrito): Call<String>

    @DELETE("api/carrito/{usuarioId}/{productoId}")
    fun eliminar(
        @Path("usuarioId") usuarioId: Int,
        @Path("productoId") productoId: Int
    ): Call<String>

    @POST("api/carrito/confirmar-pago/{usuarioId}/{metodo}")
    fun confirmarPago(
        @Path("usuarioId") usuarioId: Int,
        @Path("metodo") metodo: String
    ): Call<Map<String, String>>

    @GET("api/carrito/historial/{usuarioId}")
    fun historial(
        @Path("usuarioId") usuarioId: Int
    ): Call<List<Map<String, Any>>>
}