package jp.second_wave.equipment_management_app.database.dao

import androidx.room.*
import jp.second_wave.equipment_management_app.database.entitiy.Maker

@Dao
interface MakerDao {
    @Query("SELECT * FROM makers")
    fun getAll(): List<Maker>

    @Query("SELECT * FROM makers WHERE id = :id LIMIT 1")
    fun findById(id: Int): Maker

    @Insert
    fun insert(maker: Maker)

    @Update
    fun update(maker: Maker)

    @Delete
    fun delete(maker: Maker)
}
