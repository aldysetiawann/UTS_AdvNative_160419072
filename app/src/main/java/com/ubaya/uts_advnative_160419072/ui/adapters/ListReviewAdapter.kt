package com.ubaya.uts_advnative_160419072.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.uts_advnative_160419072.databinding.ListReviewBinding
import com.ubaya.uts_advnative_160419072.models.Review

class ListReviewAdapter(private val listReview: ArrayList<Review>) :
    RecyclerView.Adapter<ListReviewAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.listReviewUsername.text = review.username
            binding.listReviewRating.rating = review.rating.toFloat()

            if (review.komentar != null) {
                binding.listReviewComment.text = review.komentar
            } else {
                binding.listReviewComment.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(listReview[position])
    }

    override fun getItemCount(): Int {
        return listReview.size
    }
}