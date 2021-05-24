package jp.second_wave.equipment_management_app.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.second_wave.equipment_management_app.database.AppDatabase
import jp.second_wave.equipment_management_app.database.dao.EquipmentRelationShipDao
import jp.second_wave.equipment_management_app.database.entitiy.EquipmentRelationShip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EquipmentRelationShipViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: EquipmentRelationShipDao

    init {
        val db = AppDatabase.buildDatabase(application)
        dao = db.equipmentRelationShipDao()
    }

    fun insert(v: EquipmentRelationShip) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(v)
        }
    }

    fun delete(v: EquipmentRelationShip) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(v)
        }
    }

    fun deleteAll(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll(id)
        }
    }

    fun findByParentEquipmentId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.findByParentEquipmentId(id)
        }
    }
}
