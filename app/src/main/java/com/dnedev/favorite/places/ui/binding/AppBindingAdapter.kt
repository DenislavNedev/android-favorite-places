package com.dnedev.favorite.places.ui.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.dnedev.favorite.places.R

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

    @BindingAdapter("setFavoriteIcon")
    fun setFavoriteIcon(
        view: ImageView,
        isAddedAsFavorite: Boolean
    ) {
        view.setImageResource(if (isAddedAsFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite)
    }
}