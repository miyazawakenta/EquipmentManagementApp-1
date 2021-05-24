package jp.second_wave.equipment_management_app

import android.os.Bundle
import android.app.Dialog
import android.app.AlertDialog
import android.content.Intent
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import jp.second_wave.equipment_management_app.database.view_model.EquipmentViewModel
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MakerDeleteModalFragment(private val maker: Maker, private val parent: ViewGroup): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val delete = "削除"
        val cancel = "キャンセル"
        val messageDeleteCheck = "削除しますか？"

        builder.setTitle(messageDeleteCheck)
            .setPositiveButton(delete) { dialog, id ->
                println("dialog:$dialog which:$id")

                val equipmentViewModel: EquipmentViewModel by viewModels()
                val makerViewModel: MakerViewModel by viewModels()

                GlobalScope.launch(Dispatchers.IO) {
                    val equipmentIncludeMaker = equipmentViewModel.getEquipmentIncludeMaker(maker.id)
                    if (equipmentIncludeMaker == 0) {
                        println("equipmentIncludeMaker == 0")
                        makerViewModel.delete(maker)
                        Snackbar.make(parent, "削除しました", Snackbar.LENGTH_LONG).show()
                    } else {
                        println("equipmentIncludeMaker != 0")
                        Snackbar.make(parent, "登録機材で使用中のため削除できませんでした", Snackbar.LENGTH_LONG).show()
                    }
                }

                // TODO:
                // 削除結果表示してるけど画面再読み込みしてるためすぐ消えてしまう
                // →・再読み込みしないでリストをリフレッシュする
                // →・再読み込み後にメッセージ表示する
                val intent = Intent(activity, MakerActivity::class.java)
                startActivity(intent)

            }
            .setNegativeButton(cancel) { dialog, id ->
                println("dialog:$dialog which:$id")
            }

        return builder.create()
    }

}