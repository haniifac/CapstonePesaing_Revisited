package com.haniifac.capstonepesaing_revisited.app.ui.detailtoko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.haniifac.capstonepesaing_revisited.R
import com.haniifac.capstonepesaing_revisited.databinding.BarangItemBinding
import com.haniifac.capstonepesaing_revisited.domain.entity.Barang

class BarangRecyclerAdapter(private val listBarang : ArrayList<Barang>): RecyclerView.Adapter<BarangRecyclerAdapter.BarangViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val binding = BarangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val (namaBrg, stokBrg, hargaBrg, gambarBrg) = listBarang[position]
        val hargaWithRp = "Rp$hargaBrg"

        holder.namaBarang.text = namaBrg
        holder.stokBarang.text = stokBrg.toString()
        holder.hargaBarang.text = hargaWithRp

        Glide.with(holder.itemView)
            .load(gambarBrg)
            .placeholder(R.drawable.no_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.gambarBarang)
    }

    override fun getItemCount(): Int = listBarang.size

    inner class BarangViewHolder(private val binding: BarangItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val namaBarang = binding.tvBarangNama
        val stokBarang = binding.tvBarangStokJumlah
        val hargaBarang = binding.barangHarga
        val gambarBarang = binding.imgBarang
    }

}