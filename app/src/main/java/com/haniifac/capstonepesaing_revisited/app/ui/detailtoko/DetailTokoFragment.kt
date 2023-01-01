package com.haniifac.capstonepesaing_revisited.app.ui.detailtoko

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.app.ui.tokomaps.TokoMapsFragment
import com.haniifac.capstonepesaing_revisited.databinding.FragmentDetailTokoBinding
import com.haniifac.capstonepesaing_revisited.domain.entity.Barang
import com.haniifac.capstonepesaing_revisited.domain.entity.TokoFireStore

class DetailTokoFragment : Fragment() {
    private var _binding : FragmentDetailTokoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var rvBarang : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTokoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirestore = FirebaseFirestore.getInstance()

        val idToko = arguments?.getString(TokoMapsFragment.TOKO_ID_KEY)
        val namaToko = arguments?.getString(TokoMapsFragment.TOKO_NAME_KEY)
        val lokasiToko = arguments?.getString(TokoMapsFragment.TOKO_LATLON_KEY)

        binding.tvTitleNamaToko.text = namaToko

        if (idToko != null) {
            getTokoBarang(idToko)
        }

        rvBarang = binding.rvBarang
        rvBarang.setHasFixedSize(true)

    }

    private fun getTokoBarang(id : String){
        val toko = mFirestore.collection("toko/$id/barang")
        val listBarang = ArrayList<Barang>()

        toko.get()
            .addOnSuccessListener { document ->
                for (item in document){
                    val namaBarang: String = item.data["nama"].toString()
                    val stokBarang : Int? = item.getLong("stok")?.toInt()
                    val hargaBarang : Int? = item.getLong("harga")?.toInt()
                    val gambarBarang : String = item.data["gambar"].toString()


//                    Log.e("DetailToko", "$namaBarang $stokBarang, $hargaBarang")
                    if (stokBarang != null && hargaBarang != null){
                        listBarang.add(Barang(namaBarang, stokBarang, hargaBarang, gambarBarang))
                    }
                }
                showRecyclerBarang(listBarang)
            }
            .addOnFailureListener {
                Log.d("Firestore", "get failed with ", it)
            }
    }

    private fun showRecyclerBarang(listBarang : ArrayList<Barang>){
        rvBarang.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listCategoryAdapter = BarangRecyclerAdapter(listBarang)
        rvBarang.adapter = listCategoryAdapter

//        listCategoryAdapter.setOnItemClickCallback(object : CategoryRecyclerAdapter.OnItemClickCallback {
//            override fun onItemClicked(
//                category: Category,
//                holder: CategoryRecyclerAdapter.MainCategoryHolder
//            ) {
//                showSelectedCategory(category)
//            }
//        })
    }


}