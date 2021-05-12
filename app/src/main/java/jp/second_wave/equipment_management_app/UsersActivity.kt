package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
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
        val varTextView = findViewById<View>(R.id.textView1) as TextView
        when (item.itemId) {
            R.id.action_equipment_list -> {
                varTextView.setText(R.string.action_equipment_list)
                return true
            }
            R.id.action_maker_list -> {
                varTextView.setText(R.string.action_maker_list)
                return true
            }
            R.id.action_new -> {
                varTextView.setText(R.string.action_new)
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
