package com.example.appinterface.model

data class Plan(
    val plan_id: Int,
    val plan_nombre: String,
    val plan_precio: Double,
    val plan_estado: Boolean,
    val plan_descripcion: String?
)