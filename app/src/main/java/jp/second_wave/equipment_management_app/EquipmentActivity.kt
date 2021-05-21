package jp.second_wave.equipment_management_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import jp.second_wave.equipment_management_app.database.entitiy.User
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import jp.second_wave.equipment_management_app.database.view_model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat

class EquipmentActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST_CODE = 1
        const val CAMERA_PERMISSION_REQUEST_CODE = 2
    }

    private var categoryId = 0
    private var equipmentId = 0

    private lateinit var users :List<User>
    private lateinit var makers :List<Maker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        categoryId = intent.getIntExtra("categoryId", 0)
        equipmentId = intent.getIntExtra("equipmentId", 0)

        if ( equipmentId == 0) {
            // 新規登録
            setManagementNumber()
            setMakerSpinner()
            setUserSpinner()

            val button: Button = findViewById<View>(R.id.equipment_create_button) as Button
            button.setOnClickListener { createEquipment() }
        } else {
            // 更新
            title = getString(R.string.equipment_update)

            val equipmentViewModel: EquipmentViewModel by viewModels()
            GlobalScope.launch(Dispatchers.Main) {
                val equipment = equipmentViewModel.findById(equipmentId)
                val managementNumber = findViewById<View>(R.id.management_number) as TextView
                val managementNumberString = String.format("%3s", equipment.managementNumber.toString()).replace(" ", "0")

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
    }

    private fun setManagementNumber() {
        val managementNumber = findViewById<View>(R.id.management_number) as TextView
        val equipmentViewModel: EquipmentViewModel by viewModels()

        GlobalScope.launch(Dispatchers.IO) {
            val maxManagementNumber = equipmentViewModel.getMaxManagementNumber(categoryId) + 1
            val managementNumberString = String.format("%3s", maxManagementNumber.toString()).replace(" ", "0")
            managementNumber.text = managementNumberString
        }
        managementNumber.isFocusable = false
    }

    private fun setUserSpinner(userId: Int? = 0) {
        val spinner = findViewById<Spinner>(R.id.user_spinner)
        val userViewModel: UserViewModel by viewModels()

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
        val makerViewModel: MakerViewModel by viewModels()

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
        val equipmentViewModel: EquipmentViewModel by viewModels()

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
        val equipmentViewModel: EquipmentViewModel by viewModels()

        val managementNumber = findViewById<View>(R.id.management_number) as TextView
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
            val equipment = Equipment(
                0,
                Integer.parseInt(managementNumber.text.toString()),
                categoryId,
                maker.id,
                modelName.text.toString(),
                equipmentType.text.toString(),
                user.id,
                usage.text.toString(),
                note.text.toString(),
                purchaseDate
            )
            GlobalScope.launch(Dispatchers.IO) {
                equipmentViewModel.insert(equipment)
            }

            val toast: Toast = Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT)
            toast.show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // 以下、カメラ関係
    // エミュレータだとカメラ取得できない？ため動作確認できてない
    // 処理は動いているみたい
    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_equipment)

        val cameraButton: Button = findViewById<View>(R.id.start_camera_button) as Button
        cameraButton.setOnClickListener {
            println("ボタンは押されました")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager)
            if (intent != null) {
                println("intent != null")
                if (checkCameraPermission()) {
                    println("checkCameraPermission() is true")
                    takePicture()
                } else {
                    println("checkCameraPermission() is false")
                    grantCameraPermission()
                }
            } else {
                println("intent == null")
                val toast: Toast = Toast.makeText(this, "カメラありません", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun takePicture() {
        println("takePicture()")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }

        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission() = PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)


    private fun grantCameraPermission() {
        println("grantCameraPermission()")
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        }
    }
}
