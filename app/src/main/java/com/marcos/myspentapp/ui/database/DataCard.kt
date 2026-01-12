package com.marcos.myspentapp.ui.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

// TABELA
@Entity(tableName = "cards")
data class CardData(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userEmail: String,
    val title: String,
    val value: Double,
    val type: String,
    val imageUri: String?,
)

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardData)

    @Query("SELECT * FROM cards WHERE userEmail = :email")
    suspend fun getCardsByUser(email: String): List<CardData>

    @Delete
    suspend fun deleteCard(card: CardData)

    @Query("DELETE FROM cards WHERE userEmail = :email")
    suspend fun deleteCardsByUser(email: String)

    @Update
    suspend fun updateCard(card: CardData)

}