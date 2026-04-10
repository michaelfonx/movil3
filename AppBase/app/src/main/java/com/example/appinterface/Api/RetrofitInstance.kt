package com.example.appinterface.Api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val carritoApi: CarritoApiService by lazy {
        retrofit.create(CarritoApiService::class.java)
    }

    val productoApi: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    val categoriaApi: CategoriaApiService by lazy {
        retrofit.create(CategoriaApiService::class.java)
    }

    val subcategoriaApi: SubcategoriaApiService by lazy {
        retrofit.create(SubcategoriaApiService::class.java)
    }

    val usuarioApi: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    val planApi: PlanApiService by lazy {
        retrofit.create(PlanApiService::class.java)
    }

    val api: ContratoApiService by lazy {
        retrofit.create(ContratoApiService::class.java)
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

    val afiliadoApi: AfiliadoApiService by lazy {
        retrofit.create(AfiliadoApiService::class.java)
    }
    val contratoApi: ContratoApiService by lazy {
        retrofit.create(ContratoApiService::class.java)
    }
}