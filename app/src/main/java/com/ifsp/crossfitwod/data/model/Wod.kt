package com.ifsp.crossfitwod.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Wod(
    val id: String,
    val nome: String,
    val tipo: String,
    val descricao: String,
    val tempoMinutos: Int? = null,
    val exercicios: List<Exercicio> = emptyList(),
    val resultados: List<Resultado> = emptyList()
)
