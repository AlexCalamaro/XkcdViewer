package com.acalamaro.xkcdviewer.extensions

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.acalamaro.xkcdviewer.databinding.DialogErrorBinding
import com.acalamaro.xkcdviewer.utils.ImageUtils

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