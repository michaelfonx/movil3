package com.example.appinterface.Api

import com.example.appinterface.model.Servicio
import retrofit2.Call
import retrofit2.http.*

interface ApiServicioService {


    @GET("servicio")
    fun obtenerServicios(): Call<List<Servicio>>


    @GET("servicio/cliente/{id}/servicios")
    fun obtenerServiciosCliente(
        @Path("id") id: Int
    ): Call<List<Servicio>>


    @POST("servicio")
    fun crearServicio(@Body servicio: Servicio): Call<Servicio>

    @PUT("servicio/{id}")
    fun actualizarServicio(
        @Path("id") id: Int,
        @Body servicio: Servicio
    ): Call<Servicio>


    @DELETE("servicio/{id}")
    fun eliminarServicio(
        @Path("id") id: Int
    ): Call<Void>
}