package jp.second_wave.equipment_management_app

import android.os.Bundle
import android.app.Dialog
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MakerDeleteModalFragment(private val maker: Maker): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val delete = "削除"
        val cancel = "キャンセル"
        val messageDeleteCheck = "削除しますか？"
        val messageDelete = "削除しました"

        builder.setTitle(messageDeleteCheck)
            .setPositiveButton(delete) { dialog, id ->
                println("dialog:$dialog which:$id")
                val toast: Toast = Toast.makeText(context, messageDelete, Toast.LENGTH_SHORT)
                toast.show()

                val makerViewModel: MakerViewModel by viewModels()

                GlobalScope.launch(Dispatchers.IO) {
                    makerViewModel.delete(maker)
                }

                val intent = Intent(activity, MakerActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton(cancel) { dialog, id ->
                println("dialog:$dialog which:$id")
            }

        return builder.create()
    }

}