package com.haniifac.capstonepesaing_revisited.app.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            loginFirebase()
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loginFirebase(){
        binding.loginProgressBar.visibility = View.VISIBLE
        val email = binding.tvEmail.text.toString()
        val pass = binding.tvPassword.text.toString()

        if(email.isNotBlank() && pass.isNotBlank()){
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()){ task ->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
                    Toast.makeText(context,"Login success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Email or password incorrect", Toast.LENGTH_SHORT).show()
                }
                binding.loginProgressBar.visibility = View.INVISIBLE
            }
        }else{
            binding.loginProgressBar.visibility = View.INVISIBLE
            Toast.makeText(context,"Email or Password Cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

}