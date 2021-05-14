package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.database.view_model.UserViewModel
import jp.second_wave.equipment_management_app.database.entitiy.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        setUserList()

        val button: Button = findViewById<View>(R.id.to_create_user_page) as Button
        button.setOnClickListener { setToCreateUserPage() }
    }

    private fun setUserList() {
        val userViewModel: UserViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main){
            val users = userViewModel.getAll()
            val data = ArrayList<String>()
            users.forEach{
                data.add(it.firstName)
            }
            val adapter = ArrayAdapter(this@UserActivity, android.R.layout.simple_list_item_1, data)
            val listView: ListView = findViewById(R.id.user_list)
            listView.adapter = adapter
        }
    }

    private fun setToCreateUserPage() {
        setContentView(R.layout.activity_user)

        val button: Button = findViewById<View>(R.id.create_user_button) as Button
        button.setOnClickListener { createUser() }
    }

    private fun createUser() {
        val userViewModel: UserViewModel by viewModels()

        val lastName = findViewById<View>(R.id.last_name) as EditText
        val firstName = findViewById<View>(R.id.first_name) as EditText
        val email = findViewById<View>(R.id.email) as EditText

        if (lastName.text.toString().isEmpty()) {
            lastName.error = "文字を入力してください"
        } else {
            val user = User(
                0,
                lastName.text.toString(),
                firstName.text.toString(),
                email.text.toString()
            )

            GlobalScope.launch(Dispatchers.IO){
                userViewModel.insert(user)
            }

            setUserList()
            setContentView(R.layout.activity_users)
        }
    }

    // メニュー表示
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // メニュークリックアクション
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_equipment_list -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_maker_list -> {
                val intent = Intent(this, MakerActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_new_equipment -> {
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_user_list -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
