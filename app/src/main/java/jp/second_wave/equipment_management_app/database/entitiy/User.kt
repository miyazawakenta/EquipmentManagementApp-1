package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "first_name") var firstName: String?,
    var email: String?
)
