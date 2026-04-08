package com.example.appinterface.manager

import com.example.appinterface.model.Producto

object CarritoManager {

    val lista = mutableListOf<Producto>()

    fun agregar(producto: Producto) {
        lista.add(producto)
    }

    fun eliminar(producto: Producto) {
        lista.remove(producto)
    }

    fun total(): Double {
        return lista.sumOf { it.producto_precio }
    }
}