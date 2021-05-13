package jp.second_wave.equipment_management_app.database.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jp.second_wave.equipment_management_app.R
import jp.second_wave.equipment_management_app.database.entitiy.Equipment
import jp.second_wave.equipment_management_app.database.entitiy.EquipmentAndUser

class EquipmentListAdapter(private val context: Context, private val equipments: List<EquipmentAndUser>) : BaseAdapter() {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return equipments.count()
    }

    override fun getItem(position: Int): Equipment {
        return equipments[position].equipment
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(R.layout.equipment, parent, false)
        val equipment = equipments[position].equipment
        val user = equipments[position].user

        val vName = view.findViewById<TextView>(R.id.name)
        val vComment = view.findViewById<TextView>(R.id.comment)
        val vManagementNumber = view.findViewById<TextView>(R.id.equipment_management_number)
        val vUser = view.findViewById<TextView>(R.id.user_name)

        val categoryId = String.format("%2s", equipment.categoryId.toString()).replace(" ", "0")
        val managementNumberString = String.format("%3s", equipment.managementNumber.toString()).replace(" ", "0")

        vUser.text = context.getString(R.string.user_name, user.lastName, user.firstName)
        vName.text = equipment.modelName
        vComment.text = equipment.note
        vManagementNumber.text = context.getString(R.string.equipment_management_number_string, categoryId, managementNumberString)
        return view
    }

}
