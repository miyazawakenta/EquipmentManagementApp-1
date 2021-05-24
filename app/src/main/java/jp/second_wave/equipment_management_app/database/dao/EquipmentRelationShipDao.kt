package jp.second_wave.equipment_management_app.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import jp.second_wave.equipment_management_app.database.entitiy.EquipmentRelationShip

@Dao
interface EquipmentRelationShipDao {
    @Query("select * from equipment_relationships")
    fun getAll(): List<EquipmentRelationShip>

    @Query("SELECT * FROM equipment_relationships WHERE id = :id LIMIT 1")
    fun findById(id: Int): EquipmentRelationShip

    @Query("SELECT * FROM equipment_relationships WHERE parent_equipment_id = :id LIMIT 1")
    fun findByParentEquipmentId(id: Int): List<EquipmentRelationShip>

    @Insert
    fun insert(equipment : EquipmentRelationShip)

    @Delete
    fun delete(equipment: EquipmentRelationShip)

    @Query("DELETE FROM equipment_relationships WHERE parent_equipment_id = :id")
    fun deleteAll(id: Int)
}
