package jp.second_wave.equipment_management_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import jp.second_wave.equipment_management_app.database.view_model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EquipmentActivity : AppCompatActivity() {

    private var categoryId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        categoryId = intent.getIntExtra("categoryId", 0)

        setManagementNumber()
        setMakerSpinner()
        setPurchaseDateCalender()
        setUserSpinner()

        val button: Button = findViewById<View>(R.id.equipment_create_button) as Button
        button.setOnClickListener { createEquipment() }
    }

    private fun setUserSpinner() {
        val spinner = findViewById<Spinner>(R.id.user_spinner)
        val userViewModel: UserViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val users = userViewModel.getAll()
            val userIds = ArrayList<String>()
            users.forEach {
                val replaceId = String.format("%3s", it.id.toString()).replace(" ", "0")
                userIds.add("${replaceId}: ${it.firstName}")
            }

            val adapter = ArrayAdapter(this@EquipmentActivity, android.R.layout.simple_spinner_item , userIds)
            spinner.adapter = adapter
        }
    }

    private fun setPurchaseDateCalender() {
        val purchaseDate = findViewById<View>(R.id.purchase_date) as EditText
    }

    private fun setManagementNumber() {
        // TODO: 更新時の処理
        val managementNumber = findViewById<View>(R.id.management_number) as TextView
        val equipmentViewModel: EquipmentViewModel by viewModels()

        GlobalScope.launch(Dispatchers.IO) {
            val maxManagementNumber = equipmentViewModel.getMaxManagementNumber(categoryId) + 1
            val managementNumberString = String.format("%3s", maxManagementNumber.toString()).replace(" ", "0")
            managementNumber.text = managementNumberString
        }
        managementNumber.isFocusable = false
    }

    private fun setMakerSpinner() {
        // Spinnerの取得
        val spinner = findViewById<Spinner>(R.id.maker_spinner)
        val makerViewModel: MakerViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val makers = makerViewModel.getAll()
            val makerIds = ArrayList<String>()
            makers.forEach {
                val replaceId = String.format("%3s", it.id.toString()).replace(" ", "0")
                makerIds.add("${replaceId}: ${it.makerName}")
            }

            val adapter = ArrayAdapter(this@EquipmentActivity, android.R.layout.simple_spinner_item , makerIds)
            spinner.adapter = adapter
        }
    }

    private fun createEquipment() {
        val equipmentViewModel: EquipmentViewModel by viewModels()
        val managementNumber = findViewById<View>(R.id.management_number) as TextView
        val makerSpinner = findViewById<View>(R.id.maker_spinner) as Spinner
        val modelName = findViewById<View>(R.id.model_name) as EditText
        val equipmentType = findViewById<View>(R.id.equipment_type) as EditText
        val userSpinner = findViewById<View>(R.id.user_spinner) as Spinner
        val usage = findViewById<View>(R.id.usage) as EditText
        val note = findViewById<View>(R.id.note) as EditText
        val purchaseDate = findViewById<View>(R.id.purchase_date) as EditText

        if (modelName.text.toString().isEmpty()) {
            modelName.error = "文字を入力してください"
        } else {
            val userIdString = userSpinner.selectedItem.toString().split(":")[0]
            val makerIdString = makerSpinner.selectedItem.toString().split(":")[0]
            val equipment = Equipment(
                0,
                Integer.parseInt(managementNumber.text.toString()),
                categoryId,
                Integer.parseInt(makerIdString),
                modelName.text.toString(),
                equipmentType.text.toString(),
                Integer.parseInt(userIdString),
                usage.text.toString(),
                note.text.toString(),
                java.util.Date()
            )
            GlobalScope.launch(Dispatchers.IO) {
                equipmentViewModel.insert(equipment)
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
