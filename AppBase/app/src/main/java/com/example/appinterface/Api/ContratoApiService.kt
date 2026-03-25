package com.example.appinterface.Api

import com.example.appinterface.model.Contrato
import com.example.appinterface.model.MiPlanDTO
import retrofit2.Call
import retrofit2.http.*

interface ContratoApiService {



    // GET TODOS
    @GET("api/cronogramas")
    fun obtenerCronogramas(): Call<List<Contrato>>

    // GET POR ID
    @GET("api/cronogramas/{id}")
    fun buscarPorId(
        @Path("id") id: Int
    ): Call<Contrato>

    // POST
    @POST("api/cronogramas")
    fun crearContrato(
        @Body contrato: Contrato
    ): Call<String>

    // PUT
    @PUT("api/cronogramas/{id}")
    fun actualizarContrato(
        @Path("id") id: Int,
        @Body contrato: Contrato
    ): Call<String>

    // DELETE
    @DELETE("api/cronogramas/{id}")
    fun eliminarContrato(
        @Path("id") id: Int
    ): Call<String>

    @GET("api/mi-plan/{clienteId}")
    fun obtenerMiPlan(
        @Path("clienteId") clienteId: Int
    ): Call<MiPlanDTO>
}