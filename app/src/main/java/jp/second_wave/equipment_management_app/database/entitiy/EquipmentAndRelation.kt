package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.Embedded
import androidx.room.Relation

class EquipmentAndRelation {
    @Embedded
    lateinit var equipment: Equipment

    @Relation(parentColumn = "user_id", entityColumn = "id")
    lateinit var user: User

    @Relation(parentColumn = "maker_id", entityColumn = "id")
    lateinit var maker: Maker

    @Relation(parentColumn = "category_id", entityColumn = "id")
    lateinit var category: Category

    @Relation(parentColumn = "id", entityColumn = "parent_equipment_id")
    lateinit var equipmentRelationShip: EquipmentRelationShip
}


