package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.adapter.EquipmentListAdapter
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.entitiy.User
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

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
