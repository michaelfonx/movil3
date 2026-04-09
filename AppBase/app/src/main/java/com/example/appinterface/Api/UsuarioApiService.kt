package com.example.appinterface.Api

import com.example.appinterface.model.LoginRequest
import com.example.appinterface.model.LoginResponse
import com.example.appinterface.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UsuarioApiService {

    @POST("api/auth/registro")
    fun registrarUsuario(
        @Body usuario: Usuario
    ): Call<Map<String, String>>

    @POST("api/auth/login")
    fun login(
        @Body login: LoginRequest
    ): Call<LoginResponse>


    @PUT("api/usuarios/{id}")
    fun actualizarUsuario(
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Call<Map<String, String>>


    @DELETE("api/usuarios/{id}")
    fun eliminarUsuario(
        @Path("id") id: Int
    ): Call<Map<String, String>>
    @GET("api/usuarios/{id}")
    fun obtenerUsuario(
        @Path("id") id: Int
    ): Call<Usuario>


    @GET("api/usuarios/documento/{doc}")
    fun buscarPorDocumento(
        @Path("doc") doc: Int
    ): Call<Usuario>
}