package com.dnedev.favorite.places.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.databinding.MapFragmentBinding
import dagger.android.support.DaggerFragment

class MapFragment : DaggerFragment() {

    private lateinit var binding: MapFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.map_fragment,
            container,
            false
        )
        return binding.root
    }
}