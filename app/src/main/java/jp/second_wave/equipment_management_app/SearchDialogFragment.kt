package jp.second_wave.equipment_management_app

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class SearchDialogFragment(private val items: Map<Int, String>) : DialogFragment() {

    var result = ArrayList<Int>()
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
            }
            .setPositiveButton("OK") { _, _ ->
                checkedItems.forEachIndexed { index, b ->
                    if (b) {
                        result.add(index + 1)
                    } else {
                        result.remove(index + 1)
                    }
                }
                listener.onClickButton()
            }

        return builder.create()
    }


}
