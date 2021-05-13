package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.database.CategoryListAdapter
import jp.second_wave.equipment_management_app.database.CategoryViewModel
import jp.second_wave.equipment_management_app.database.entitiy.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CategoriesActivity : AppCompatActivity() {

//    companion object {
//        const val EXTRA_DATA = "jp.second_wave.equipment_management_app.DATA"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        val categoryViewModel: CategoryViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main){
            val categories = categoryViewModel.getAll()
            val data = ArrayList<String>()
            categories.forEach { data.add(it.CategoryName) }

            val adapter = CategoryListAdapter(this@CategoriesActivity, categories)
            val listView: ListView = findViewById(R.id.category_list)
            listView.adapter = adapter
            listView.onItemClickListener = ListItemClickListener()
        }

        val button: Button = findViewById<View>(R.id.start_category) as Button
        button.setOnClickListener { startCreateCategory() }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as Category
            startCreateEquipment(item)
        }
    }
    private fun startCreateEquipment(item: Category) {
        val intent = Intent(this, EquipmentActivity::class.java)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }

    private fun startCreateCategory() {
        val intent = Intent(this, CategoryActivity::class.java)
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
