package com.dnedev.favorite.places.ui.venues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.databinding.VenuesFragmentBinding
import dagger.android.support.DaggerFragment

class VenuesFragment : DaggerFragment() {

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
}