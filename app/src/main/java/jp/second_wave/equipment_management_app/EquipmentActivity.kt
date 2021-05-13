package jp.second_wave.equipment_management_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EquipmentActivity : AppCompatActivity() {

    private var categoryId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        categoryId = intent.getIntExtra("categoryId", 0)

        val managementNumber = findViewById<View>(R.id.management_number) as EditText
        managementNumber.setText("001") // from DB
        managementNumber.isFocusable = false

        val button: Button = findViewById<View>(R.id.equipment_create_button) as Button
        button.setOnClickListener { createEquipment() }
    }

    private fun setMakerSpinner() {
        // Spinnerの取得
        val spinner = findViewById<Spinner>(R.id.maker_spinner)

        val spinnerItems = ArrayList<String>()

        // Adapterの生成
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)

        // 選択肢の各項目のレイアウト
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // AdapterをSpinnerのAdapterとして設定
        spinner.adapter = adapter
    }

    private fun createEquipment() {
        val equipmentViewModel: EquipmentViewModel by viewModels()
        val managementNumber = findViewById<View>(R.id.management_number) as EditText
        val makerId = findViewById<View>(R.id.maker_id) as EditText
        val modelName = findViewById<View>(R.id.model_name) as EditText
        val equipmentType = findViewById<View>(R.id.equipment_type) as EditText
        val userId = findViewById<View>(R.id.user_id) as EditText
        val usage = findViewById<View>(R.id.usage) as EditText
        val note = findViewById<View>(R.id.note) as EditText
        val purchaseDate = findViewById<View>(R.id.purchase_date) as EditText

        if (managementNumber.text.toString().isEmpty()) {
            managementNumber.error = "文字を入力してください"
        } else {
            val _equipment = Equipment(
                0,
                Integer.parseInt(managementNumber.text.toString()),
                categoryId,
                Integer.parseInt(makerId.text.toString()),
                modelName.text.toString(),
                equipmentType.text.toString(),
                Integer.parseInt(userId.text.toString()),
                usage.text.toString(),
                note.text.toString(),
                null
            )

            val equipment = Equipment(
                0,
                1,
                categoryId,
                1,
                "モデル名",
                "種別",
                1,
                "用途",
                "メモ",
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
