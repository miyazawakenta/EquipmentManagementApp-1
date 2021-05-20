package jp.second_wave.equipment_management_app

import android.os.Bundle
import android.app.Dialog
import android.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteModalFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Here Title")
            .setMessage("Here Message")
            .setPositiveButton("done") { dialog, id ->
                println("dialog:$dialog which:$id")
            }
            .setNegativeButton("cancel") { dialog, id ->
                println("dialog:$dialog which:$id")
            }

        return builder.create()
    }

}