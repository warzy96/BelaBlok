package com.hr.warzy.bela.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToStart() {
    smoothScrollToPosition(0)
}

/** Clears focus from view and calls [InputMethodManager.hideSoftInputFromWindow] */
fun View.hideKeyboard() {
    clearFocus()
    (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}