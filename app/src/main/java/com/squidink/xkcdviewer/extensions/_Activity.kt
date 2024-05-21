package com.squidink.xkcdviewer.extensions

import android.app.Activity
import android.app.AlertDialog
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.DialogLayoutBinding

internal fun Activity.showErrorDialog(
    title: String,
    message: String
) {
    val context = this
    val builder = AlertDialog.Builder(context)
    val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)

    dialogBinding.dialogIcon.setImageResource(R.drawable.icon_error)
    dialogBinding.dialogTitle.text = title
    dialogBinding.dialogBody.text = message

    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialogBinding.dialogButtonAffirm.setOnClickListener { dialog.dismiss() }
    dialog.show()
}