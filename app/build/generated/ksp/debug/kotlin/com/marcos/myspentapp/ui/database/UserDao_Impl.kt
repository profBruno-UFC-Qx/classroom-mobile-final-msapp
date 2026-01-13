package com.marcos.myspentapp.ui.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserDao_Impl(
  __db: RoomDatabase,
) : UserDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserSaved: EntityInsertAdapter<UserSaved>

  private val __updateAdapterOfUserSaved: EntityDeleteOrUpdateAdapter<UserSaved>
  init {
    this.__db = __db
    this.__insertAdapterOfUserSaved = object : EntityInsertAdapter<UserSaved>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `users` (`email`,`name`,`senha`,`fotoUri`,`codeRescue`,`ganhos`,`darkTheme`,`initApp`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserSaved) {
        statement.bindText(1, entity.email)
        val _tmpName: String? = entity.name
        if (_tmpName == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpName)
        }
        val _tmpSenha: String? = entity.senha
        if (_tmpSenha == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpSenha)
        }
        val _tmpFotoUri: String? = entity.fotoUri
        if (_tmpFotoUri == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpFotoUri)
        }
        statement.bindText(5, entity.codeRescue)
        statement.bindDouble(6, entity.ganhos)
        val _tmp: Int = if (entity.darkTheme) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Int = if (entity.initApp) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
      }
    }
    this.__updateAdapterOfUserSaved = object : EntityDeleteOrUpdateAdapter<UserSaved>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `users` SET `email` = ?,`name` = ?,`senha` = ?,`fotoUri` = ?,`codeRescue` = ?,`ganhos` = ?,`darkTheme` = ?,`initApp` = ? WHERE `email` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: UserSaved) {
        statement.bindText(1, entity.email)
        val _tmpName: String? = entity.name
        if (_tmpName == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpName)
        }
        val _tmpSenha: String? = entity.senha
        if (_tmpSenha == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpSenha)
        }
        val _tmpFotoUri: String? = entity.fotoUri
        if (_tmpFotoUri == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpFotoUri)
        }
        statement.bindText(5, entity.codeRescue)
        statement.bindDouble(6, entity.ganhos)
        val _tmp: Int = if (entity.darkTheme) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Int = if (entity.initApp) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
        statement.bindText(9, entity.email)
      }
    }
  }

  public override suspend fun insertUser(user: UserSaved): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUserSaved.insert(_connection, user)
  }

  public override suspend fun updateUser(user: UserSaved): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfUserSaved.handle(_connection, user)
  }

  public override suspend fun getUserByEmail(email: String): UserSaved? {
    val _sql: String = "SELECT * FROM users WHERE email = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, email)
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfSenha: Int = getColumnIndexOrThrow(_stmt, "senha")
        val _columnIndexOfFotoUri: Int = getColumnIndexOrThrow(_stmt, "fotoUri")
        val _columnIndexOfCodeRescue: Int = getColumnIndexOrThrow(_stmt, "codeRescue")
        val _columnIndexOfGanhos: Int = getColumnIndexOrThrow(_stmt, "ganhos")
        val _columnIndexOfDarkTheme: Int = getColumnIndexOrThrow(_stmt, "darkTheme")
        val _columnIndexOfInitApp: Int = getColumnIndexOrThrow(_stmt, "initApp")
        val _result: UserSaved?
        if (_stmt.step()) {
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpName: String?
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName)
          }
          val _tmpSenha: String?
          if (_stmt.isNull(_columnIndexOfSenha)) {
            _tmpSenha = null
          } else {
            _tmpSenha = _stmt.getText(_columnIndexOfSenha)
          }
          val _tmpFotoUri: String?
          if (_stmt.isNull(_columnIndexOfFotoUri)) {
            _tmpFotoUri = null
          } else {
            _tmpFotoUri = _stmt.getText(_columnIndexOfFotoUri)
          }
          val _tmpCodeRescue: String
          _tmpCodeRescue = _stmt.getText(_columnIndexOfCodeRescue)
          val _tmpGanhos: Double
          _tmpGanhos = _stmt.getDouble(_columnIndexOfGanhos)
          val _tmpDarkTheme: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDarkTheme).toInt()
          _tmpDarkTheme = _tmp != 0
          val _tmpInitApp: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfInitApp).toInt()
          _tmpInitApp = _tmp_1 != 0
          _result = UserSaved(_tmpEmail,_tmpName,_tmpSenha,_tmpFotoUri,_tmpCodeRescue,_tmpGanhos,_tmpDarkTheme,_tmpInitApp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clear(email: String) {
    val _sql: String = "DELETE FROM users WHERE email = ?"
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
