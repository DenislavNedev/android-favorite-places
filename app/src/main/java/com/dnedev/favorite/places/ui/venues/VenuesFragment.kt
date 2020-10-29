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
import com.dnedev.favorite.places.navigation.navigateTo
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.venues_fragment.*
import javax.inject.Inject

class VenuesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: VenuesViewModel by activityViewModels { viewModelFactory }

    private val venuesListAdapter by lazy { VenuesListAdapter(viewModel) }
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
        lifecycle.addObserver(viewModel)
        observeUiModel()
        observeNavigation()
        initRecyclerView()
        swipeToRefreshData()
    }

    private fun observeUiModel() {
        viewModel.uiModel.observe(viewLifecycleOwner, {
            binding.uiModel = it
            venuesListAdapter.submitList(it.listOfVenues)
        })
    }

    private fun swipeToRefreshData() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.initViewModel()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeNavigation() {
        viewModel.navigation.observe(viewLifecycleOwner, {
            requireActivity().navigateTo(it)
        })
    }

    private fun initRecyclerView() {
        with(venuesRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = venuesListAdapter
        }
    }
}