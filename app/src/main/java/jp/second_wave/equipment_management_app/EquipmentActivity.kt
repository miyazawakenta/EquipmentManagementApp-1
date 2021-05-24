package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.database.entitiy.*
import jp.second_wave.equipment_management_app.database.view_model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class EquipmentActivity : AppCompatActivity(), SearchDialogFragment.ParentFragmentListener {

    private var categoryId = 0
    private var equipmentId = 0

    private lateinit var users :List<User>
    private lateinit var makers :List<Maker>

    private val equipmentViewModel: EquipmentViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val makerViewModel: MakerViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val equipmentRelationShipViewModel: EquipmentRelationShipViewModel by viewModels()
    private val macAddressViewModel: MacAddressViewModel by viewModels()

    private lateinit var macAddressEditText: EditText

    private var selectedEquipmentRelations = ArrayList<Int>()
    private lateinit var equipmentRelationShipDialogFragment : SearchDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        categoryId = intent.getIntExtra("categoryId", 0)
        equipmentId = intent.getIntExtra("equipmentId", 0)

        macAddressEditText = findViewById(R.id.mac_address)
        macAddressEditText.addTextChangedListener(macAddressTextWatcher)

        if ( equipmentId == 0) {
            // 新規登録
            setManagementNumber()
            setMakerSpinner()
            setUserSpinner()
            setCategory(categoryId)

            val button: Button = findViewById<View>(R.id.equipment_create_button) as Button
            button.setOnClickListener { createEquipment() }
        } else {
            // 更新
            title = getString(R.string.equipment_update)

            GlobalScope.launch(Dispatchers.Main) {
                val equipment = equipmentViewModel.findById(equipmentId)
                val managementNumber = findViewById<View>(R.id.management_number) as TextView
                val managementNumberString = String.format("%3s", equipment.managementNumber.toString()).replace(" ", "0")

                setCategory(equipment.categoryId)

                setMakerSpinner(equipment.makerId)
                setUserSpinner(equipment.userId)
                managementNumber.text = managementNumberString

                val modelName = findViewById<View>(R.id.model_name) as EditText
                val equipmentType = findViewById<View>(R.id.equipment_type) as EditText
                val usage = findViewById<View>(R.id.usage) as EditText
                val note = findViewById<View>(R.id.note) as EditText
                val purchaseDate = findViewById<EditText>(R.id.purchase_date)

                modelName.setText(equipment.modelName)
                equipmentType.setText(equipment.equipmentType)
                usage.setText(equipment.usage)
                note.setText(equipment.note)

                val df = SimpleDateFormat("yyyy/MM/dd")
                val date = equipment.purchase_date
                if (date != null) {
                    purchaseDate.setText(df.format(date))
                }

                val button: Button = findViewById<View>(R.id.equipment_create_button) as Button
                button.setOnClickListener { updateEquipment(equipment) }
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            val equipments = mutableMapOf<Int, String>()
            equipmentViewModel.getAll().filter { it.id != equipmentId }.forEach { equipment ->
                equipments[equipment.id] = equipment.modelName
            }
            equipmentRelationShipDialogFragment = SearchDialogFragment(equipments)
            val equipmentRelationShipButton = findViewById<Button>(R.id.select_relation_equipment_dialog)
            equipmentRelationShipButton.setOnClickListener {
                equipmentRelationShipDialogFragment.show(supportFragmentManager, "simple")
            }
        }
    }

    private fun setManagementNumber() {
        val managementNumber = findViewById<View>(R.id.management_number) as TextView

        GlobalScope.launch(Dispatchers.IO) {
            val maxManagementNumber = equipmentViewModel.getMaxManagementNumber(categoryId) + 1
            val managementNumberString = String.format("%3s", maxManagementNumber.toString()).replace(" ", "0")
            managementNumber.text = managementNumberString
        }
        managementNumber.isFocusable = false
    }

    private fun setCategory(id : Int) {
        val categoryNameLabel = findViewById<TextView>(R.id.category_name_label)
        GlobalScope.launch(Dispatchers.Main) {
            val category = categoryViewModel.findById(id)
            categoryNameLabel.text = category.CategoryName
        }
    }

    private fun setUserSpinner(userId: Int? = 0) {
        val spinner = findViewById<Spinner>(R.id.user_spinner)

        GlobalScope.launch(Dispatchers.Main) {
            users = userViewModel.getAll()
            val userIds = ArrayList<String>()
            users.forEach {
                val replaceId = String.format("%3s", it.id.toString()).replace(" ", "0")
                userIds.add("${replaceId}: ${it.lastName}")
            }

            val adapter = ArrayAdapter(this@EquipmentActivity, android.R.layout.simple_spinner_item , userIds)
            spinner.adapter = adapter
            if (userId != 0) {
                spinner.setSelection(users.indexOf(users.find { it.id == userId }))
            }
        }
    }

    private fun setMakerSpinner(makerId : Int = 0) {
        // Spinnerの取得
        val spinner = findViewById<Spinner>(R.id.maker_spinner)

        GlobalScope.launch(Dispatchers.Main) {
            makers = makerViewModel.getAll()
            val makerIds = ArrayList<String>()
            makers.forEach {
                val replaceId = String.format("%3s", it.id.toString()).replace(" ", "0")
                makerIds.add("${replaceId}: ${it.makerName}")
            }

            val adapter = ArrayAdapter(this@EquipmentActivity, android.R.layout.simple_spinner_item , makerIds)
            spinner.adapter = adapter
            if (makerId != 0) {
                spinner.setSelection(makers.indexOf(makers.find { it.id == makerId }))
            }
        }
    }

    private fun updateEquipment(equipment: Equipment) {

        val makerSpinner = findViewById<View>(R.id.maker_spinner) as Spinner
        val modelName = findViewById<View>(R.id.model_name) as EditText
        val equipmentType = findViewById<View>(R.id.equipment_type) as EditText
        val userSpinner = findViewById<View>(R.id.user_spinner) as Spinner
        val usage = findViewById<View>(R.id.usage) as EditText
        val note = findViewById<View>(R.id.note) as EditText
        val vPurchaseDate = findViewById<View>(R.id.purchase_date) as EditText

        val sdFormat = try {
            SimpleDateFormat("yyyy/MM/dd")
        } catch (e: IllegalArgumentException) {
            null
        }

        val purchaseDate = sdFormat?.let {
            try {
                it.parse(vPurchaseDate.text.toString())
            } catch (e: ParseException) {
                java.util.Date()
            }
        }

        if (modelName.text.toString().isEmpty()) {
            modelName.error = "モデル名をいれてください"
        } else {
            val maker = makers[makerSpinner.selectedItemPosition]
            val user = users[userSpinner.selectedItemPosition]

            equipment.modelName = modelName.text.toString()
            equipment.equipmentType = equipmentType.text.toString()
            equipment.usage = usage.text.toString()
            equipment.note = note.text.toString()
            equipment.userId = user.id
            equipment.makerId = maker.id
            equipment.purchase_date = purchaseDate

            GlobalScope.launch(Dispatchers.IO) {
                equipmentViewModel.update(equipment)
            }
            val toast: Toast = Toast.makeText(this, "更新しました。", Toast.LENGTH_SHORT)
            toast.show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createEquipment() {

        val managementNumberTextView = findViewById<View>(R.id.management_number) as TextView
        val makerSpinner = findViewById<View>(R.id.maker_spinner) as Spinner
        val modelNameEditText = findViewById<View>(R.id.model_name) as EditText
        val equipmentTypeEditText = findViewById<View>(R.id.equipment_type) as EditText
        val userSpinner = findViewById<View>(R.id.user_spinner) as Spinner
        val usageEditText = findViewById<View>(R.id.usage) as EditText
        val noteEditText = findViewById<View>(R.id.note) as EditText
        val purchaseDateEditText = findViewById<View>(R.id.purchase_date) as EditText
        val hostNameEditText = findViewById<EditText>(R.id.host_name)
        val osEditText = findViewById<EditText>(R.id.os)
        val macAddressEditText = findViewById<EditText>(R.id.mac_address)

        val sdFormat = try {
            SimpleDateFormat("yyyy/MM/dd")
        } catch (e: IllegalArgumentException) {
            null
        }

        val purchaseDate = sdFormat?.let {
            try {
                it.parse(purchaseDateEditText.text.toString())
            } catch (e: ParseException) {
                java.util.Date()
            }
        }
        if (modelNameEditText.text.toString().isEmpty()) {
            modelNameEditText.error = "モデル名をいれてください"
        } else {
            val maker = makers[makerSpinner.selectedItemPosition]
            val user = users[userSpinner.selectedItemPosition]
            val equipment = Equipment(
                0,
                Integer.parseInt(managementNumberTextView.text.toString()),
                categoryId,
                maker.id,
                modelNameEditText.text.toString(),
                equipmentTypeEditText.text.toString(),
                user.id,
                usageEditText.text.toString(),
                noteEditText.text.toString(),
                hostNameEditText.text.toString(),
                purchaseDate,
                osEditText.text.toString()
            )
            GlobalScope.launch(Dispatchers.IO) {
                val id = equipmentViewModel.insert(equipment)
                equipment.id = id.toInt()
                // TODO: IDが取得できずエラーになる?  java.lang.Long cannot be cast to java.lang.Integer
            }
            val equipmentRelationShips = selectedEquipmentRelations.map {
                EquipmentRelationShip(
                    0,
                    equipment.id,
                    it
                )
            }
            GlobalScope.launch(Dispatchers.IO) {
                equipmentRelationShips.forEach {
                    equipmentRelationShipViewModel.insert(it)
                }
            }
            if (macAddressEditText.text.toString().isNotEmpty()) {
                val macAddress = MacAddress(
                    0,
                    equipment.id,
                    macAddressEditText.text.toString()
                )
                GlobalScope.launch(Dispatchers.IO) {
                    macAddressViewModel.insert(macAddress)
                }
            }

            val toast: Toast = Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT)
            toast.show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private val macAddressTextWatcher = object : TextWatcher {
        private val pattern1 = Pattern.compile("[0-9a-fA-F]{2}")
        private val pattern2 = Pattern.compile("[0-9a-fA-F]{2}-[0-9a-fA-F]{2}")
        private val pattern3 = Pattern.compile("[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}")
        private val pattern4 = Pattern.compile("[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}")
        private val pattern5 = Pattern.compile("[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}")
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(before == 0) {
                if (pattern1.matcher(s).matches()) {
                    macAddressEditText.setText(resources.getString(R.string.with_hyphen, s))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern2.matcher(s).matches()) {
                    macAddressEditText.setText(resources.getString(R.string.with_hyphen, s))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern3.matcher(s).matches()) {
                    macAddressEditText.setText(resources.getString(R.string.with_hyphen, s))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern4.matcher(s).matches()) {
                    macAddressEditText.setText(resources.getString(R.string.with_hyphen, s))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern5.matcher(s).matches()) {
                    macAddressEditText.setText(resources.getString(R.string.with_hyphen, s))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
            } else {
                if (pattern1.matcher(s).matches()) {
                    macAddressEditText.setText(s.subSequence(0, s.length-1))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern2.matcher(s).matches()) {
                    macAddressEditText.setText(s.subSequence(0, s.length-1))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern3.matcher(s).matches()) {
                    macAddressEditText.setText(s.subSequence(0, s.length-1))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern4.matcher(s).matches()) {
                    macAddressEditText.setText(s.subSequence(0, s.length-1))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
                if (pattern5.matcher(s).matches()) {
                    macAddressEditText.setText(s.subSequence(0, s.length-1))
                    macAddressEditText.setSelection(macAddressEditText.editableText.toString().length)
                }
            }
        }
    }

    override fun onClickButton() {
        selectedEquipmentRelations = equipmentRelationShipDialogFragment.result
        val selectedRelationShips = findViewById<TextView>(R.id.selected_relationships)
        selectedRelationShips.text = equipmentRelationShipDialogFragment.resultLabel.joinToString()
    }
}