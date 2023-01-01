package com.haniifac.capstonepesaing_revisited.app.ui.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentAccountBinding
import com.haniifac.capstonepesaing_revisited.databinding.FragmentHomeUserBinding

class AccountFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private var _binding : FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFirestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        val userUid = mAuth.currentUser?.uid

        binding.tvNameAccount.text = mAuth.currentUser?.displayName

        binding.btnLogoutProfile.setOnClickListener {
            mAuth.signOut()
            findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
        }

        binding.btnOpenStore.setOnClickListener {
            mFirestore.document("user/$userUid").get()
                .addOnSuccessListener {
                    val hasToko = it.getBoolean("hasToko")
                    if (hasToko == true){
                        findNavController().navigate(R.id.action_accountFragment_to_tokoPanelFragment)
                    }else{
                        findNavController().navigate(R.id.action_accountFragment_to_createTokoFragment)
                    }
                }
                .addOnFailureListener {
                    Log.e("AccountFragment","error ${it.message}")
                }
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}