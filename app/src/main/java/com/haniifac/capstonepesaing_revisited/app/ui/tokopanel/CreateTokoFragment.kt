package com.haniifac.capstonepesaing_revisited.app.ui.tokopanel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentCreateTokoBinding

class CreateTokoFragment : Fragment() {
    private var _binding : FragmentCreateTokoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateTokoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.GONE

        mFirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()



        binding.btnCreateToko.setOnClickListener {
            createToko()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createToko(){
        val namaToko = binding.edtNamaToko.text.toString().trim()
        val latToko = binding.edtLatitudeToko.text.toString().trim()
        val lonToko = binding.edtLongitudeToko.text.toString().trim()
        val userUid = mAuth.currentUser?.uid

        val lokasiToko = GeoPoint(latToko.toDouble(), lonToko.toDouble())

        val data = hashMapOf(
            "lokasi" to lokasiToko,
            "nama" to namaToko
        )

        mFirestore.document("toko/${userUid}_toko").set(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Toko successfully created", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_createTokoFragment_to_tokoPanelFragment)
            }

        if (userUid != null) {
            updateHasTokoToTrue(userUid)
        }
    }

    private fun updateHasTokoToTrue(uid: String){
        mFirestore.document("user/${uid}").update("hasToko",true)
    }
}