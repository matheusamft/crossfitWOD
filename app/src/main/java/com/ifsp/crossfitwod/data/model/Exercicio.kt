package com.ifsp.crossfitwod.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercicio(
    val nome: String,
    val repeticoes: String,
    val carga: Double? = null
)
