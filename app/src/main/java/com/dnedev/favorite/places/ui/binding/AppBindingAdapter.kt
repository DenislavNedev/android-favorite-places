package com.dnedev.favorite.places.ui.binding

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

object AppBindingAdapter {
    @BindingAdapter("setTextResource")
    fun setTextResource(
        view: TextView,
        @StringRes stringResource: Int
    ) {
        if (stringResource != 0) {
            view.text = view.context.getString(stringResource)
        }
    }

}