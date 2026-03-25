package com.example.appinterface.Api
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.appinterface.model.DashboardClienteResponse
import com.example.appinterface.model.Servicio

interface DashboardApiService {
    @GET("api/dashboard/{id}")
    fun obtenerDashboard(
        @Path("id") id: Int
    ): Call<DashboardClienteResponse>
    @GET("servicio/cliente/{id}/servicios")
    fun obtenerServiciosCliente(
        @Path("id") id: Int
    ): Call<List<Servicio>>
}