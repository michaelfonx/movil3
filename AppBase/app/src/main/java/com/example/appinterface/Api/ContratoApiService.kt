package com.example.appinterface.Api

import com.example.appinterface.model.Contrato
import com.example.appinterface.model.ContratoPlan
import com.example.appinterface.model.MiPlanDTO
import retrofit2.Call
import retrofit2.http.*

interface ContratoApiService {

    @GET("api/contratos")
    fun obtenerContratos(): Call<List<Contrato>>

    @GET("api/contratos/{id}")
    fun buscarPorId(@Path("id") id: Int): Call<Contrato>

    @POST("api/contratos")
    fun crearContrato(@Body contrato: Contrato): Call<Int>

    @PUT("api/contratos/{id}")
    fun actualizarContrato(
        @Path("id") id: Int,
        @Body contrato: Contrato
    ): Call<String>

    @DELETE("api/contratos/{id}")
    fun eliminarContrato(@Path("id") id: Int): Call<String>

    @POST("api/contrato-plan")
    fun crearContratoPlan(
        @Body contratoPlan: ContratoPlan
    ): Call<String>

    @GET("api/mi-plan/{clienteId}")
    fun obtenerMiPlan(
        @Path("clienteId") clienteId: Int
    ): Call<MiPlanDTO>
}