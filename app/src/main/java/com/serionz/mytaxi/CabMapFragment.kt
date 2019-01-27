package com.serionz.mytaxi


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.serionz.mytaxi.api.Repository
import com.serionz.mytaxi.data.Cab
import com.serionz.mytaxi.data.FleetType
import com.serionz.mytaxi.viewmodel.CabsListFragmentViewModel
import com.serionz.mytaxi.viewmodel.CustomViewModelFactory


class CabMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var mViewModel: CabsListFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cab_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = CustomViewModelFactory(Repository())

        // Initialize shared view model
        mViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CabsListFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initMap(savedInstanceState, view)
        initListeners()
    }

    private fun initListeners() {
        // Move camera to the selected cab's location
        mViewModel.selectedCab.observe(this, Observer {
            if (::mMap.isInitialized && it != null) {
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.coordinate.latitude, it.coordinate.longitude), 15f
                    )
                )
            } else {
                setCameraDefaultPosition()
            }
        })

        // Ensures that the map is always populated with the latest cabs data
        mViewModel.cabsList.observe(this, Observer {
            displayCabsOnMap()
        })
    }

    private fun initMap(savedInstanceState: Bundle?, view: View) {
        mMapView = view.findViewById(R.id.map_view) as MapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()

        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setCameraDefaultPosition()
        displayCabsOnMap()
    }

    private fun setCameraDefaultPosition() {
        // Set camera's default position to the lower bounds of Hamburg
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(mViewModel.bounds.p2Lat, mViewModel.bounds.p2Lon),
                10.5f
            )
        )
    }

    private fun displayCabsOnMap() {
        val taxiIcon = getBitmapIcon(R.drawable.ic_taxi_top)
        val poolingIcon = getBitmapIcon(R.drawable.ic_pooling_top)

        // Clear markers on the map
        mMap.clear()

        mViewModel.cabsList.value?.let {
            for (cab: Cab in it) {
                val latLng = LatLng(cab.coordinate.latitude, cab.coordinate.longitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(cab.address)
                        .snippet(cab.fleetType.toString())
                        .icon(
                            when (cab.fleetType) {
                                FleetType.TAXI -> BitmapDescriptorFactory.fromBitmap(taxiIcon)
                                FleetType.POOLING -> BitmapDescriptorFactory.fromBitmap(poolingIcon)
                            }
                        )
                        .rotation(cab.heading.toFloat())
                )
            }
        }
    }

    private fun getBitmapIcon(drawableResource: Int): Bitmap {
        val bitmapDrawable = ContextCompat.getDrawable(context!!, drawableResource) as BitmapDrawable
        return Bitmap.createScaledBitmap(bitmapDrawable.bitmap, 100, 100, false)
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}
