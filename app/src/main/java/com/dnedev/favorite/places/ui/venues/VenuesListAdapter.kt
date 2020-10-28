package com.dnedev.favorite.places.ui.venues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.databinding.VenueListItemBinding
import com.dnedev.favorite.places.utils.DataBoundListAdapter

class VenuesListAdapter(private val presenter: VenueItemPresenter) :
    DataBoundListAdapter<VenueItemUiModel, VenueListItemBinding>(VenueDiffUtil()) {
    override fun createBinding(parent: ViewGroup, viewType: Int): VenueListItemBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.venue_list_item,
            parent,
            false
        )

    override fun bind(binding: VenueListItemBinding, item: VenueItemUiModel) {
        binding.presenter = presenter
        binding.uiModel = item
    }
}

class VenueDiffUtil : DiffUtil.ItemCallback<VenueItemUiModel>() {
    override fun areItemsTheSame(oldItem: VenueItemUiModel, newItem: VenueItemUiModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: VenueItemUiModel, newItem: VenueItemUiModel): Boolean =
        oldItem == newItem

}