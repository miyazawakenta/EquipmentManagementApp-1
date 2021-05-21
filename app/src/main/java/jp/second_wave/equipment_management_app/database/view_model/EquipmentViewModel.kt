package jp.second_wave.equipment_management_app.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.second_wave.equipment_management_app.database.AppDatabase
import jp.second_wave.equipment_management_app.database.dao.EquipmentDao
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.entitiy.EquipmentAndRelation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EquipmentViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: EquipmentDao

    init {
        val db = AppDatabase.buildDatabase(application)
        dao = db.equipmentDao()
    }

    suspend fun getAll() : List<Equipment> {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }

    suspend fun findById(id: Int) : Equipment {
        return withContext(Dispatchers.IO) {
            dao.findById(id)
        }
    }

    suspend fun getMaxManagementNumber(categoryId: Int) :Int {
        return withContext(Dispatchers.IO) {
            dao.getMaxManagementNumber(categoryId)
        }
    }

    suspend fun getEquipmentAndRelationAll(
        categoryIds: List<Int> = arrayListOf(),
        userIds: List<Int> = arrayListOf(),
        makerIds: List<Int> = arrayListOf(),
        managementNumber: String = "",
        modelName: String = ""
    ) :List<EquipmentAndRelation> {

        return withContext(Dispatchers.IO) {
            dao.getEquipmentAndRelationAll().filter {
                val category = if (categoryIds.isEmpty()) true else categoryIds.contains(it.category.id)
                val user = if (userIds.isEmpty()) true else userIds.contains(it.user.id)
                val maker = if (makerIds.isEmpty()) true else makerIds.contains(it.maker.id)
                val model = if (modelName == "") true else Regex(modelName.toLowerCase()).containsMatchIn(it.equipment.modelName.toLowerCase())
                val itManagementNumber = String.format("%2s", it.equipment.categoryId.toString()).replace(" ", "0") + "-" + String.format("%3s", it.equipment.managementNumber.toString()).replace(" ", "0")
                val managementNumber = if(managementNumber == "") true else managementNumber == itManagementNumber
                category && user && maker && model && managementNumber
            }
        }
    }

    suspend fun insert(equipment: Equipment) : Long {
        return withContext(Dispatchers.IO) {
            dao.insert(equipment)
        }
    }

    fun update(equipment: Equipment) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(equipment)
        }
    }


}
