package com.ubaya.uts_advnative_160419072.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.uts_advnative_160419072.databinding.FragmentReviewsBinding
import com.ubaya.uts_advnative_160419072.models.Review
import com.ubaya.uts_advnative_160419072.ui.adapters.ListReviewAdapter
import kotlin.properties.Delegates

class ReviewsFragment : Fragment() {

    private lateinit var viewModel: ReviewsViewModel
    private lateinit var adapter: ListReviewAdapter
    private var resto by Delegates.notNull<Int>()

    private var listReview: ArrayList<Review> = arrayListOf()
    private var _binding: FragmentReviewsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resto = arguments?.getInt("resto")!!
        viewModel = ViewModelProvider(this)[ReviewsViewModel::class.java]
        observer(viewModel)

        if (viewModel.listReview.value.isNullOrEmpty()) {
            viewModel.loadReview(resto)
        }

        adapter = ListReviewAdapter(listReview)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true

        binding.reviewsListReview.adapter = adapter
        binding.reviewsListReview.layoutManager = layoutManager

        binding.reviewsBtnAdd.setOnClickListener {
            val komentar = binding.reviewsInputComment.text.toString()

            if (binding.reviewsRating.rating == 0.0f) {
                Toast.makeText(requireContext(), "Rating tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (komentar.isEmpty()) {
                    viewModel.addReview(resto, binding.reviewsRating.rating.toInt(), komentar)
                } else {
                    viewModel.addReview(resto, binding.reviewsRating.rating.toInt())
                }
            }
        }
    }

    private fun observer(viewModel: ReviewsViewModel) {
        viewModel.listReview.observe(viewLifecycleOwner) {
            listReview.clear()

            if (it.isNotEmpty()) {
                listReview.addAll(it)

                for (i in 0 until listReview.size) {
                    adapter.notifyItemInserted(0)
                }
            }
        }

        viewModel.isReviewed.observe(viewLifecycleOwner) {
            if (it) {
                binding.cardReview.visibility = View.GONE
            } else {
                binding.cardReview.visibility = View.VISIBLE
            }
        }

        viewModel.review.observe(viewLifecycleOwner) {
            listReview.add(it)
            adapter.notifyDataSetChanged()
            viewModel.isReviewed.value = false
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.reviewsLoading.visibility = View.VISIBLE
                binding.layoutReviews.visibility = View.GONE
            } else {
                binding.reviewsLoading.visibility = View.GONE
                binding.layoutReviews.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), viewModel.errorMsg.value, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}