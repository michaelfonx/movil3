package com.example.appinterface.Api

import com.example.appinterface.model.AfiliadoDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AfiliadoApiService {

    @POST("api/afiliado-documento")
    fun agregarAfiliadoPorDocumento(
        @Body body: Map<String, Int>
    ): Call<ResponseBody>

    @GET("api/afiliados/{contratoId}")
    fun obtenerAfiliados(
        @Path("contratoId") contratoId: Int
    ): Call<List<AfiliadoDTO>>
}