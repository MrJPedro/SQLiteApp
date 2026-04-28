package com.example.sqliteapp.model

data class VeiculoModel(
    val id: Int = 0,
    val tipo: String,
    val nome: String,
    val preco: Int,
    val cor: String
){

    override fun toString(): String {
        return nome
    }
}