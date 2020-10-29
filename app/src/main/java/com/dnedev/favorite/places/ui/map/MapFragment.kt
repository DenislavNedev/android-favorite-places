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
import com.dnedev.favorite.places.utils.SHOW_ON_MAP_ITEM_KEY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MapViewModel by activityViewModels { viewModelFactory }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.presenter = viewModel
        lifecycle.addObserver(viewModel)
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
            googleMap.setOnInfoWindowClickListener(viewModel)
            viewModel.uiModel.observe(viewLifecycleOwner, {
                map.clear()
                it.listOfMarkers.forEach { marker ->
                    googleMap.addMarker(marker)
                }
            })

            arguments?.getString(SHOW_ON_MAP_ITEM_KEY)?.let {
                viewModel.showOnMap(it)?.let { location ->
                    moveToCurrentLocation(location, googleMap)
                }
                arguments?.remove(SHOW_ON_MAP_ITEM_KEY)
            }

            moveToCurrentLocation(
                LatLng(
                    POMORIE_LATITUDE,
                    POMORIE_LONGITUDE
                ), googleMap
            )
        }
    }

    private fun moveToCurrentLocation(
        currentLocation: LatLng,
        googleMap: GoogleMap
    ) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
    }
}