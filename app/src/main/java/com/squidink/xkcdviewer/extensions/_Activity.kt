package com.squidink.xkcdviewer.extensions

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.data.SettingsDataSource
import com.squidink.xkcdviewer.databinding.DialogLayoutSinglebuttonBinding
import com.squidink.xkcdviewer.databinding.DialogLayoutTwobuttonBinding
import kotlinx.coroutines.launch

internal fun Activity.showErrorDialog(
    title: String,
    message: String
) {
    val context = this
    val builder = AlertDialog.Builder(context)
    val dialogBinding = DialogLayoutSinglebuttonBinding.inflate(layoutInflater)

    dialogBinding.dialogIcon.setImageResource(R.drawable.icon_error)
    dialogBinding.dialogTitle.text = title
    dialogBinding.dialogBody.text = message

    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialogBinding.dialogButtonAffirm.setOnClickListener { dialog.dismiss() }
    dialog.show()
}

internal fun AppCompatActivity.showNotificationRequestDialog(
    settingsDataSource: SettingsDataSource
) : AlertDialog {
    val context = this
    val builder = AlertDialog.Builder(context)
    val dialogBinding = DialogLayoutTwobuttonBinding.inflate(layoutInflater)

    dialogBinding.dialogIcon.setImageResource(R.drawable.xkcd_hat)
    dialogBinding.dialogTitle.text = getString(R.string.notification_prompt_title)
    dialogBinding.dialogBody.text = getString(R.string.notification_prompt_body)
    dialogBinding.dialogButtonAffirm.text = getString(R.string.notification_prompt_button_accept)
    dialogBinding.dialogButtonDecline.text = getString(R.string.notification_prompt_button_decline)

    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialogBinding.dialogButtonDecline.setOnClickListener { dialog.dismiss() }
    dialogBinding.dialogButtonAffirm.setOnClickListener {
        lifecycleScope.launch {
            settingsDataSource.setNotificationsToggleState(true)
            dialog.dismiss()
        }
    }
    dialog.show()
    return dialog
}