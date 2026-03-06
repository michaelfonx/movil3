package com.example.appinterface.Api

import com.example.appinterface.Contrato
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("breed/hound/images")
    fun getHoundImages(): Call<DataResponse>

    @GET("api/cronogramas")
    fun obtenerCronogramas(): Call<List<Contrato>>

    @GET("api/cronogramas/{id}")
    fun buscarPorId(
        @Path("id") id: Int
    ): Call<Contrato>

    @POST("api/cronogramas")
    fun crearContrato(
        @Body contrato: Contrato
    ): Call<String>

    @PUT("api/cronogramas/{id}")
    fun actualizarContrato(
        @Path("id") id: Int,
        @Body contrato: Contrato
    ): Call<String>

    @DELETE("api/cronogramas/{id}")
    fun eliminarContrato(
        @Path("id") id: Int
    ): Call<String>
}