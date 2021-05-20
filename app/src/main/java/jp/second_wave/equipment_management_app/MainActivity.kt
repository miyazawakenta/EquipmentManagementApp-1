package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.adapter.EquipmentListAdapter
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.view_model.CategoryViewModel
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import jp.second_wave.equipment_management_app.database.view_model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), SearchDialogFragment.ParentFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEquipmentList()
        setSearchOptions()

        val createEquipmentButton = findViewById<Button>(R.id.create_equipment_button)
        createEquipmentButton.setOnClickListener { startCreateEquipment() }

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener { searchEquipment() }
    }

    private var searchCategoryValues = ArrayList<Int>()
    private var searchUserValues = ArrayList<Int>()
    private lateinit var categorySearchDialogFragment: SearchDialogFragment
    private lateinit var userSearchDialogFragment: SearchDialogFragment

    private fun searchEquipment() {
        val vSearchModel = findViewById<EditText>(R.id.search_model_name)

        val equipmentViewModel: EquipmentViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val equipments = equipmentViewModel.getEquipmentAndRelationAll(
                searchCategoryValues,
                searchUserValues,
                vSearchModel.text.toString()
            )
            val adapter = EquipmentListAdapter(applicationContext, equipments)
            val listView: ListView = findViewById(R.id.equipment_list)
            listView.adapter = adapter
            listView.onItemClickListener = ListItemClickListener()
        }
    }

    private fun setSearchOptions() {
        val categoryViewModel: CategoryViewModel by viewModels()
        val userViewModel: UserViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val categories = categoryViewModel.getAll()
            categorySearchDialogFragment = SearchDialogFragment(categories.map { it.id to it.CategoryName }.toMap())

            val categoryDialogButton = findViewById<Button>(R.id.category_dialog_button)
            categoryDialogButton.setOnClickListener {
                categorySearchDialogFragment.show(supportFragmentManager, "simple")
            }

            val users = userViewModel.getAll()
            userSearchDialogFragment = SearchDialogFragment(users.map { it.id to it.lastName }.toMap())

            val userDialogButton = findViewById<Button>(R.id.user_dialog_button)
            userDialogButton.setOnClickListener {
                userSearchDialogFragment.show(supportFragmentManager, "simple")
            }
        }
    }

    private fun setEquipmentList() {
        val equipmentViewModel: EquipmentViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val equipments = equipmentViewModel.getEquipmentAndRelationAll()
            val adapter = EquipmentListAdapter(applicationContext, equipments)
            val listView: ListView = findViewById(R.id.equipment_list)
            listView.adapter = adapter
            listView.onItemClickListener = ListItemClickListener()
        }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val equipment = parent.getItemAtPosition(position) as Equipment

            startUpdateEquipment(equipment)
        }
    }

    private fun startUpdateEquipment(equipment: Equipment) {
        val intent = Intent(this, EquipmentActivity::class.java)
        intent.putExtra("equipmentId", equipment.id)
        startActivity(intent)
    }

    private fun startCreateEquipment() {
        val intent = Intent(this, CategoryActivity::class.java)
        startActivity(intent)
    }

    override fun onClickButton() {
        searchCategoryValues = categorySearchDialogFragment.result
        searchUserValues = userSearchDialogFragment.result
    }
}
