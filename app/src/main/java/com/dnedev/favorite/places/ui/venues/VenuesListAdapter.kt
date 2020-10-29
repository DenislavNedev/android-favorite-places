package com.dnedev.favorite.places.ui.venues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.databinding.VenueHeaderItemBinding
import com.dnedev.favorite.places.databinding.VenueListItemBinding
import com.dnedev.favorite.places.utils.DataBoundListAdapter

class VenuesListAdapter(private val presenter: VenueItemPresenter) :
    DataBoundListAdapter<VenueListItem, ViewDataBinding>(VenueDiffUtil()) {

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ViewDataBinding = when (viewType) {
        VENUE_DATA_ITEM -> createViewDataBinding(
            R.layout.venue_list_item,
            parent
        )
        else -> createViewDataBinding(
            R.layout.venue_header_item,
            parent
        )
    }

    private fun createViewDataBinding(
        @LayoutRes layout: Int,
        parent: ViewGroup
    ): ViewDataBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

    override fun bind(binding: ViewDataBinding, item: VenueListItem) {
        when (binding) {
            is VenueHeaderItemBinding -> {
                binding.uiModel = item as VenueHeaderUiModel
                binding.presenter = presenter
            }
            is VenueListItemBinding -> {
                binding.uiModel = item as VenueItemUiModel
                binding.presenter = presenter
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is VenueItemUiModel -> VENUE_DATA_ITEM
        else -> VENUE_HEADER_ITEM
    }

    companion object {
        private const val VENUE_HEADER_ITEM = 0
        private const val VENUE_DATA_ITEM = 1
    }
}

class VenueDiffUtil : DiffUtil.ItemCallback<VenueListItem>() {

    override fun areItemsTheSame(oldItem: VenueListItem, newItem: VenueListItem): Boolean =
        if (oldItem is VenueItemUiModel && newItem is VenueItemUiModel) oldItem.id == newItem.id
        else oldItem === newItem

    override fun areContentsTheSame(oldItem: VenueListItem, newItem: VenueListItem): Boolean =
        if (oldItem is VenueItemUiModel && newItem is VenueItemUiModel) oldItem == newItem
        else oldItem.equals(newItem)
}