package com.marcos.myspentapp.ui.database

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.marcos.myspentapp.ui.state.TypeGasto

// CONVERSOR
class Converters {

    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun toUri(uriString: String?): Uri? =
        uriString?.let { Uri.parse(it) }

    // Enum <-> String para tipo de gasto
    @TypeConverter
    fun fromType(type: TypeGasto): String = type.name

    @TypeConverter
    fun toType(name: String): TypeGasto =
        TypeGasto.valueOf(name)
}

@Database(
    entities = [UserSaved::class, CardData::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao

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