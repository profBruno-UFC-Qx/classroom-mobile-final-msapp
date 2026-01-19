package com.marcos.myspentapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// TABELA
@Entity(tableName = "cards")
data class Gastos(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userEmail: String,
    val title: String,
    val value: Double,
    val type: String,
    val imageUri: String?,
)

enum class TypeGasto {
    LAZER,
    MORADIA,
    EDUCACAO,
    IMPREVISTOS,
    ALIMENTACAO,
    TRANSPORTE,
    SAUDE,
    OUTROS,
    NONE
}

fun List<Gastos>.toGastosMap(): Map<String, Double> {
    return this
        .groupBy { it.type }
        .mapValues { entry -> entry.value.sumOf { it.value } }
}