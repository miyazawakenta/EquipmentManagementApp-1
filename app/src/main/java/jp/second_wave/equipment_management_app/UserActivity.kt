package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.database.UserViewModel
import jp.second_wave.equipment_management_app.database.entitiy.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            GlobalScope.launch(Dispatchers.Main){
                userViewModel.insert(user)
            }

            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }
    }
}
