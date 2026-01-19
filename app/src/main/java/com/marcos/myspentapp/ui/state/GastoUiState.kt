package com.marcos.myspentapp.ui.state

import android.net.Uri
import com.marcos.myspentapp.data.models.TypeGasto
import java.util.UUID

data class GastoUiState (
    val id: String = UUID.randomUUID().toString(),
    val imageUri: Uri?,
    val title: String,
    val value: Double,
    val type: TypeGasto = TypeGasto.NONE
)

