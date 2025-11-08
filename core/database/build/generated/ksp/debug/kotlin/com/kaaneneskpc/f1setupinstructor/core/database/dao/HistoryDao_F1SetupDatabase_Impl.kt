package com.kaaneneskpc.f1setupinstructor.core.database.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.kaaneneskpc.f1setupinstructor.core.database.Converters
import com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity
import java.time.Instant
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class HistoryDao_F1SetupDatabase_Impl(
  __db: RoomDatabase,
) : HistoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHistoryItemEntity: EntityInsertAdapter<HistoryItemEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfHistoryItemEntity: EntityDeleteOrUpdateAdapter<HistoryItemEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHistoryItemEntity = object : EntityInsertAdapter<HistoryItemEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `history` (`timestamp`,`circuit`,`weatherQuali`,`weatherRace`,`selectedSetupId`,`isFavorite`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HistoryItemEntity) {
        val _tmp: Long? = __converters.fromInstant(entity.timestamp)
        if (_tmp == null) {
          statement.bindNull(1)
        } else {
          statement.bindLong(1, _tmp)
        }
        statement.bindText(2, entity.circuit)
        statement.bindText(3, entity.weatherQuali)
        statement.bindText(4, entity.weatherRace)
        val _tmpSelectedSetupId: String? = entity.selectedSetupId
        if (_tmpSelectedSetupId == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpSelectedSetupId)
        }
        val _tmp_1: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
      }
    }
    this.__deleteAdapterOfHistoryItemEntity = object :
        EntityDeleteOrUpdateAdapter<HistoryItemEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `history` WHERE `timestamp` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HistoryItemEntity) {
        val _tmp: Long? = __converters.fromInstant(entity.timestamp)
        if (_tmp == null) {
          statement.bindNull(1)
        } else {
          statement.bindLong(1, _tmp)
        }
      }
    }
  }

  public override suspend fun insertHistory(history: HistoryItemEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfHistoryItemEntity.insert(_connection, history)
  }

  public override suspend fun deleteHistory(history: HistoryItemEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfHistoryItemEntity.handle(_connection, history)
  }

  public override fun getHistories(): Flow<List<HistoryItemEntity>> {
    val _sql: String = "SELECT * FROM history ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfCircuit: Int = getColumnIndexOrThrow(_stmt, "circuit")
        val _columnIndexOfWeatherQuali: Int = getColumnIndexOrThrow(_stmt, "weatherQuali")
        val _columnIndexOfWeatherRace: Int = getColumnIndexOrThrow(_stmt, "weatherRace")
        val _columnIndexOfSelectedSetupId: Int = getColumnIndexOrThrow(_stmt, "selectedSetupId")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<HistoryItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HistoryItemEntity
          val _tmpTimestamp: Instant
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfTimestamp)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfTimestamp)
          }
          val _tmp_1: Instant? = __converters.toInstant(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.Instant', but it was NULL.")
          } else {
            _tmpTimestamp = _tmp_1
          }
          val _tmpCircuit: String
          _tmpCircuit = _stmt.getText(_columnIndexOfCircuit)
          val _tmpWeatherQuali: String
          _tmpWeatherQuali = _stmt.getText(_columnIndexOfWeatherQuali)
          val _tmpWeatherRace: String
          _tmpWeatherRace = _stmt.getText(_columnIndexOfWeatherRace)
          val _tmpSelectedSetupId: String?
          if (_stmt.isNull(_columnIndexOfSelectedSetupId)) {
            _tmpSelectedSetupId = null
          } else {
            _tmpSelectedSetupId = _stmt.getText(_columnIndexOfSelectedSetupId)
          }
          val _tmpIsFavorite: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_2 != 0
          _item =
              HistoryItemEntity(_tmpTimestamp,_tmpCircuit,_tmpWeatherQuali,_tmpWeatherRace,_tmpSelectedSetupId,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getHistoryByTimestamp(timestamp: Long): HistoryItemEntity? {
    val _sql: String = "SELECT * FROM history WHERE timestamp = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfCircuit: Int = getColumnIndexOrThrow(_stmt, "circuit")
        val _columnIndexOfWeatherQuali: Int = getColumnIndexOrThrow(_stmt, "weatherQuali")
        val _columnIndexOfWeatherRace: Int = getColumnIndexOrThrow(_stmt, "weatherRace")
        val _columnIndexOfSelectedSetupId: Int = getColumnIndexOrThrow(_stmt, "selectedSetupId")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: HistoryItemEntity?
        if (_stmt.step()) {
          val _tmpTimestamp: Instant
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfTimestamp)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfTimestamp)
          }
          val _tmp_1: Instant? = __converters.toInstant(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.Instant', but it was NULL.")
          } else {
            _tmpTimestamp = _tmp_1
          }
          val _tmpCircuit: String
          _tmpCircuit = _stmt.getText(_columnIndexOfCircuit)
          val _tmpWeatherQuali: String
          _tmpWeatherQuali = _stmt.getText(_columnIndexOfWeatherQuali)
          val _tmpWeatherRace: String
          _tmpWeatherRace = _stmt.getText(_columnIndexOfWeatherRace)
          val _tmpSelectedSetupId: String?
          if (_stmt.isNull(_columnIndexOfSelectedSetupId)) {
            _tmpSelectedSetupId = null
          } else {
            _tmpSelectedSetupId = _stmt.getText(_columnIndexOfSelectedSetupId)
          }
          val _tmpIsFavorite: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_2 != 0
          _result =
              HistoryItemEntity(_tmpTimestamp,_tmpCircuit,_tmpWeatherQuali,_tmpWeatherRace,_tmpSelectedSetupId,_tmpIsFavorite)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
