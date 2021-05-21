package jp.second_wave.equipment_management_app

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class SearchDialogFragment(private val items: MutableMap<Int, String>) : DialogFragment() {

    var result = ArrayList<Int>()
    var resultLabel = ArrayList<String>()
    private lateinit var listener: ParentFragmentListener

    interface ParentFragmentListener {
        fun onClickButton()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ParentFragmentListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val checkedItems = items.map { result.contains(it.key) }.toBooleanArray()
        val builder = AlertDialog.Builder(activity)
            .setTitle(getString(R.string.select))
            .setMultiChoiceItems(items.map { it.value }.toTypedArray(), checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
                val id  = items.keys.toList()[which]
                if (isChecked) {
                    result.add(id)
                    resultLabel.add("${items[id]}")
                } else {
                    result.remove(id)
                    resultLabel.remove(items[id])
                }
            }
            .setPositiveButton("OK") { _, _ ->
                listener.onClickButton()
            }

        return builder.create()
    }


}
