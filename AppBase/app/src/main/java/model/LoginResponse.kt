package com.example.appinterface.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("token")
    val token: String?,

    @SerializedName("rol")
    val rol: String?,

    @SerializedName("usuario_id")
    val usuario_id: Int?,

    @SerializedName("cliente_id")
    val cliente_id: Int?,

    @SerializedName("usuario_primer_nombre")
    val usuario_primer_nombre: String?,

    @SerializedName("usuario_segundo_nombre")
    val usuario_segundo_nombre: String?,

    @SerializedName("usuario_primer_apellido")
    val usuario_primer_apellido: String?,

    @SerializedName("usuario_segundo_apellido")
    val usuario_segundo_apellido: String?,

    @SerializedName("usuario_correo")
    val usuario_correo: String?
)