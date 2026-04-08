package model

data class ProductoUI(
    val producto_id: Int,
    val producto_nombre: String,
    val producto_precio: Double,
    val categoria_nombre: String,
    val subcategoria_nombre: String
)