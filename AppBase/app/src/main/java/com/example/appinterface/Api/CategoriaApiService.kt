package com.example.appinterface.Api

import com.example.appinterface.model.Categoria
import retrofit2.Call
import retrofit2.http.GET

interface CategoriaApiService {

    @GET("api/categorias")
    fun obtenerCategorias(): Call<List<Categoria>>
}