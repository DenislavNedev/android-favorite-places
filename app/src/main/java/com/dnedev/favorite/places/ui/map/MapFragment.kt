package com.dnedev.favorite.places.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.databinding.MapFragmentBinding
import com.dnedev.favorite.places.utils.POMORIE_LATITUDE
import com.dnedev.favorite.places.utils.POMORIE_LONGITUDE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MapViewModel by activityViewModels { viewModelFactory }
    private lateinit var binding: MapFragmentBinding

    //TODO get arguments for show on map

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.presenter = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoogleMapsFragment()
    }

    private fun initGoogleMapsFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let { googleMap ->
            viewModel.uiModel.observe(viewLifecycleOwner, {
                it.listOfMarkers.forEach { marker ->
                    googleMap.addMarker(marker)
                }
            })

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        POMORIE_LATITUDE,
                        POMORIE_LONGITUDE
                    )
                )
            )
        }
    }
}