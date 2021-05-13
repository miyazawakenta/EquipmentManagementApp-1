package jp.second_wave.equipment_management_app.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.second_wave.equipment_management_app.database.AppDatabase
import jp.second_wave.equipment_management_app.database.dao.MakerDao
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MakerViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: MakerDao

    init {
        val db = AppDatabase.buildDatabase(application)
        dao = db.makerDao()
    }

    suspend fun getAll() : List<Maker> {
        return withContext(Dispatchers.IO) { dao.getAll() }
    }

    fun insert(maker: Maker) {
        viewModelScope.launch(Dispatchers.IO) { dao.insert(maker) }
    }

    fun update(maker: Maker) {
        viewModelScope.launch(Dispatchers.IO) { dao.update(maker) }
    }
}
