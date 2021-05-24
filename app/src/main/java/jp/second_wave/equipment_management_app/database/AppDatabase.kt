package jp.second_wave.equipment_management_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.second_wave.equipment_management_app.database.dao.*
import jp.second_wave.equipment_management_app.database.entitiy.*


@Database(entities = [
  User::class,
  Equipment::class,
  Category::class,
  MacAddress::class,
  Maker::class,
  EquipmentRelationShip::class,
  Image::class
                     ], version = 3)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
  abstract fun equipmentDao(): EquipmentDao
  abstract fun makerDao(): MakerDao
  abstract fun categoryDao(): CategoryDao
  abstract fun equipmentRelationShipDao(): EquipmentRelationShipDao
  abstract fun macAddressDao(): MacAddressDao

  companion object {
    fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
        context,
        AppDatabase::class.java, "equipment_management.db"
      )
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build()
    }
    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'equipments' ADD COLUMN 'os' TEXT;")
      }
    }
    private val MIGRATION_2_3: Migration = object : Migration(2,3) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'equipments' ADD COLUMN 'host_name' TEXT;")
      }
    }
  }
}
