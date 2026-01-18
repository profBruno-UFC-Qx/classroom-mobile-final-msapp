package com.marcos.myspentapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marcos.myspentapp.data.models.Gastos
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(gasto: Gastos)

    @Query("SELECT * FROM cards WHERE userEmail = :email")
    fun getCardsByUser(email: String): Flow<List<Gastos>>

    @Update
    suspend fun updateCard(gasto: Gastos)


    @Delete
    suspend fun deleteCard(gasto: Gastos)

    @Query("DELETE FROM cards WHERE userEmail = :email")
    suspend fun deleteCardsByUser(email: String)

}

