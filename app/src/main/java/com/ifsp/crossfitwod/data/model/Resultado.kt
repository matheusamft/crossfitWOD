package com.ifsp.crossfitwod.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Resultado(
    val id: String,
    val data: String,
    val score: String,
    val observacao: String
)
