package com.squidink.xkcdviewer.extensions

import android.app.AlertDialog
import android.graphics.Insets
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.databinding.DialogLayoutBinding

internal fun Fragment.showErrorDialog(
    title: String,
    message: String
) {
    val context = requireContext()
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

internal fun Fragment.showContentDialog(
    title: String,
    message: String
) {
    val context = requireContext()
    val builder = AlertDialog.Builder(context)
    val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)

    dialogBinding.dialogIcon.setImageResource(R.drawable.icon_label)
    dialogBinding.dialogTitle.text = title
    dialogBinding.dialogBody.text = message

    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialogBinding.dialogButtonAffirm.setOnClickListener { dialog.dismiss() }
    dialog.show()
}

internal fun Fragment.setTopInset(view: View) {
    var insets: Insets? = null
    view.setOnApplyWindowInsetsListener { view, i ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insets = i.getInsets(WindowInsetsCompat.Type.systemBars())
        }
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                insets?.top ?: i.systemGestureInsets.top
            else
                i.systemWindowInsetTop
        }
        i
    }
}

internal fun Fragment.setBottomInset(view: View) {
    var insets: Insets? = null
    view.setOnApplyWindowInsetsListener { view, i ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insets = i.getInsets(WindowInsetsCompat.Type.systemBars())
        }
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                insets?.bottom ?: i.systemGestureInsets.bottom
            else
                i.systemWindowInsetBottom
        }
        i
    }
}
