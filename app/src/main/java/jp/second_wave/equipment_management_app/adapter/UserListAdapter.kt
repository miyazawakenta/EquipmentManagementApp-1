package jp.second_wave.equipment_management_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jp.second_wave.equipment_management_app.database.entitiy.User

class UserListAdapter(context: Context, private val users: List<User>) : BaseAdapter() {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return users.count()
    }

    override fun getItem(position: Int): User {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        val user = users[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        if (user.email?.isNotEmpty()!!) {
            textView.text = "${user.lastName}${user.firstName} (${user.email})"
        } else {
            textView.text = "${user.lastName}${user.firstName}"
        }
        return view
    }
}
