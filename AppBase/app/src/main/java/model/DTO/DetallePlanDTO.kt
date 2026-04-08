package com.example.appinterface.model
data class DetallePlanDTO(
    val plan: Plan,
    val servicios: List<Servicio>,
    val productos: List<Producto>?
)