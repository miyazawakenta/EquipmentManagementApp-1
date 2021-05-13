package jp.second_wave.equipment_management_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class EquipmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        val textView: TextView = findViewById(R.id.management_number_label)

        val item = intent.getIntExtra("id", 0)
        textView.text = item.toString()

    }
}
