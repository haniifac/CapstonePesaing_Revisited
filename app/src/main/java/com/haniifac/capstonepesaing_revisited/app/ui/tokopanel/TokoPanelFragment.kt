package com.haniifac.capstonepesaing_revisited.app.ui.tokopanel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.app.ui.detailtoko.BarangRecyclerAdapter
import com.haniifac.capstonepesaing_revisited.databinding.FragmentTokoPanelBinding
import com.haniifac.capstonepesaing_revisited.domain.entity.Barang


class TokoPanelFragment : Fragment() {
    private var _binding : FragmentTokoPanelBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore : FirebaseFirestore
    private lateinit var rvBarangToko : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTokoPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.GONE

        mFirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        rvBarangToko = binding.rvBarangToko
        val user = mAuth.currentUser?.uid

        if (user != null){
            binding.panelProgressBar.visibility = View.VISIBLE
            getTokoBarang(user)

        }

        binding.btnTambahBarang.setOnClickListener {
            findNavController().navigate(R.id.action_tokoPanelFragment_to_addTokoItemFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getTokoBarang(id : String){
        val toko = mFirestore.collection("toko/${id}_toko/barang")
        val listBarang = ArrayList<Barang>()

        toko.get()
            .addOnSuccessListener { document ->
                for (item in document){
                    val namaBarang: String = item.data["nama"].toString()
                    val stokBarang : Int? = item.getLong("stok")?.toInt()
                    val hargaBarang : Int? = item.getLong("harga")?.toInt()
                    val gambarBarang : String = item.data["gambar"].toString()

                    if (stokBarang != null && hargaBarang != null){
                        listBarang.add(Barang(namaBarang, stokBarang, hargaBarang, gambarBarang))
                    }
                }
                binding.panelProgressBar.visibility = View.INVISIBLE
                showRecyclerBarang(listBarang)
            }
            .addOnFailureListener {
                binding.panelProgressBar.visibility = View.INVISIBLE
                Log.d("Firestore", "get failed with ", it)
            }
    }

    private fun showRecyclerBarang(listBarang : ArrayList<Barang>) {
        rvBarangToko.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listCategoryAdapter = BarangRecyclerAdapter(listBarang)
        rvBarangToko.adapter = listCategoryAdapter
    }
}