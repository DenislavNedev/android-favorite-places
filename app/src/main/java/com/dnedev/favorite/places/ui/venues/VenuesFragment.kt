package com.dnedev.favorite.places.ui.venues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.databinding.VenuesFragmentBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.venues_fragment.*
import javax.inject.Inject

class VenuesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: VenuesViewModel by activityViewModels { viewModelFactory }

    //TODO use only one recyclerView with one adapter
    private val restaurantsListAdapter by lazy { VenuesListAdapter(viewModel) }
    private val supermarketsListAdapter by lazy { VenuesListAdapter(viewModel) }
    private lateinit var binding: VenuesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.venues_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeUiModel()
        initRecyclerView()
    }

    private fun observeUiModel() {
        viewModel.uiModel.observe(viewLifecycleOwner, {
            binding.uiModel = it
            restaurantsListAdapter.submitList(it.listOfRestaurants)
            supermarketsListAdapter.submitList(it.listOfSupermarkets)
        })
    }

    private fun initRecyclerView() {
        with(restaurantRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantsListAdapter
        }

        with(supermarketsRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = supermarketsListAdapter
        }
    }
}