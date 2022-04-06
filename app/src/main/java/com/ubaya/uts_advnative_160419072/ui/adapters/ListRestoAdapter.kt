package com.ubaya.uts_advnative_160419072.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.ubaya.uts_advnative_160419072.R
import com.ubaya.uts_advnative_160419072.databinding.CardRestoBinding
import com.ubaya.uts_advnative_160419072.models.Resto

class ListRestoAdapter(private val listResto: ArrayList<Resto>) :
    RecyclerView.Adapter<ListRestoAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CardRestoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resto: Resto) {
            binding.cardRestoName.text = resto.nama
            binding.cardRestoAddress.text = resto.alamat
            binding.cardRestoRating.rating = resto.rating!!
            binding.cardRestoTotalRating.text = resto.rating!!.toString()
            binding.cardRestoTotalReview.text = "${resto.ratingCount} ulasan"

            binding.cardResto.setOnClickListener {
                val bundle = bundleOf("resto" to Gson().toJson(resto))
                Navigation.findNavController(it)
                    .navigate(R.id.navigation_resto, bundle)
            }

            Picasso.get().load(resto.photo).into(binding.cardRestoPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardRestoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(listResto[position])
    }

    override fun getItemCount(): Int {
        return listResto.size
    }
}