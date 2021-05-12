package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "mac_addresses",
    foreignKeys = [
        ForeignKey(
            entity = Equipment::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("equipment_id")
        )
    ]
)
data class MacAddress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "equipment_id") val equipmentId: Int,
    @ColumnInfo(name = "mac_address") val macAddress: String
)
