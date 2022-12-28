package com.haniifac.capstonepesaing_revisited.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haniifac.capstonepesaing_revisited.databinding.CategoryItemsBinding
import com.haniifac.capstonepesaing_revisited.databinding.TokoSekitarItemsBinding
import com.haniifac.capstonepesaing_revisited.domain.entity.Category
import com.haniifac.capstonepesaing_revisited.domain.entity.TokoSekitar

class TokoSekitarRecyclerAdapter(private val listTokoSekitar : ArrayList<TokoSekitar>) : RecyclerView.Adapter<TokoSekitarRecyclerAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback: TokoSekitarRecyclerAdapter.OnItemClickCallback

    inner class ViewHolder(private val binding : TokoSekitarItemsBinding): RecyclerView.ViewHolder(binding.root) {
        val img = binding.imgTokoSekitar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TokoSekitarItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toko = listTokoSekitar[position]
        holder.img.setImageResource(toko.img)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listTokoSekitar[holder.adapterPosition], holder)
        }
    }

    override fun getItemCount(): Int = listTokoSekitar.size

    interface OnItemClickCallback {
        fun onItemClicked(tokoSekitar: TokoSekitar, holder: TokoSekitarRecyclerAdapter.ViewHolder)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}