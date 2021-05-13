package jp.second_wave.equipment_management_app.database

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jp.second_wave.equipment_management_app.R
import jp.second_wave.equipment_management_app.database.entitiy.Equipment

class EquipmentListAdapter(private val context: Context, private val equipments: List<Equipment>) : BaseAdapter() {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return equipments.count()
    }

    override fun getItem(position: Int): Equipment {
        return equipments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(R.layout.equipment, parent, false)
        val equipment = equipments[position]
        val name = view.findViewById<TextView>(R.id.name)
        val comment = view.findViewById<TextView>(R.id.comment)
        name.text = equipment.modelName
        comment.text = equipment.note
        return view
    }

}
