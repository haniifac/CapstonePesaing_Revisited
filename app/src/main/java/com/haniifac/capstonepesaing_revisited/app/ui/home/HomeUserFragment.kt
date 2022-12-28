package com.haniifac.capstonepesaing_revisited.app.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.FragmentHomeUserBinding
import com.haniifac.capstonepesaing_revisited.domain.entity.Category
import com.haniifac.capstonepesaing_revisited.domain.entity.TokoSekitar

class HomeUserFragment : Fragment() {
    private var _binding : FragmentHomeUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth

    private lateinit var rvCategory: RecyclerView
    private lateinit var rvTokoSekitar : RecyclerView
    private val list1 = ArrayList<Category>()
    private val list2 = ArrayList<TokoSekitar>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
//        mAuth.signOut()

        rvCategory = binding.rvCategory
        rvCategory.setHasFixedSize(true)
        list1.addAll(listCategory)
        showRecyclerCategory()

        rvTokoSekitar = binding.rvTokoSekitar
        rvTokoSekitar.setHasFixedSize(true)
        list2.addAll(listTokoSekitar)
        showRecyclerTokoSekitar()
    }

    private val listCategory: ArrayList<Category>
        get() {
            val categoryName = resources.getStringArray(R.array.category_name)
            val categoryImg = resources.obtainTypedArray(R.array.category_img)

            val list = ArrayList<Category>()
            for (i in categoryName.indices) {
                val category = Category(categoryName[i], categoryImg.getResourceId(i, -1))
                list.add(category)
                Log.e("ListCategory","${categoryName[i]}, ${categoryImg.getResourceId(i,-1)}")
            }
            return list
        }

    private val listTokoSekitar: ArrayList<TokoSekitar>
        get() {
            val categoryImg = resources.obtainTypedArray(R.array.toko_sekitar_img)

            val list = ArrayList<TokoSekitar>()
                for (i in 0..2){
                val toko = TokoSekitar(categoryImg.getResourceId(i, -1))
                list.add(toko)
                Log.e("ListTokoSekitar","${categoryImg.getResourceId(i,-1)}")
            }
            return list
        }

    private fun showRecyclerCategory() {
        rvCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val listCategoryAdapter = CategoryRecyclerAdapter(list1)
        rvCategory.adapter = listCategoryAdapter

        listCategoryAdapter.setOnItemClickCallback(object : CategoryRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(
                category: Category,
                holder: CategoryRecyclerAdapter.MainCategoryHolder
            ) {
                showSelectedCategory(category)
            }
        })
    }

    private fun showRecyclerTokoSekitar(){
        rvTokoSekitar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val listTokoSekitarAdapter = TokoSekitarRecyclerAdapter(list2)
        rvTokoSekitar.adapter = listTokoSekitarAdapter

        listTokoSekitarAdapter.setOnItemClickCallback(object : TokoSekitarRecyclerAdapter.OnItemClickCallback{
            override fun onItemClicked(
                tokoSekitar: TokoSekitar,
                holder: TokoSekitarRecyclerAdapter.ViewHolder
            ) {
                Toast.makeText(context, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSelectedCategory(category: Category) {
        Toast.makeText(context, "Kamu memilih " + category.category, Toast.LENGTH_SHORT).show()
    }

}