package jp.second_wave.equipment_management_app.database.dao

import androidx.room.*
import jp.second_wave.equipment_management_app.database.entitiy.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    fun findById(id: Int): Category

    @Insert
    fun insert(category: Category)

    @Update
    fun update(category: Category)

    @Delete
    fun delete(category: Category)
}
