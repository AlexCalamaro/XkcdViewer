package com.squidink.xkcdviewer.extensions

import android.annotation.SuppressLint
import android.view.Menu
import androidx.appcompat.view.menu.MenuBuilder

@SuppressLint("RestrictedApi")
fun Menu.showOptionalIcons() {
    if(this is MenuBuilder) {
        setOptionalIconsVisible(true)
    }
}