package com.example.sqliteapp.model

import java.io.Serializable

data class VeiculoModel(
    var id: Int,
    var tipo: String,
    var nome: String,
    var preco: Int,
    var cor: String
): Serializable {

    override fun toString(): String {
        return "$nome - R$ $preco"
    }
}