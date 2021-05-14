package jp.second_wave.equipment_management_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jp.second_wave.equipment_management_app.R
import jp.second_wave.equipment_management_app.database.entitiy.Category

class CategoryListAdapter(context: Context, private val categories: List<Category>) : BaseAdapter() {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return categories.count()
    }

    override fun getItem(position: Int): Category {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(R.layout.category, parent, false)
        val category = categories[position]
        val categoryLabel = view.findViewById<TextView>(R.id.category_name)
        categoryLabel.text = category.CategoryName
        return view
    }
}
