package com.mrh.storyapp.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.mrh.storyapp.R
import com.mrh.storyapp.data.UserPreference
import com.mrh.storyapp.data.stories.StoryViewModel
import com.mrh.storyapp.databinding.ActivityStoryMapsBinding
import com.mrh.storyapp.utils.CustomInfoWindowGoogleMap

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var preference: UserPreference
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyViewModel = StoryViewModel()
        preference = UserPreference(this)
        val userToken = preference.getAuthSession().token
        storyViewModel.getStoriesWithLocation(userToken.toString())

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

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        getMyLocation()
        markAllStories()
    }

    private fun markAllStories() {
        storyViewModel.getListStoryObserve().observe(this) { listStory ->
            if(listStory != null) {
                listStory.forEach { storyPlace ->
                    val latLng = LatLng(storyPlace.lat as Double, storyPlace.lon as Double)
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
            } else {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        }
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