package example.appinterface.model

data class AdquirirPlanRequest(
    val cliente_id: Int,
    val plan_id: Int,
    val valor: Double
)