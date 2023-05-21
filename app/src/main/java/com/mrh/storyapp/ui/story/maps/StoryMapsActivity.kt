package com.mrh.storyapp.ui.story.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.mrh.storyapp.R
import com.mrh.storyapp.data.stories.ListStoryItem
import com.mrh.storyapp.utils.UserPreference
import com.mrh.storyapp.databinding.ActivityStoryMapsBinding
import com.mrh.storyapp.utils.CustomInfoWindowGoogleMap
import com.mrh.storyapp.utils.ViewModelFactory

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private val storyMapViewModel: StoryMapsViewModel by viewModels {
        ViewModelFactory(this)
    }
    private val boundsBuilder = LatLngBounds.Builder()

    companion object {
        const val TAG = "StoryMapsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
        getMyLocation()
        storyMapViewModel.getStoryWithLocation().observe(this) {result ->
            if(result != null) {
                when(result) {
                    is com.mrh.storyapp.data.Result.Success -> {
                        markAllStories(result.data.listStory)
                    }
                    is com.mrh.storyapp.data.Result.Loading -> {
//                        showLoading
                    }
                    is com.mrh.storyapp.data.Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if(!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find syle. Error: ", exception)
        }
    }

    private fun markAllStories(listStory: List<ListStoryItem>) {
        listStory.forEach { storyPlace ->
            val latLng = LatLng(storyPlace.lat, storyPlace.lon)
            val customInfoWindow = CustomInfoWindowGoogleMap(this)
            mMap.setInfoWindowAdapter(customInfoWindow)
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(storyPlace.name)
                .snippet(storyPlace.description)

            val marker = mMap.addMarker(markerOptions)
            marker?.tag = storyPlace
            marker?.showInfoWindow()
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private val requestPermissionLaucher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if(isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLaucher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}