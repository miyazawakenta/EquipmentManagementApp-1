package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "equipment_relationships",
    foreignKeys = [
        ForeignKey(
            entity = Equipment::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parent_equipment_id")
        ),
        ForeignKey(
            entity = Equipment::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("child_equipment_id")
        )
    ]
)
data class EquipmentRelationShip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "parent_equipment_id") val parentEquipmentId: Int,
    @ColumnInfo(name = "child_equipment_id") val childEquipmentId: Int
)

