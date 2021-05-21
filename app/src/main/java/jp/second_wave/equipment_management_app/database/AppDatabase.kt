package jp.second_wave.equipment_management_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.second_wave.equipment_management_app.database.dao.CategoryDao
import jp.second_wave.equipment_management_app.database.dao.EquipmentDao
import jp.second_wave.equipment_management_app.database.dao.MakerDao
import jp.second_wave.equipment_management_app.database.dao.UserDao
import jp.second_wave.equipment_management_app.database.entitiy.*


@Database(entities = [
  User::class,
  Equipment::class,
  Category::class,
  MacAddress::class,
  Maker::class,
  EquipmentRelationShip::class,
  Image::class
                     ], version = 2)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
  abstract fun equipmentDao(): EquipmentDao
  abstract fun makerDao(): MakerDao
  abstract fun categoryDao(): CategoryDao

  companion object {
    fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
        context,
        AppDatabase::class.java, "equipment_management.db"
      )
        .addMigrations(MIGRATION_1_2)
        .build()
    }
    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'equipments' ADD COLUMN 'os' TEXT;")
      }
    }
  }
}
