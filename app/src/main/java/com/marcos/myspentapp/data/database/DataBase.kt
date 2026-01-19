package com.marcos.myspentapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marcos.myspentapp.data.dao.GastoDao
import com.marcos.myspentapp.data.dao.UserDao
import com.marcos.myspentapp.data.models.Gastos
import com.marcos.myspentapp.data.models.Users

@Database(
    entities = [Users::class, Gastos::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                )
                    .fallbackToDestructiveMigration(false) // evitar crashes durante migração
                    .build().also { INSTANCE = it }
            }
    }
}