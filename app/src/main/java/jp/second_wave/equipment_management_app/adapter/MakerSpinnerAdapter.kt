package jp.second_wave.equipment_management_app.adapter

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import jp.second_wave.equipment_management_app.database.entitiy.Maker


class MakerSpinnerAdapter(context: Context, private val makers: List<Maker>) : ArrayAdapter<Maker>(context, 0, makers) {
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


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = layoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        val maker = getItem(position) as Maker
        textView.text = maker.makerName

        return view
    }
}

