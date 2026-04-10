package com.example.appinterface.Api

import model.DTO.AfiliadoDTO
import com.example.appinterface.model.Contrato
import com.example.appinterface.model.ContratoPlan
import model.DTO.MiPlanDTO
import example.appinterface.model.AdquirirPlanRequest
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

    @POST("api/adquirir-plan")
    fun adquirirPlan(
        @Body body: AdquirirPlanRequest
    ): Call<Map<String, Int>>
    @POST("api/afiliado")
    fun agregarAfiliado(
        @Body body: Map<String, Int>
    ): Call<String>

    @GET("api/afiliados/{contratoId}")
    fun obtenerAfiliados(
        @Path("contratoId") contratoId: Int
    ): Call<List<AfiliadoDTO>>

    @POST("api/afiliado-documento")
    fun agregarAfiliadoPorDocumento(
        @Body body: Map<String, Int>
    ): Call<String>
    @POST("api/contrato-producto")
    fun agregarProductoContrato(
        @Body body: Map<String, Int>
    ): Call<Map<String, String>>

    @GET("api/carrito/{usuarioId}")
    fun obtenerCarrito(
        @Path("usuarioId") usuarioId: Int
    ): Call<List<Map<String, Any>>>

    @GET("api/mi-plan/{clienteId}")
    fun obtenerMiPlan(
        @Path("clienteId") clienteId: Int
    ): Call<Map<String, Any>>

}
