package jp.second_wave.equipment_management_app.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.second_wave.equipment_management_app.database.AppDatabase
import jp.second_wave.equipment_management_app.database.dao.MacAddressDao
import jp.second_wave.equipment_management_app.database.entitiy.MacAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MacAddressViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: MacAddressDao

    init {
        val db = AppDatabase.buildDatabase(application)
        dao = db.macAddressDao()
    }

    suspend fun findById(id: Int) : MacAddress {
        return withContext(Dispatchers.IO) {
            dao.findById(id)
        }
    }

    fun insert(v: MacAddress) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(v)
        }
    }

    fun delete(v : MacAddress) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(v)
        }
    }
}
