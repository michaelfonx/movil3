package com.example.appinterface.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val planApi: PlanApiService by lazy {
        retrofit.create(PlanApiService::class.java)
    }

    val api: ContratoApiService by lazy {
        retrofit.create(ContratoApiService::class.java)
    }

    val usuarioApi: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    val apiServicios: ApiServicioService by lazy {
        retrofit.create(ApiServicioService::class.java)
    }

    val dashboardApi: DashboardApiService by lazy {
        retrofit.create(DashboardApiService::class.java)
    }

    val pagoApi: PagoApiService by lazy {
        retrofit.create(PagoApiService::class.java)
    }

}