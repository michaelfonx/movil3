package com.example.appinterface.model

data class DashboardClienteResponse(
    val contrato: Contrato?,
    val servicios: List<Servicio>,
    val afiliados: List<Usuario>,
    val pagos: List<String>
)