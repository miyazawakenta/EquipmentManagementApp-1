package jp.second_wave.equipment_management_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment


class MenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater :MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_equipment_list -> {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_maker_list -> {
                val intent = Intent(activity, MakerActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_new_equipment -> {
                val intent = Intent(activity, CategoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_user_list -> {
                val intent = Intent(activity, UserActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
