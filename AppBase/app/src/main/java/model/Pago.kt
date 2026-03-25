package com.example.appinterface.model

data class Pago(
    val pago_id: Int? = null,
    val pago_metodo: String,
    val pago_fecha: String,
    val contrato_id: Int
)