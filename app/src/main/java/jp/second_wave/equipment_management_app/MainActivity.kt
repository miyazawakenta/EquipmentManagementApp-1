package jp.second_wave.equipment_management_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.adapter.EquipmentListAdapter
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.view_model.CategoryViewModel
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import jp.second_wave.equipment_management_app.database.view_model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class MainActivity : AppCompatActivity(), SearchDialogFragment.ParentFragmentListener {

    private lateinit var searchManagementNumberEditText: EditText
    private var searchCategoryValues = ArrayList<Int>()
    private var searchUserValues = ArrayList<Int>()
    private var searchMakerValues = ArrayList<Int>()
    private lateinit var categorySearchDialogFragment: SearchDialogFragment
    private lateinit var userSearchDialogFragment: SearchDialogFragment
    private lateinit var makerSearchDialogFragment: SearchDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEquipmentList()
        setSearchOptions()

        val createEquipmentButton = findViewById<ImageButton>(R.id.create_equipment_button)
        createEquipmentButton.setOnClickListener { startCreateEquipment() }

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener { searchEquipment() }

        searchManagementNumberEditText = findViewById(R.id.search_management_number)
        searchManagementNumberEditText.addTextChangedListener(managementNumberTextWatcher)
    }


    private fun searchEquipment() {
        val view = this.currentFocus
        if (view != null) {
            val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val vSearchModel = findViewById<EditText>(R.id.search_model_name)

        val equipmentViewModel: EquipmentViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val equipments = equipmentViewModel.getEquipmentAndRelationAll(
                searchCategoryValues,
                searchUserValues,
                searchMakerValues,
                searchManagementNumberEditText.text.toString(),
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
        val makerViewModel: MakerViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val categories = mutableMapOf<Int, String>()
            categoryViewModel.getAll().forEach { category -> categories[category.id] = category.CategoryName }
            categorySearchDialogFragment = SearchDialogFragment(categories)
            val categoryDialogButton = findViewById<Button>(R.id.category_dialog_button)
            categoryDialogButton.setOnClickListener {
                categorySearchDialogFragment.show(supportFragmentManager, "simple")
            }

            val users = mutableMapOf<Int, String>()
            userViewModel.getAll().forEach { user ->  users[user.id] = user.lastName }
            userSearchDialogFragment = SearchDialogFragment(users)
            val userDialogButton = findViewById<Button>(R.id.user_dialog_button)
            userDialogButton.setOnClickListener {
                userSearchDialogFragment.show(supportFragmentManager, "simple")
            }

            val makers = mutableMapOf<Int, String>()
            makerViewModel.getAll().forEach { maker ->  makers[maker.id] = maker.makerName }
            makerSearchDialogFragment = SearchDialogFragment(makers)
            val makerDialogButton = findViewById<Button>(R.id.maker_dialog_button)
            makerDialogButton.setOnClickListener {
                makerSearchDialogFragment.show(supportFragmentManager, "simple")
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
        searchMakerValues = makerSearchDialogFragment.result

        val categories = findViewById<TextView>(R.id.selected_categories)
        categories.text = categorySearchDialogFragment.resultLabel.joinToString()

        val users = findViewById<TextView>(R.id.selected_users)
        users.text = userSearchDialogFragment.resultLabel.joinToString()

        val makers = findViewById<TextView>(R.id.selected_makers)
        makers.text = makerSearchDialogFragment.resultLabel.joinToString()
    }

    private val pattern: Pattern = Pattern.compile("[0-9]{2}")

    private val managementNumberTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (before == 0) {
                if (pattern.matcher(s).matches()) {
                    searchManagementNumberEditText.setText(resources.getString(R.string.with_hyphen, s))
                    searchManagementNumberEditText.setSelection(searchManagementNumberEditText.editableText.toString().length)
                }
            } else {
                if (pattern.matcher(s).matches()) {
                    searchManagementNumberEditText.setText(s.subSequence(0, s.length -1))
                    searchManagementNumberEditText.setSelection(searchManagementNumberEditText.editableText.toString().length)
                }
            }
        }
    }
}
