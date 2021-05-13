package jp.second_wave.equipment_management_app.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.second_wave.equipment_management_app.database.AppDatabase
import jp.second_wave.equipment_management_app.database.dao.CategoryDao
import jp.second_wave.equipment_management_app.database.entitiy.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: CategoryDao

    init {
        val db = AppDatabase.buildDatabase(application)
        dao = db.categoryDao()
    }
    suspend fun getAll() : List<Category> {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }
    fun insert(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(category)
        }
    }

    fun update(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(category)
        }
    }
}
