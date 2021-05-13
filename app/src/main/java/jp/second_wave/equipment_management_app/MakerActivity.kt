package jp.second_wave.equipment_management_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MakerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maker)

        val button: Button = findViewById<View>(R.id.create_maker_button) as Button
        button.setOnClickListener { createMaker() }
    }

    private fun createMaker() {
        val makerViewModel: MakerViewModel by viewModels()
        val makerName = findViewById<View>(R.id.maker_name) as EditText

        if (makerName.text.toString().isEmpty()) {
            makerName.error = "文字を入力してください"
        } else {
            val maker = Maker(
                0,
                makerName.text.toString()
            )
            GlobalScope.launch(Dispatchers.IO) {
                makerViewModel.insert(maker)
            }

            val intent = Intent(this, MakersActivity::class.java)
            startActivity(intent)
        }
    }
}
