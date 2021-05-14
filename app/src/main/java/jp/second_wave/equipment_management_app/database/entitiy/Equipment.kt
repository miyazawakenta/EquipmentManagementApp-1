package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.*

@Entity(tableName = "equipments",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id")
        ),
        ForeignKey(
            entity = Maker::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("maker_id")
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id")
        )
    ]
)
data class Equipment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "management_number") val managementNumber: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "maker_id") val makerId: Int,
    @ColumnInfo(name = "model_name") var modelName: String,
    @ColumnInfo(name = "equipment_type") var equipmentType: String?,
    @ColumnInfo(name = "user_id") val userId: Int?,
    var usage: String?,
    var note: String?,
    val purchase_date: java.util.Date?
)
