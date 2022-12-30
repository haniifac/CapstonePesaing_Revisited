package com.haniifac.capstonepesaing_revisited.app.ui.tokomaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haniifac.capstonepesaing_revisited.R

class TokoMapsFragment : Fragment() {
    private val permissionId = 2
    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        val indonesia = LatLng(0.7893,113.9213)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia,4f))

//        getLocation(googleMap)
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        getMyLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toko_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.GONE


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkPermissions()) {
            if (!isLocationEnabled()) Toast.makeText(requireContext(), "Nyalakan GPS terlebih dahulu.", Toast.LENGTH_SHORT).show()
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
//
//    @SuppressLint("MissingPermission", "SetTextI18n")
//    private fun getLocation(mMap: GoogleMap) {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mMap.isMyLocationEnabled = true
//            } else {
//                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG).show()
//            }
//        } else {
//            requestPermissions()
//        }
//    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}