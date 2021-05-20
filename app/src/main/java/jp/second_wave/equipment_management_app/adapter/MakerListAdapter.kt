package jp.second_wave.equipment_management_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import jp.second_wave.equipment_management_app.R
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import android.widget.Toast

data class ViewHolder(val nameView: TextView, val deleteView: TextView)

class MakerListAdapter(private val context: Context, private val makers: List<Maker>) : BaseAdapter() {

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
        var viewHolder : ViewHolder? = null
        var view = convertView

        // 再利用の設定
        if (view == null) {

            view = layoutInflater.inflate(R.layout.list, parent, false)

            viewHolder = ViewHolder(
                view.findViewById(R.id.list_name),
                view.findViewById(R.id.delete_button)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        // 項目の情報を設定
        val maker = makers[position]
        val makeName = maker.makerName
        viewHolder.nameView.text = makeName
        viewHolder.deleteView.setOnClickListener { _ ->
            Toast.makeText(context, "$makeName を削除したい", Toast.LENGTH_SHORT).show()
        }

        return view!!
    }
}
