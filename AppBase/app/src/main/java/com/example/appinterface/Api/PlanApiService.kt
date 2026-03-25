package com.example.appinterface.Api

import com.example.appinterface.model.Plan
import retrofit2.Call
import retrofit2.http.*

interface PlanApiService {

    // 🔥 GET (EL QUE USAS)
    @GET("api/planes")
    fun obtenerPlanes(): Call<List<Plan>>

    // 🔽 LOS DEMÁS (NO LOS USAS AHORA, pero quedan listos)

    @GET("api/planes/{id}")
    fun obtenerPlan(@Path("id") id: Int): Call<Plan>

    @POST("api/planes")
    fun crearPlan(@Body plan: Plan): Call<String>

    @PUT("api/planes/{id}")
    fun actualizarPlan(
        @Path("id") id: Int,
        @Body plan: Plan
    ): Call<String>

    @DELETE("api/planes/{id}")
    fun eliminarPlan(@Path("id") id: Int): Call<String>
}