package com.ubaya.uts_advnative_160419072.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.uts_advnative_160419072.databinding.ListMenuBinding
import com.ubaya.uts_advnative_160419072.models.Menu

class ListMenuAdapter(private val listMenu: ArrayList<Menu>) :
    RecyclerView.Adapter<ListMenuAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.listMenuName.text = menu.nama
            binding.listMenuPrice.text = menu.hargaText()
            binding.listMenuDescription.text = menu.deskripsi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(listMenu[position])
    }

    override fun getItemCount(): Int {
        return listMenu.size
    }
}