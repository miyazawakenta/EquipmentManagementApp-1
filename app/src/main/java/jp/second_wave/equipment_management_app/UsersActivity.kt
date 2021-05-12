package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.database.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        val userViewModel: UserViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main){
            val users = userViewModel.getAll()
            val data = ArrayList<String>()
            users.forEach{
                data.add(it.firstName)
            }
            val adapter = ArrayAdapter(this@UsersActivity, android.R.layout.simple_list_item_1, data)
            val listView: ListView = findViewById(R.id.user_list)
            listView.adapter = adapter
        }

        val button: Button = findViewById<View>(R.id.start_user) as Button
        button.setOnClickListener { startCreateUser() }
    }

    private fun startCreateUser() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_equipment_list -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_maker_list -> {
                val intent = Intent(this, MakersActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_new -> {
                val intent = Intent(this, CategoriesActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_user_list -> {
                val intent = Intent(this, UsersActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
