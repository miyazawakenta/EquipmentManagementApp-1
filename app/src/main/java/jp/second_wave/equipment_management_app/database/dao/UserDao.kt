package jp.second_wave.equipment_management_app.database.dao

import androidx.room.*
import jp.second_wave.equipment_management_app.database.entitiy.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun findById(id: Int): User

    @Insert
    fun insert(user : User)

//    @Insert
//    fun insertAll(vararg users: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}
