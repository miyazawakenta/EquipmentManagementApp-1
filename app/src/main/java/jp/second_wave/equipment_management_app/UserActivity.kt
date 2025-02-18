package jp.second_wave.equipment_management_app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.adapter.UserListAdapter
import jp.second_wave.equipment_management_app.database.entitiy.User
import jp.second_wave.equipment_management_app.database.view_model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        setUserList()

        val button = findViewById<ImageButton>(R.id.to_create_user_page)
        button.setOnClickListener { setToCreateUserPage() }
    }

    private fun setUserList() {
        val userViewModel: UserViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main){
            val users = userViewModel.getAll()
            val adapter = UserListAdapter(this@UserActivity, users, supportFragmentManager)
            val listView: ListView = findViewById(R.id.user_list)
            listView.adapter = adapter
            listView.onItemClickListener = ListItemClickListener()
        }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val user = parent.getItemAtPosition(position) as User

            setToUpdateUserPage(user)
        }
    }

    private fun setToUserListPage() {
        setContentView(R.layout.activity_users)
        title = getString(R.string.menu_user_list)

        setUserList()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val button = findViewById<ImageButton>(R.id.to_create_user_page)
        button.setOnClickListener { setToCreateUserPage() }
    }

    private fun setToUpdateUserPage(user: User) {
        setContentView(R.layout.activity_user)
        title = getString(R.string.user_update)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val lastName = findViewById<View>(R.id.last_name) as EditText
        val firstName = findViewById<View>(R.id.first_name) as EditText
        val email = findViewById<View>(R.id.email) as EditText
        lastName.setText(user.lastName)
        firstName.setText(user.firstName)
        email.setText(user.email)

        val button: Button = findViewById<View>(R.id.create_user_button) as Button
        button.setOnClickListener { updateUser(user) }
    }

    private fun setToCreateUserPage() {
        setContentView(R.layout.activity_user)
        title = getString(R.string.user_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val button: Button = findViewById<View>(R.id.create_user_button) as Button
        button.setOnClickListener { createUser() }
    }

    private fun deleteUser(user: User) {
        // TODO: 削除処理
    }

    private fun updateUser(user: User) {
        val userViewModel: UserViewModel by viewModels()

        val lastName = findViewById<View>(R.id.last_name) as EditText
        val firstName = findViewById<View>(R.id.first_name) as EditText
        val email = findViewById<View>(R.id.email) as EditText

        if (lastName.text.toString().isEmpty()) {
            lastName.error = "名前（姓）を入力してください"
        } else {
            user.lastName = lastName.text.toString()
            user.firstName = firstName.text.toString()
            user.email = email.text.toString()
            GlobalScope.launch(Dispatchers.IO){
                userViewModel.update(user)
            }

            val toast: Toast = Toast.makeText(this, "更新しました", Toast.LENGTH_SHORT)
            toast.show()

            setToUserListPage()
        }
    }

    private fun createUser() {
        val userViewModel: UserViewModel by viewModels()

        val lastName = findViewById<View>(R.id.last_name) as EditText
        val firstName = findViewById<View>(R.id.first_name) as EditText
        val email = findViewById<View>(R.id.email) as EditText

        if (lastName.text.toString().isEmpty()) {
            lastName.error = "名前（姓）を入力してください"
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

            val toast: Toast = Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT)
            toast.show()

            setToUserListPage()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        setToUserListPage()
        return false
    }

}
