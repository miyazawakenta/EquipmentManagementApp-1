package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.adapter.EquipmentListAdapter
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.view_model.CategoryViewModel
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEquipmentList()

        val button: Button = findViewById<View>(R.id.create_equipment_button) as Button
        button.setOnClickListener { startCreateEquipment() }

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener { searchEquipment() }

        setSearchSpinner()
    }

    private fun searchEquipment() {

    }

    private fun setSearchSpinner() {
        val categoryViewModel: CategoryViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val makers = categoryViewModel.getAll()
            val makerNames = makers.map {
                it.CategoryName
            }
            val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, makerNames)
            val spinner = findViewById< Spinner>(R.id.search_category_spinner)
            spinner.adapter = adapter
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

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
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
}
