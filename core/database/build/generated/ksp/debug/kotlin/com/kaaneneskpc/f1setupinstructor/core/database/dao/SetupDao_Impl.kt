package com.kaaneneskpc.f1setupinstructor.core.database.dao

import androidx.paging.PagingSource
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.RoomRawQuery
import androidx.room.paging.LimitOffsetPagingSource
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.kaaneneskpc.f1setupinstructor.core.database.Converters
import com.kaaneneskpc.f1setupinstructor.core.database.entity.AeroEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.BrakesEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SetupEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SuspensionEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SuspensionGeometryEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.TransmissionEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.TyresEntity
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import java.time.Instant
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SetupDao_Impl(
  __db: RoomDatabase,
) : SetupDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSetupEntity: EntityInsertAdapter<SetupEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfSetupEntity = object : EntityInsertAdapter<SetupEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `setups` (`sourceUrl`,`sourceName`,`sourcePublishedAt`,`sourceCommunityRating`,`gameVersion`,`patch`,`circuit`,`weatherQuali`,`weatherRace`,`style`,`notes`,`score`,`aero_front`,`aero_rear`,`transmission_onThrottle`,`transmission_offThrottle`,`transmission_engineBraking`,`suspension_geometry_frontCamber`,`suspension_geometry_rearCamber`,`suspension_geometry_frontToe`,`suspension_geometry_rearToe`,`suspension_frontSusp`,`suspension_rearSusp`,`suspension_frontARB`,`suspension_rearARB`,`suspension_frontRideHeight`,`suspension_rearRideHeight`,`brakes_pressure`,`brakes_bias`,`tyres_frontPsi`,`tyres_rearPsi`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SetupEntity) {
        statement.bindText(1, entity.sourceUrl)
        statement.bindText(2, entity.sourceName)
        val _tmp: Long? = __converters.fromInstant(entity.sourcePublishedAt)
        if (_tmp == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmp)
        }
        val _tmpSourceCommunityRating: Double? = entity.sourceCommunityRating
        if (_tmpSourceCommunityRating == null) {
          statement.bindNull(4)
        } else {
          statement.bindDouble(4, _tmpSourceCommunityRating)
        }
        statement.bindText(5, entity.gameVersion)
        val _tmpPatch: String? = entity.patch
        if (_tmpPatch == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpPatch)
        }
        statement.bindText(7, entity.circuit)
        statement.bindText(8, entity.weatherQuali)
        statement.bindText(9, entity.weatherRace)
        val _tmp_1: String? = __converters.fromSetupStyle(entity.style)
        if (_tmp_1 == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmp_1)
        }
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpNotes)
        }
        statement.bindDouble(12, entity.score)
        val _tmpAero: AeroEntity = entity.aero
        statement.bindLong(13, _tmpAero.front.toLong())
        statement.bindLong(14, _tmpAero.rear.toLong())
        val _tmpTransmission: TransmissionEntity = entity.transmission
        statement.bindLong(15, _tmpTransmission.onThrottle.toLong())
        statement.bindLong(16, _tmpTransmission.offThrottle.toLong())
        statement.bindLong(17, _tmpTransmission.engineBraking.toLong())
        val _tmpSuspensionGeometry: SuspensionGeometryEntity = entity.suspensionGeometry
        statement.bindDouble(18, _tmpSuspensionGeometry.frontCamber)
        statement.bindDouble(19, _tmpSuspensionGeometry.rearCamber)
        statement.bindDouble(20, _tmpSuspensionGeometry.frontToe)
        statement.bindDouble(21, _tmpSuspensionGeometry.rearToe)
        val _tmpSuspension: SuspensionEntity = entity.suspension
        statement.bindLong(22, _tmpSuspension.frontSusp.toLong())
        statement.bindLong(23, _tmpSuspension.rearSusp.toLong())
        statement.bindLong(24, _tmpSuspension.frontARB.toLong())
        statement.bindLong(25, _tmpSuspension.rearARB.toLong())
        statement.bindLong(26, _tmpSuspension.frontRideHeight.toLong())
        statement.bindLong(27, _tmpSuspension.rearRideHeight.toLong())
        val _tmpBrakes: BrakesEntity = entity.brakes
        statement.bindLong(28, _tmpBrakes.pressure.toLong())
        statement.bindLong(29, _tmpBrakes.bias.toLong())
        val _tmpTyres: TyresEntity = entity.tyres
        statement.bindDouble(30, _tmpTyres.frontPsi)
        statement.bindDouble(31, _tmpTyres.rearPsi)
      }
    }
  }

  public override suspend fun insert(setup: SetupEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfSetupEntity.insert(_connection, setup)
  }

  public override suspend fun insertAll(setups: List<SetupEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfSetupEntity.insert(_connection, setups)
  }

  public override fun getSetups(
    circuit: String,
    qualiWeather: String,
    raceWeather: String,
  ): PagingSource<Int, SetupEntity> {
    val _sql: String = """
        |
        |        SELECT * FROM setups 
        |        WHERE circuit = ? 
        |        AND weatherQuali = ? 
        |        AND weatherRace = ? 
        |        ORDER BY score DESC, sourcePublishedAt DESC
        |    
        """.trimMargin()
    val _rawQuery: RoomRawQuery = RoomRawQuery(_sql) { _stmt ->
      var _argIndex: Int = 1
      _stmt.bindText(_argIndex, circuit)
      _argIndex = 2
      _stmt.bindText(_argIndex, qualiWeather)
      _argIndex = 3
      _stmt.bindText(_argIndex, raceWeather)
    }
    return object : LimitOffsetPagingSource<SetupEntity>(_rawQuery, __db, "setups") {
      protected override suspend fun convertRows(limitOffsetQuery: RoomRawQuery, itemCount: Int):
          List<SetupEntity> = performSuspending(__db, true, false) { _connection ->
        val _stmt: SQLiteStatement = _connection.prepare(limitOffsetQuery.sql)
        limitOffsetQuery.getBindingFunction().invoke(_stmt)
        try {
          val _columnIndexOfSourceUrl: Int = getColumnIndexOrThrow(_stmt, "sourceUrl")
          val _columnIndexOfSourceName: Int = getColumnIndexOrThrow(_stmt, "sourceName")
          val _columnIndexOfSourcePublishedAt: Int = getColumnIndexOrThrow(_stmt,
              "sourcePublishedAt")
          val _columnIndexOfSourceCommunityRating: Int = getColumnIndexOrThrow(_stmt,
              "sourceCommunityRating")
          val _columnIndexOfGameVersion: Int = getColumnIndexOrThrow(_stmt, "gameVersion")
          val _columnIndexOfPatch: Int = getColumnIndexOrThrow(_stmt, "patch")
          val _columnIndexOfCircuit: Int = getColumnIndexOrThrow(_stmt, "circuit")
          val _columnIndexOfWeatherQuali: Int = getColumnIndexOrThrow(_stmt, "weatherQuali")
          val _columnIndexOfWeatherRace: Int = getColumnIndexOrThrow(_stmt, "weatherRace")
          val _columnIndexOfStyle: Int = getColumnIndexOrThrow(_stmt, "style")
          val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
          val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
          val _columnIndexOfFront: Int = getColumnIndexOrThrow(_stmt, "aero_front")
          val _columnIndexOfRear: Int = getColumnIndexOrThrow(_stmt, "aero_rear")
          val _columnIndexOfOnThrottle: Int = getColumnIndexOrThrow(_stmt,
              "transmission_onThrottle")
          val _columnIndexOfOffThrottle: Int = getColumnIndexOrThrow(_stmt,
              "transmission_offThrottle")
          val _columnIndexOfEngineBraking: Int = getColumnIndexOrThrow(_stmt,
              "transmission_engineBraking")
          val _columnIndexOfFrontCamber: Int = getColumnIndexOrThrow(_stmt,
              "suspension_geometry_frontCamber")
          val _columnIndexOfRearCamber: Int = getColumnIndexOrThrow(_stmt,
              "suspension_geometry_rearCamber")
          val _columnIndexOfFrontToe: Int = getColumnIndexOrThrow(_stmt,
              "suspension_geometry_frontToe")
          val _columnIndexOfRearToe: Int = getColumnIndexOrThrow(_stmt,
              "suspension_geometry_rearToe")
          val _columnIndexOfFrontSusp: Int = getColumnIndexOrThrow(_stmt, "suspension_frontSusp")
          val _columnIndexOfRearSusp: Int = getColumnIndexOrThrow(_stmt, "suspension_rearSusp")
          val _columnIndexOfFrontARB: Int = getColumnIndexOrThrow(_stmt, "suspension_frontARB")
          val _columnIndexOfRearARB: Int = getColumnIndexOrThrow(_stmt, "suspension_rearARB")
          val _columnIndexOfFrontRideHeight: Int = getColumnIndexOrThrow(_stmt,
              "suspension_frontRideHeight")
          val _columnIndexOfRearRideHeight: Int = getColumnIndexOrThrow(_stmt,
              "suspension_rearRideHeight")
          val _columnIndexOfPressure: Int = getColumnIndexOrThrow(_stmt, "brakes_pressure")
          val _columnIndexOfBias: Int = getColumnIndexOrThrow(_stmt, "brakes_bias")
          val _columnIndexOfFrontPsi: Int = getColumnIndexOrThrow(_stmt, "tyres_frontPsi")
          val _columnIndexOfRearPsi: Int = getColumnIndexOrThrow(_stmt, "tyres_rearPsi")
          val _result: MutableList<SetupEntity> = mutableListOf()
          while (_stmt.step()) {
            val _item: SetupEntity
            val _tmpSourceUrl: String
            _tmpSourceUrl = _stmt.getText(_columnIndexOfSourceUrl)
            val _tmpSourceName: String
            _tmpSourceName = _stmt.getText(_columnIndexOfSourceName)
            val _tmpSourcePublishedAt: Instant
            val _tmp: Long?
            if (_stmt.isNull(_columnIndexOfSourcePublishedAt)) {
              _tmp = null
            } else {
              _tmp = _stmt.getLong(_columnIndexOfSourcePublishedAt)
            }
            val _tmp_1: Instant? = __converters.toInstant(_tmp)
            if (_tmp_1 == null) {
              error("Expected NON-NULL 'java.time.Instant', but it was NULL.")
            } else {
              _tmpSourcePublishedAt = _tmp_1
            }
            val _tmpSourceCommunityRating: Double?
            if (_stmt.isNull(_columnIndexOfSourceCommunityRating)) {
              _tmpSourceCommunityRating = null
            } else {
              _tmpSourceCommunityRating = _stmt.getDouble(_columnIndexOfSourceCommunityRating)
            }
            val _tmpGameVersion: String
            _tmpGameVersion = _stmt.getText(_columnIndexOfGameVersion)
            val _tmpPatch: String?
            if (_stmt.isNull(_columnIndexOfPatch)) {
              _tmpPatch = null
            } else {
              _tmpPatch = _stmt.getText(_columnIndexOfPatch)
            }
            val _tmpCircuit: String
            _tmpCircuit = _stmt.getText(_columnIndexOfCircuit)
            val _tmpWeatherQuali: String
            _tmpWeatherQuali = _stmt.getText(_columnIndexOfWeatherQuali)
            val _tmpWeatherRace: String
            _tmpWeatherRace = _stmt.getText(_columnIndexOfWeatherRace)
            val _tmpStyle: SetupStyle
            val _tmp_2: String?
            if (_stmt.isNull(_columnIndexOfStyle)) {
              _tmp_2 = null
            } else {
              _tmp_2 = _stmt.getText(_columnIndexOfStyle)
            }
            val _tmp_3: SetupStyle? = __converters.toSetupStyle(_tmp_2)
            if (_tmp_3 == null) {
              error("Expected NON-NULL 'com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle', but it was NULL.")
            } else {
              _tmpStyle = _tmp_3
            }
            val _tmpNotes: String?
            if (_stmt.isNull(_columnIndexOfNotes)) {
              _tmpNotes = null
            } else {
              _tmpNotes = _stmt.getText(_columnIndexOfNotes)
            }
            val _tmpScore: Double
            _tmpScore = _stmt.getDouble(_columnIndexOfScore)
            val _tmpAero: AeroEntity
            val _tmpFront: Int
            _tmpFront = _stmt.getLong(_columnIndexOfFront).toInt()
            val _tmpRear: Int
            _tmpRear = _stmt.getLong(_columnIndexOfRear).toInt()
            _tmpAero = AeroEntity(_tmpFront,_tmpRear)
            val _tmpTransmission: TransmissionEntity
            val _tmpOnThrottle: Int
            _tmpOnThrottle = _stmt.getLong(_columnIndexOfOnThrottle).toInt()
            val _tmpOffThrottle: Int
            _tmpOffThrottle = _stmt.getLong(_columnIndexOfOffThrottle).toInt()
            val _tmpEngineBraking: Int
            _tmpEngineBraking = _stmt.getLong(_columnIndexOfEngineBraking).toInt()
            _tmpTransmission = TransmissionEntity(_tmpOnThrottle,_tmpOffThrottle,_tmpEngineBraking)
            val _tmpSuspensionGeometry: SuspensionGeometryEntity
            val _tmpFrontCamber: Double
            _tmpFrontCamber = _stmt.getDouble(_columnIndexOfFrontCamber)
            val _tmpRearCamber: Double
            _tmpRearCamber = _stmt.getDouble(_columnIndexOfRearCamber)
            val _tmpFrontToe: Double
            _tmpFrontToe = _stmt.getDouble(_columnIndexOfFrontToe)
            val _tmpRearToe: Double
            _tmpRearToe = _stmt.getDouble(_columnIndexOfRearToe)
            _tmpSuspensionGeometry =
                SuspensionGeometryEntity(_tmpFrontCamber,_tmpRearCamber,_tmpFrontToe,_tmpRearToe)
            val _tmpSuspension: SuspensionEntity
            val _tmpFrontSusp: Int
            _tmpFrontSusp = _stmt.getLong(_columnIndexOfFrontSusp).toInt()
            val _tmpRearSusp: Int
            _tmpRearSusp = _stmt.getLong(_columnIndexOfRearSusp).toInt()
            val _tmpFrontARB: Int
            _tmpFrontARB = _stmt.getLong(_columnIndexOfFrontARB).toInt()
            val _tmpRearARB: Int
            _tmpRearARB = _stmt.getLong(_columnIndexOfRearARB).toInt()
            val _tmpFrontRideHeight: Int
            _tmpFrontRideHeight = _stmt.getLong(_columnIndexOfFrontRideHeight).toInt()
            val _tmpRearRideHeight: Int
            _tmpRearRideHeight = _stmt.getLong(_columnIndexOfRearRideHeight).toInt()
            _tmpSuspension =
                SuspensionEntity(_tmpFrontSusp,_tmpRearSusp,_tmpFrontARB,_tmpRearARB,_tmpFrontRideHeight,_tmpRearRideHeight)
            val _tmpBrakes: BrakesEntity
            val _tmpPressure: Int
            _tmpPressure = _stmt.getLong(_columnIndexOfPressure).toInt()
            val _tmpBias: Int
            _tmpBias = _stmt.getLong(_columnIndexOfBias).toInt()
            _tmpBrakes = BrakesEntity(_tmpPressure,_tmpBias)
            val _tmpTyres: TyresEntity
            val _tmpFrontPsi: Double
            _tmpFrontPsi = _stmt.getDouble(_columnIndexOfFrontPsi)
            val _tmpRearPsi: Double
            _tmpRearPsi = _stmt.getDouble(_columnIndexOfRearPsi)
            _tmpTyres = TyresEntity(_tmpFrontPsi,_tmpRearPsi)
            _item =
                SetupEntity(_tmpSourceUrl,_tmpSourceName,_tmpSourcePublishedAt,_tmpSourceCommunityRating,_tmpGameVersion,_tmpPatch,_tmpCircuit,_tmpWeatherQuali,_tmpWeatherRace,_tmpStyle,_tmpAero,_tmpTransmission,_tmpSuspensionGeometry,_tmpSuspension,_tmpBrakes,_tmpTyres,_tmpNotes,_tmpScore)
            _result.add(_item)
          }
          _result
        } finally {
          _stmt.close()
        }
      }
    }
  }

  public override suspend fun getSetupBySourceUrl(sourceUrl: String): SetupEntity? {
    val _sql: String = "SELECT * FROM setups WHERE sourceUrl = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, sourceUrl)
        val _columnIndexOfSourceUrl: Int = getColumnIndexOrThrow(_stmt, "sourceUrl")
        val _columnIndexOfSourceName: Int = getColumnIndexOrThrow(_stmt, "sourceName")
        val _columnIndexOfSourcePublishedAt: Int = getColumnIndexOrThrow(_stmt, "sourcePublishedAt")
        val _columnIndexOfSourceCommunityRating: Int = getColumnIndexOrThrow(_stmt,
            "sourceCommunityRating")
        val _columnIndexOfGameVersion: Int = getColumnIndexOrThrow(_stmt, "gameVersion")
        val _columnIndexOfPatch: Int = getColumnIndexOrThrow(_stmt, "patch")
        val _columnIndexOfCircuit: Int = getColumnIndexOrThrow(_stmt, "circuit")
        val _columnIndexOfWeatherQuali: Int = getColumnIndexOrThrow(_stmt, "weatherQuali")
        val _columnIndexOfWeatherRace: Int = getColumnIndexOrThrow(_stmt, "weatherRace")
        val _columnIndexOfStyle: Int = getColumnIndexOrThrow(_stmt, "style")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfFront: Int = getColumnIndexOrThrow(_stmt, "aero_front")
        val _columnIndexOfRear: Int = getColumnIndexOrThrow(_stmt, "aero_rear")
        val _columnIndexOfOnThrottle: Int = getColumnIndexOrThrow(_stmt, "transmission_onThrottle")
        val _columnIndexOfOffThrottle: Int = getColumnIndexOrThrow(_stmt,
            "transmission_offThrottle")
        val _columnIndexOfEngineBraking: Int = getColumnIndexOrThrow(_stmt,
            "transmission_engineBraking")
        val _columnIndexOfFrontCamber: Int = getColumnIndexOrThrow(_stmt,
            "suspension_geometry_frontCamber")
        val _columnIndexOfRearCamber: Int = getColumnIndexOrThrow(_stmt,
            "suspension_geometry_rearCamber")
        val _columnIndexOfFrontToe: Int = getColumnIndexOrThrow(_stmt,
            "suspension_geometry_frontToe")
        val _columnIndexOfRearToe: Int = getColumnIndexOrThrow(_stmt, "suspension_geometry_rearToe")
        val _columnIndexOfFrontSusp: Int = getColumnIndexOrThrow(_stmt, "suspension_frontSusp")
        val _columnIndexOfRearSusp: Int = getColumnIndexOrThrow(_stmt, "suspension_rearSusp")
        val _columnIndexOfFrontARB: Int = getColumnIndexOrThrow(_stmt, "suspension_frontARB")
        val _columnIndexOfRearARB: Int = getColumnIndexOrThrow(_stmt, "suspension_rearARB")
        val _columnIndexOfFrontRideHeight: Int = getColumnIndexOrThrow(_stmt,
            "suspension_frontRideHeight")
        val _columnIndexOfRearRideHeight: Int = getColumnIndexOrThrow(_stmt,
            "suspension_rearRideHeight")
        val _columnIndexOfPressure: Int = getColumnIndexOrThrow(_stmt, "brakes_pressure")
        val _columnIndexOfBias: Int = getColumnIndexOrThrow(_stmt, "brakes_bias")
        val _columnIndexOfFrontPsi: Int = getColumnIndexOrThrow(_stmt, "tyres_frontPsi")
        val _columnIndexOfRearPsi: Int = getColumnIndexOrThrow(_stmt, "tyres_rearPsi")
        val _result: SetupEntity?
        if (_stmt.step()) {
          val _tmpSourceUrl: String
          _tmpSourceUrl = _stmt.getText(_columnIndexOfSourceUrl)
          val _tmpSourceName: String
          _tmpSourceName = _stmt.getText(_columnIndexOfSourceName)
          val _tmpSourcePublishedAt: Instant
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfSourcePublishedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfSourcePublishedAt)
          }
          val _tmp_1: Instant? = __converters.toInstant(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.Instant', but it was NULL.")
          } else {
            _tmpSourcePublishedAt = _tmp_1
          }
          val _tmpSourceCommunityRating: Double?
          if (_stmt.isNull(_columnIndexOfSourceCommunityRating)) {
            _tmpSourceCommunityRating = null
          } else {
            _tmpSourceCommunityRating = _stmt.getDouble(_columnIndexOfSourceCommunityRating)
          }
          val _tmpGameVersion: String
          _tmpGameVersion = _stmt.getText(_columnIndexOfGameVersion)
          val _tmpPatch: String?
          if (_stmt.isNull(_columnIndexOfPatch)) {
            _tmpPatch = null
          } else {
            _tmpPatch = _stmt.getText(_columnIndexOfPatch)
          }
          val _tmpCircuit: String
          _tmpCircuit = _stmt.getText(_columnIndexOfCircuit)
          val _tmpWeatherQuali: String
          _tmpWeatherQuali = _stmt.getText(_columnIndexOfWeatherQuali)
          val _tmpWeatherRace: String
          _tmpWeatherRace = _stmt.getText(_columnIndexOfWeatherRace)
          val _tmpStyle: SetupStyle
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfStyle)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfStyle)
          }
          val _tmp_3: SetupStyle? = __converters.toSetupStyle(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle', but it was NULL.")
          } else {
            _tmpStyle = _tmp_3
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpScore: Double
          _tmpScore = _stmt.getDouble(_columnIndexOfScore)
          val _tmpAero: AeroEntity
          val _tmpFront: Int
          _tmpFront = _stmt.getLong(_columnIndexOfFront).toInt()
          val _tmpRear: Int
          _tmpRear = _stmt.getLong(_columnIndexOfRear).toInt()
          _tmpAero = AeroEntity(_tmpFront,_tmpRear)
          val _tmpTransmission: TransmissionEntity
          val _tmpOnThrottle: Int
          _tmpOnThrottle = _stmt.getLong(_columnIndexOfOnThrottle).toInt()
          val _tmpOffThrottle: Int
          _tmpOffThrottle = _stmt.getLong(_columnIndexOfOffThrottle).toInt()
          val _tmpEngineBraking: Int
          _tmpEngineBraking = _stmt.getLong(_columnIndexOfEngineBraking).toInt()
          _tmpTransmission = TransmissionEntity(_tmpOnThrottle,_tmpOffThrottle,_tmpEngineBraking)
          val _tmpSuspensionGeometry: SuspensionGeometryEntity
          val _tmpFrontCamber: Double
          _tmpFrontCamber = _stmt.getDouble(_columnIndexOfFrontCamber)
          val _tmpRearCamber: Double
          _tmpRearCamber = _stmt.getDouble(_columnIndexOfRearCamber)
          val _tmpFrontToe: Double
          _tmpFrontToe = _stmt.getDouble(_columnIndexOfFrontToe)
          val _tmpRearToe: Double
          _tmpRearToe = _stmt.getDouble(_columnIndexOfRearToe)
          _tmpSuspensionGeometry =
              SuspensionGeometryEntity(_tmpFrontCamber,_tmpRearCamber,_tmpFrontToe,_tmpRearToe)
          val _tmpSuspension: SuspensionEntity
          val _tmpFrontSusp: Int
          _tmpFrontSusp = _stmt.getLong(_columnIndexOfFrontSusp).toInt()
          val _tmpRearSusp: Int
          _tmpRearSusp = _stmt.getLong(_columnIndexOfRearSusp).toInt()
          val _tmpFrontARB: Int
          _tmpFrontARB = _stmt.getLong(_columnIndexOfFrontARB).toInt()
          val _tmpRearARB: Int
          _tmpRearARB = _stmt.getLong(_columnIndexOfRearARB).toInt()
          val _tmpFrontRideHeight: Int
          _tmpFrontRideHeight = _stmt.getLong(_columnIndexOfFrontRideHeight).toInt()
          val _tmpRearRideHeight: Int
          _tmpRearRideHeight = _stmt.getLong(_columnIndexOfRearRideHeight).toInt()
          _tmpSuspension =
              SuspensionEntity(_tmpFrontSusp,_tmpRearSusp,_tmpFrontARB,_tmpRearARB,_tmpFrontRideHeight,_tmpRearRideHeight)
          val _tmpBrakes: BrakesEntity
          val _tmpPressure: Int
          _tmpPressure = _stmt.getLong(_columnIndexOfPressure).toInt()
          val _tmpBias: Int
          _tmpBias = _stmt.getLong(_columnIndexOfBias).toInt()
          _tmpBrakes = BrakesEntity(_tmpPressure,_tmpBias)
          val _tmpTyres: TyresEntity
          val _tmpFrontPsi: Double
          _tmpFrontPsi = _stmt.getDouble(_columnIndexOfFrontPsi)
          val _tmpRearPsi: Double
          _tmpRearPsi = _stmt.getDouble(_columnIndexOfRearPsi)
          _tmpTyres = TyresEntity(_tmpFrontPsi,_tmpRearPsi)
          _result =
              SetupEntity(_tmpSourceUrl,_tmpSourceName,_tmpSourcePublishedAt,_tmpSourceCommunityRating,_tmpGameVersion,_tmpPatch,_tmpCircuit,_tmpWeatherQuali,_tmpWeatherRace,_tmpStyle,_tmpAero,_tmpTransmission,_tmpSuspensionGeometry,_tmpSuspension,_tmpBrakes,_tmpTyres,_tmpNotes,_tmpScore)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteByCircuit(circuit: String) {
    val _sql: String = "DELETE FROM setups WHERE circuit = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, circuit)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM setups"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
