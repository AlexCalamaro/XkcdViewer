package com.squidink.xkcdviewer.extensions

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.squidink.xkcdviewer.databinding.DialogErrorBinding

internal fun Fragment.showErrorDialog(
    title: String,
    message: String
) {
    val context = requireContext()
    val builder = AlertDialog.Builder(context)
    val dialogBinding = DialogErrorBinding.inflate(layoutInflater)

    dialogBinding.dialogTitle.text = title
    dialogBinding.dialogBody.text = message

    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialogBinding.dialogButtonAffirm.setOnClickListener { dialog.dismiss() }
    dialog.show()
}