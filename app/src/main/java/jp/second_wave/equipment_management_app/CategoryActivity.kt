package jp.second_wave.equipment_management_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.viewModels
import jp.second_wave.equipment_management_app.database.adapter.CategoryListAdapter
import jp.second_wave.equipment_management_app.database.view_model.CategoryViewModel
import jp.second_wave.equipment_management_app.database.entitiy.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        setCategoryList()

        val button: Button = findViewById<View>(R.id.to_create_category_page) as Button
        button.setOnClickListener { setToCreateCategoryPage() }

    }

    // 一覧データをセットする
    private fun setCategoryList() {
        val categoryViewModel: CategoryViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val categories = categoryViewModel.getAll()
            val data = ArrayList<String>()
            categories.forEach { data.add(it.CategoryName) }

            val adapter = CategoryListAdapter(this@CategoryActivity, categories)
            val listView: ListView = findViewById(R.id.category_list)
            listView.adapter = adapter
            listView.onItemClickListener = ListItemClickListener()
        }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as Category
            startCreateEquipment(item)
        }
    }

    // Listの項目タップで機材登録画面へ遷移する
    private fun startCreateEquipment(item: Category) {
        val intent = Intent(this, EquipmentActivity::class.java)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }

    // 新規登録画面へ切り替える
    private fun setToCreateCategoryPage() {
        setContentView(R.layout.activity_category)
        val button: Button = findViewById<View>(R.id.create_category_button) as Button
        button.setOnClickListener { createCategory() }
    }

    // 登録アクション
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

            setCategoryList()
            setContentView(R.layout.activity_categories)
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
