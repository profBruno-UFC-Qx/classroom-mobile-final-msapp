package com.marcos.myspentapp.ui.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CardDao_Impl(
  __db: RoomDatabase,
) : CardDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCardData: EntityInsertAdapter<CardData>

  private val __deleteAdapterOfCardData: EntityDeleteOrUpdateAdapter<CardData>

  private val __updateAdapterOfCardData: EntityDeleteOrUpdateAdapter<CardData>
  init {
    this.__db = __db
    this.__insertAdapterOfCardData = object : EntityInsertAdapter<CardData>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `cards` (`id`,`userEmail`,`title`,`value`,`type`,`imageUri`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CardData) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.userEmail)
        statement.bindText(3, entity.title)
        statement.bindDouble(4, entity.value)
        statement.bindText(5, entity.type)
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpImageUri)
        }
      }
    }
    this.__deleteAdapterOfCardData = object : EntityDeleteOrUpdateAdapter<CardData>() {
      protected override fun createQuery(): String = "DELETE FROM `cards` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CardData) {
        statement.bindText(1, entity.id)
      }
    }
    this.__updateAdapterOfCardData = object : EntityDeleteOrUpdateAdapter<CardData>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `cards` SET `id` = ?,`userEmail` = ?,`title` = ?,`value` = ?,`type` = ?,`imageUri` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CardData) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.userEmail)
        statement.bindText(3, entity.title)
        statement.bindDouble(4, entity.value)
        statement.bindText(5, entity.type)
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpImageUri)
        }
        statement.bindText(7, entity.id)
      }
    }
  }

  public override suspend fun insertCard(card: CardData): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCardData.insert(_connection, card)
  }

  public override suspend fun deleteCard(card: CardData): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfCardData.handle(_connection, card)
  }

  public override suspend fun updateCard(card: CardData): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfCardData.handle(_connection, card)
  }

  public override suspend fun getCardsByUser(email: String): List<CardData> {
    val _sql: String = "SELECT * FROM cards WHERE userEmail = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, email)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserEmail: Int = getColumnIndexOrThrow(_stmt, "userEmail")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "imageUri")
        val _result: MutableList<CardData> = mutableListOf()
        while (_stmt.step()) {
          val _item: CardData
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUserEmail: String
          _tmpUserEmail = _stmt.getText(_columnIndexOfUserEmail)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpValue: Double
          _tmpValue = _stmt.getDouble(_columnIndexOfValue)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpImageUri: String?
          if (_stmt.isNull(_columnIndexOfImageUri)) {
            _tmpImageUri = null
          } else {
            _tmpImageUri = _stmt.getText(_columnIndexOfImageUri)
          }
          _item = CardData(_tmpId,_tmpUserEmail,_tmpTitle,_tmpValue,_tmpType,_tmpImageUri)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteCardsByUser(email: String) {
    val _sql: String = "DELETE FROM cards WHERE userEmail = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, email)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
