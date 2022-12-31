package com.haniifac.capstonepesaing_revisited.app.ui.tokomaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.app.bottomsheet.BottomSheetMapFragment
import com.haniifac.capstonepesaing_revisited.domain.entity.TokoFireStore

class TokoMapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var mFirestore: FirebaseFirestore

    private val callback = OnMapReadyCallback { googleMap ->
        val indonesia = LatLng(0.7893,113.9213)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia,4f))

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        getMyLocation()
        getAllToko()

        googleMap.setOnMarkerClickListener {
//            BottomSheetMapFragment().show(parentFragmentManager, "bottomSheetTag")
            val bottomSheet = BottomSheetMapFragment()
            val bundle = Bundle().apply {
                putString(TOKO_NAME_KEY, it.title)
                putString(TOKO_LATLON_KEY, "Lat = ${it.position.latitude}, Lon = ${it.position.longitude}")
            }

            bottomSheet.arguments = bundle
            bottomSheet.show(parentFragmentManager, "bottomSheetTag")

            false
        }
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
        mFirestore = FirebaseFirestore.getInstance()

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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getAllToko(){
        val tokoRef = mFirestore.collection("toko")
        val listToko = arrayListOf<TokoFireStore>()

        tokoRef.get()
            .addOnSuccessListener { document ->
                for (doc in document.documents){
                    val data = doc.data
                    val lokasi : GeoPoint? = doc.getGeoPoint("lokasi")
                    if (data != null && lokasi != null) {
                        val lat = lokasi.latitude
                        val lon = lokasi.longitude

                        val currToko = TokoFireStore(data["nama"].toString(), LatLng(lat,lon))
                        listToko.add(currToko)
                    }
                }
                showAllTokoMarker(listToko)
            }
            .addOnFailureListener {
                Log.d("Firestore", "get failed with ", it)
            }
    }

    private fun showAllTokoMarker(listToko : ArrayList<TokoFireStore>){
        for(toko in listToko){
            mMap.addMarker(
                MarkerOptions()
                    .position(toko.lokasi)
                    .title(toko.nama)
            )
        }
    }

    companion object{
        const val TOKO_NAME_KEY = "toko_name_key"
        const val TOKO_LATLON_KEY = "toko_latlon_key"
    }

}