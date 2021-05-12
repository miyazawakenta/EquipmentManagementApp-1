package jp.second_wave.equipment_management_app.database.dao

import androidx.room.*
import jp.second_wave.equipment_management_app.database.entitiy.Equipment

@Dao
interface EquipmentDao {
    @Query("select * from equipments")
    fun getAll(): List<Equipment>

    @Query("SELECT * FROM equipments WHERE id = :id LIMIT 1")
    fun findById(id: Int): Equipment

    @Insert
    fun insert(equipment : Equipment)

    @Update
    fun update(equipment: Equipment)

    @Delete
    fun delete(equipment: Equipment)
}
