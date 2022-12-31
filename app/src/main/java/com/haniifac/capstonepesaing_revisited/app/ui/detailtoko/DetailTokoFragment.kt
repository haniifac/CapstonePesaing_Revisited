package com.haniifac.capstonepesaing_revisited.app.ui.detailtoko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.app.ui.tokomaps.TokoMapsFragment
import com.haniifac.capstonepesaing_revisited.databinding.FragmentDetailTokoBinding

class DetailTokoFragment : Fragment() {
    private var _binding : FragmentDetailTokoBinding? = null
    private val binding get() = _binding!!

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
        val idToko = arguments?.getString(TokoMapsFragment.TOKO_ID_KEY)

        binding.tvTitleNamaToko.text = idToko
    }
}