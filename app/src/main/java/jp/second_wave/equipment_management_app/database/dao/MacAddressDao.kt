package jp.second_wave.equipment_management_app.database.dao

import jp.second_wave.equipment_management_app.database.entitiy.MacAddress
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MacAddressDao {
    @Query("SELECT * FROM mac_addresses")
    fun getAll(): List<MacAddress>

    @Query("SELECT * FROM mac_addresses where id = :id LIMIT 1")
    fun findById(id: Int) : MacAddress

    @Insert
    fun insert(macAddress: MacAddress)

    @Delete
    fun delete(macAddress: MacAddress)
}
