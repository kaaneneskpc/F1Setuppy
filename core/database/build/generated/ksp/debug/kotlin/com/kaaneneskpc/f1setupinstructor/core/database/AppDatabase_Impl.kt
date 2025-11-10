package com.kaaneneskpc.f1setupinstructor.core.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao_Impl
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao_Impl
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

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _setupDao: Lazy<SetupDao> = lazy {
    SetupDao_Impl(this)
  }

  private val _historyDao: Lazy<HistoryDao> = lazy {
    HistoryDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "831aaf27a69ee25a960b68123a2f5111", "fdfa68f50f0243821735e369707ace66") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `setups` (`sourceUrl` TEXT NOT NULL, `sourceName` TEXT NOT NULL, `sourcePublishedAt` INTEGER NOT NULL, `sourceCommunityRating` REAL, `gameVersion` TEXT NOT NULL, `patch` TEXT, `circuit` TEXT NOT NULL, `weatherQuali` TEXT NOT NULL, `weatherRace` TEXT NOT NULL, `style` TEXT NOT NULL, `notes` TEXT, `score` REAL NOT NULL, `aero_front` INTEGER NOT NULL, `aero_rear` INTEGER NOT NULL, `transmission_onThrottle` INTEGER NOT NULL, `transmission_offThrottle` INTEGER NOT NULL, `transmission_engineBraking` INTEGER NOT NULL, `suspension_geometry_frontCamber` REAL NOT NULL, `suspension_geometry_rearCamber` REAL NOT NULL, `suspension_geometry_frontToe` REAL NOT NULL, `suspension_geometry_rearToe` REAL NOT NULL, `suspension_frontSusp` INTEGER NOT NULL, `suspension_rearSusp` INTEGER NOT NULL, `suspension_frontARB` INTEGER NOT NULL, `suspension_rearARB` INTEGER NOT NULL, `suspension_frontRideHeight` INTEGER NOT NULL, `suspension_rearRideHeight` INTEGER NOT NULL, `brakes_pressure` INTEGER NOT NULL, `brakes_bias` INTEGER NOT NULL, `tyres_frontPsi` REAL NOT NULL, `tyres_rearPsi` REAL NOT NULL, PRIMARY KEY(`sourceUrl`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_setups_circuit_weatherQuali_weatherRace_patch` ON `setups` (`circuit`, `weatherQuali`, `weatherRace`, `patch`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `history` (`timestamp` INTEGER NOT NULL, `circuit` TEXT NOT NULL, `weatherQuali` TEXT NOT NULL, `weatherRace` TEXT NOT NULL, `selectedSetupId` TEXT, `isFavorite` INTEGER NOT NULL, PRIMARY KEY(`timestamp`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '831aaf27a69ee25a960b68123a2f5111')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `setups`")
        connection.execSQL("DROP TABLE IF EXISTS `history`")
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

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsSetups: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSetups.put("sourceUrl", TableInfo.Column("sourceUrl", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("sourceName", TableInfo.Column("sourceName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("sourcePublishedAt", TableInfo.Column("sourcePublishedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("sourceCommunityRating", TableInfo.Column("sourceCommunityRating",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("gameVersion", TableInfo.Column("gameVersion", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("patch", TableInfo.Column("patch", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("circuit", TableInfo.Column("circuit", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("weatherQuali", TableInfo.Column("weatherQuali", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("weatherRace", TableInfo.Column("weatherRace", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("style", TableInfo.Column("style", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("notes", TableInfo.Column("notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("score", TableInfo.Column("score", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("aero_front", TableInfo.Column("aero_front", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("aero_rear", TableInfo.Column("aero_rear", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("transmission_onThrottle", TableInfo.Column("transmission_onThrottle",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("transmission_offThrottle", TableInfo.Column("transmission_offThrottle",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("transmission_engineBraking",
            TableInfo.Column("transmission_engineBraking", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_geometry_frontCamber",
            TableInfo.Column("suspension_geometry_frontCamber", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_geometry_rearCamber",
            TableInfo.Column("suspension_geometry_rearCamber", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_geometry_frontToe",
            TableInfo.Column("suspension_geometry_frontToe", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_geometry_rearToe",
            TableInfo.Column("suspension_geometry_rearToe", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_frontSusp", TableInfo.Column("suspension_frontSusp",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_rearSusp", TableInfo.Column("suspension_rearSusp", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_frontARB", TableInfo.Column("suspension_frontARB", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_rearARB", TableInfo.Column("suspension_rearARB", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_frontRideHeight",
            TableInfo.Column("suspension_frontRideHeight", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("suspension_rearRideHeight",
            TableInfo.Column("suspension_rearRideHeight", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("brakes_pressure", TableInfo.Column("brakes_pressure", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("brakes_bias", TableInfo.Column("brakes_bias", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("tyres_frontPsi", TableInfo.Column("tyres_frontPsi", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSetups.put("tyres_rearPsi", TableInfo.Column("tyres_rearPsi", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSetups: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSetups: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSetups.add(TableInfo.Index("index_setups_circuit_weatherQuali_weatherRace_patch",
            false, listOf("circuit", "weatherQuali", "weatherRace", "patch"), listOf("ASC", "ASC",
            "ASC", "ASC")))
        val _infoSetups: TableInfo = TableInfo("setups", _columnsSetups, _foreignKeysSetups,
            _indicesSetups)
        val _existingSetups: TableInfo = read(connection, "setups")
        if (!_infoSetups.equals(_existingSetups)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |setups(com.kaaneneskpc.f1setupinstructor.core.database.entity.SetupEntity).
              | Expected:
              |""".trimMargin() + _infoSetups + """
              |
              | Found:
              |""".trimMargin() + _existingSetups)
        }
        val _columnsHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsHistory.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHistory.put("circuit", TableInfo.Column("circuit", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHistory.put("weatherQuali", TableInfo.Column("weatherQuali", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHistory.put("weatherRace", TableInfo.Column("weatherRace", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHistory.put("selectedSetupId", TableInfo.Column("selectedSetupId", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHistory.put("isFavorite", TableInfo.Column("isFavorite", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoHistory: TableInfo = TableInfo("history", _columnsHistory, _foreignKeysHistory,
            _indicesHistory)
        val _existingHistory: TableInfo = read(connection, "history")
        if (!_infoHistory.equals(_existingHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |history(com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity).
              | Expected:
              |""".trimMargin() + _infoHistory + """
              |
              | Found:
              |""".trimMargin() + _existingHistory)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "setups", "history")
  }

  public override fun clearAllTables() {
    super.performClear(false, "setups", "history")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(SetupDao::class, SetupDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(HistoryDao::class, HistoryDao_AppDatabase_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun setupDao(): SetupDao = _setupDao.value

  public override fun historyDao(): HistoryDao = _historyDao.value
}
