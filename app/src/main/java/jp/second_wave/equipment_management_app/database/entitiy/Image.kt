package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = Equipment::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("equipment_id")
        )
    ]
)
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "equipment_id") val equipmentId: Int,
    @ColumnInfo(name = "file_name") val fileName: String
)
