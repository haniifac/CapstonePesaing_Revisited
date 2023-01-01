package com.haniifac.capstonepesaing_revisited.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mFirestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            handleRegister()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun handleRegister(){
        val email = binding.tvEmail.text.toString()
        val pass = binding.tvPassword.text.toString()
        val name = binding.tvName.text.toString()

        if(email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(context,"Email format is not valid", Toast.LENGTH_SHORT).show()
        }else if(email.isNotBlank() && pass.isNotBlank()){
            createUserFirebase(email, pass, name)
//            createUserFirestore(email, name)
        }else{
            Toast.makeText(context,"Email or Password Cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUserFirebase(email: String, pass: String, name: String){
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()){ task ->
            if(task.isSuccessful){
                val user = mAuth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }

                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d(TAG, "User profile updated.")
                        }
                    }

                findNavController().navigate(R.id.action_registerFragment_to_finishRegisterFragment)
                Toast.makeText(context,"Register success", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Email or password invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUserFirestore(email: String, name: String){
        val data = hashMapOf(
            "email" to email,
            "nama" to name
        )

        mFirestore.collection("user").add(data)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
    }

    companion object{
        private const val TAG = "RegisterFragment"
    }
}