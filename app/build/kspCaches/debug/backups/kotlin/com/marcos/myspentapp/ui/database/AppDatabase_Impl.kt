package com.marcos.myspentapp.ui.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  private val _cardDao: Lazy<CardDao> = lazy {
    CardDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2, "707a2387da3e1dac7c030544eb2254f2", "8e0ded3277a31a5d7ec530b9be6260cb") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`email` TEXT NOT NULL, `name` TEXT, `senha` TEXT, `fotoUri` TEXT, `codeRescue` TEXT NOT NULL, `ganhos` REAL NOT NULL, `darkTheme` INTEGER NOT NULL, `initApp` INTEGER NOT NULL, PRIMARY KEY(`email`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `cards` (`id` TEXT NOT NULL, `userEmail` TEXT NOT NULL, `title` TEXT NOT NULL, `value` REAL NOT NULL, `type` TEXT NOT NULL, `imageUri` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '707a2387da3e1dac7c030544eb2254f2')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `cards`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("email", TableInfo.Column("email", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("name", TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("senha", TableInfo.Column("senha", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("fotoUri", TableInfo.Column("fotoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("codeRescue", TableInfo.Column("codeRescue", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("ganhos", TableInfo.Column("ganhos", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("darkTheme", TableInfo.Column("darkTheme", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("initApp", TableInfo.Column("initApp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.marcos.myspentapp.ui.database.UserSaved).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsCards: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCards.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("userEmail", TableInfo.Column("userEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("value", TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("type", TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("imageUri", TableInfo.Column("imageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCards: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCards: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCards: TableInfo = TableInfo("cards", _columnsCards, _foreignKeysCards, _indicesCards)
        val _existingCards: TableInfo = read(connection, "cards")
        if (!_infoCards.equals(_existingCards)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |cards(com.marcos.myspentapp.ui.database.CardData).
              | Expected:
              |""".trimMargin() + _infoCards + """
              |
              | Found:
              |""".trimMargin() + _existingCards)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "cards")
  }

  public override fun clearAllTables() {
    super.performClear(false, "users", "cards")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CardDao::class, CardDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun userDao(): UserDao = _userDao.value

  public override fun cardDao(): CardDao = _cardDao.value
}
