package jp.second_wave.equipment_management_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import jp.second_wave.equipment_management_app.UserDeleteModalFragment
import jp.second_wave.equipment_management_app.R
import jp.second_wave.equipment_management_app.database.entitiy.User

class UserListAdapter(private val context: Context, private val users: List<User>, private val Fragment: FragmentManager) : BaseAdapter() {

    class ViewHolder(val nameView: TextView, val deleteView: TextView)

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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder : MakerListAdapter.ViewHolder? = null
        var view = convertView

        // 再利用の設定
        if (view == null) {

            view = layoutInflater.inflate(R.layout.user_list, parent, false)

            viewHolder = MakerListAdapter.ViewHolder(
                view.findViewById(R.id.user_name),
                view.findViewById(R.id.delete_button)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as MakerListAdapter.ViewHolder
        }

        // 項目の情報を設定
        val user = users[position]
        if (user.email?.isNotEmpty()!!) {
            viewHolder.nameView.text = "${user.lastName}${user.firstName} (${user.email})"
        } else {
            viewHolder.nameView.text = "${user.lastName}${user.firstName}"
        }

        viewHolder.deleteView.setOnClickListener { _ ->
            val dialog = UserDeleteModalFragment(user, parent)
            dialog.show(Fragment, "simple")
        }
        return view!!
    }
}
