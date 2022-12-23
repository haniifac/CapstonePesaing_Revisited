package com.haniifac.capstonepesaing_revisited.app.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentFinishRegisterBinding

class FinishRegisterFragment : Fragment() {
    private var _binding : FragmentFinishRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_finishRegisterFragment_to_loginFragment)
        }
    }
}