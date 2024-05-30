package com.example.puffnpoof

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ClosingPage : AppCompatActivity() , OnMapReadyCallback{

    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closing_page)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        button = findViewById(R.id.closing_button)

        var mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onMapReady(p0: GoogleMap) {
        myMap = p0
        val place = LatLng(-6.20201, 106.78113)
        myMap.addMarker(MarkerOptions().position(place).title("PuFF and Poof"))
        myMap.moveCamera(CameraUpdateFactory.newLatLng(place))
    }


}