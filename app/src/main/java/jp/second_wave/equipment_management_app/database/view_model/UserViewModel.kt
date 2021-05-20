package jp.second_wave.equipment_management_app.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.second_wave.equipment_management_app.database.AppDatabase
import jp.second_wave.equipment_management_app.database.dao.UserDao
import jp.second_wave.equipment_management_app.database.entitiy.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: UserDao

    init {
        val db = AppDatabase.buildDatabase(application)
        dao = db.userDao()
    }

    suspend fun getAll() : List<User> {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }

    fun insert(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(user)
        }
    }

    fun update(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(user)
        }
    }

    fun delete(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(user)
        }
    }
}
