package com.example.appinterface.Api

import com.example.appinterface.model.Subcategoria
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SubcategoriaApiService {

    @GET("api/subcategorias/categoria/{id}")
    fun obtenerSubcategorias(
        @Path("id") id: Int
    ): Call<List<Subcategoria>>


}