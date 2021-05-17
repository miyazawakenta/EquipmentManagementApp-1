package jp.second_wave.equipment_management_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jp.second_wave.equipment_management_app.R
import jp.second_wave.equipment_management_app.database.entitiy.Maker

class MakerListAdapter(context: Context, private val makers: List<Maker>) : BaseAdapter() {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return makers.count()
    }

    override fun getItem(position: Int): Maker {
        return makers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(R.layout.list, parent, false)
        val maker = makers[position]
        val categoryLabel = view.findViewById<TextView>(R.id.list_name)
        categoryLabel.text = maker.makerName
        return view
    }
}
