package jp.second_wave.equipment_management_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import jp.second_wave.equipment_management_app.database.CategoryViewModel
import jp.second_wave.equipment_management_app.database.entitiy.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val button: Button = findViewById<View>(R.id.create_category_button) as Button
        button.setOnClickListener { createCategory() }
    }

    private fun createCategory() {
        val categoryViewModel: CategoryViewModel by viewModels()

        val categoryName = findViewById<View>(R.id.category_name) as EditText

        if (categoryName.text.toString().isEmpty()) {
            categoryName.error = "文字を入力してください"
        } else {
            val category = Category(
                0,
                categoryName.text.toString()
            )

            GlobalScope.launch(Dispatchers.IO){
                categoryViewModel.insert(category)
            }

            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)

        }

    }
}
