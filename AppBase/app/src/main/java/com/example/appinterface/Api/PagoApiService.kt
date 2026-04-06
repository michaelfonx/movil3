package com.example.appinterface.Api

import com.example.appinterface.model.Pago
import retrofit2.Call
import retrofit2.http.*

interface PagoApiService {

    @GET("api/pagos/contrato/{id}")
    fun obtenerPagosPorContrato(
        @Path("id") contratoId: Int
    ): Call<List<Pago>>

    @POST("api/pagos")
    fun crearPago(
        @Body pago: Pago
    ): Call<Void>

    @PUT("api/pagos/{id}")
    fun actualizarPago(
        @Path("id") id: Int,
        @Body pago: Pago
    ): Call<Pago>

    @DELETE("api/pagos/{id}")
    fun eliminarPago(
        @Path("id") id: Int
    ): Call<Void>
}