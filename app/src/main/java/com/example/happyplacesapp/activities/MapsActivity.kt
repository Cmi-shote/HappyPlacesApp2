package com.example.happyplacesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplacesapp.R
import com.example.happyplacesapp.databinding.ActivityMapsBinding
import com.example.happyplacesapp.models.HappyPlacesModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var binding : ActivityMapsBinding? = null
    private var mhappyPlaceDetails : HappyPlacesModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            mhappyPlaceDetails = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS)
                    as HappyPlacesModel

        }

        if(mhappyPlaceDetails != null){
            setSupportActionBar(binding?.toolbarMap)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mhappyPlaceDetails!!.title

            binding?.toolbarMap?.setNavigationOnClickListener{
                onBackPressed()
            }
        }

        val supportMapFragment : SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(mhappyPlaceDetails!!.latitude, mhappyPlaceDetails!!.longitude)

        googleMap.addMarker(MarkerOptions().position(position).title(mhappyPlaceDetails!!.location))

        //to have the map zoomed in to the particular location
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)

    }
}