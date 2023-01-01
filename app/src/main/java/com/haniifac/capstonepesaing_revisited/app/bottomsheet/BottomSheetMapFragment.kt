package com.haniifac.capstonepesaing_revisited.app.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.app.ui.detailtoko.DetailTokoFragment
import com.haniifac.capstonepesaing_revisited.app.ui.tokomaps.TokoMapsFragment
import com.haniifac.capstonepesaing_revisited.databinding.FragmentBottomSheetMapBinding

class BottomSheetMapFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentBottomSheetMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCekToko.setBackgroundColor(Color.parseColor("#54B435"))
        binding.btnCekToko.setTextColor(Color.parseColor("#FFFFFF"))
        binding.btnCekToko.setRippleColorResource(androidx.appcompat.R.color.ripple_material_light)

        val idToko = arguments?.getString(TokoMapsFragment.TOKO_ID_KEY)
        val namaToko = arguments?.getString(TokoMapsFragment.TOKO_NAME_KEY)
        val latLon = arguments?.getString(TokoMapsFragment.TOKO_LATLON_KEY)

        binding.tvNamaToko.text = namaToko
        binding.tvLonlat.text = latLon

        binding.btnCekToko.setOnClickListener {
            val bundle = Bundle().apply {
                putString(TokoMapsFragment.TOKO_ID_KEY, idToko)
                putString(TokoMapsFragment.TOKO_NAME_KEY, namaToko)
                putString(TokoMapsFragment.TOKO_LATLON_KEY, latLon)
            }

            val detailToko = DetailTokoFragment().apply {
                arguments = bundle
            }

            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, detailToko)
                addToBackStack(null)
                commit()
            }
        }

    }
}