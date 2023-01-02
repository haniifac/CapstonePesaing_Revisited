package com.haniifac.capstonepesaing_revisited.app.ui.tokopanel

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentAddTokoItemBinding
import com.haniifac.capstonepesaing_revisited.util.reduceFileImage
import com.haniifac.capstonepesaing_revisited.util.uriToFile
import java.io.File


class AddTokoItemFragment : Fragment() {
    private var _binding : FragmentAddTokoItemBinding? = null
    private val binding get() = _binding!!

    private var getFile: File? = null

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mStorage : FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTokoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mStorage = Firebase.storage

        val uid = mFirebaseAuth.currentUser?.uid

        binding.btnAddImageItem.setOnClickListener { startGallery() }

        binding.btnAddItem.setOnClickListener {
            if (uid != null){
                uploadImage(uid)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile
            binding.imgItemBarang.setImageURI(selectedImg)
        }
    }

    fun uploadImage(uid: String){
        val nama = binding.tvNamaItem.text.toString().trim()
        val harga = binding.tvHarga.text.toString().trim().toDouble()
        val stok = binding.tvJumlahStok.text.toString().trim().toInt()

        val data = mutableMapOf(
            "nama" to nama,
            "harga" to harga,
            "stok" to stok,
        )

        val storageRef = mStorage.reference
        val fileName = System.currentTimeMillis()
        val file = getFile?.let { reduceFileImage(it) }
        val uriFile = Uri.fromFile(file)
        val fileRef = storageRef.child("barang/$fileName.jpeg")

        fileRef.putFile(uriFile)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Sukses menambah barang baru", Toast.LENGTH_LONG).show()

                fileRef.downloadUrl.addOnSuccessListener {
                    data["gambar"] = it.toString()
                    mFirestore.collection("toko/${uid}_toko/barang").add(data)
                        .addOnSuccessListener {
                            Log.e("AddTokoItem","success adding new barang in firestore")
                        }
                        .addOnFailureListener {
                            Log.e("AddTokoItem","failed adding new barang in firestore")
                        }
                }
                findNavController().navigate(R.id.action_addTokoItemFragment_to_tokoPanelFragment)

            }
            .addOnFailureListener{
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
            .addOnProgressListener {
                binding.progressBar.visibility = View.VISIBLE
            }

    }
}