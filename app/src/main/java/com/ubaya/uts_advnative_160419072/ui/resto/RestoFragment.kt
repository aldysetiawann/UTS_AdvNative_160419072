package com.ubaya.uts_advnative_160419072.ui.resto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.ubaya.uts_advnative_160419072.R
import com.ubaya.uts_advnative_160419072.databinding.FragmentRestoBinding
import com.ubaya.uts_advnative_160419072.models.Menu
import com.ubaya.uts_advnative_160419072.models.Resto
import com.ubaya.uts_advnative_160419072.ui.adapters.ListMenuAdapter
import kotlin.math.roundToInt

class RestoFragment : Fragment() {

    private lateinit var viewModel: RestoViewModel
    private lateinit var adapter: ListMenuAdapter
    private lateinit var resto: Resto

    private var listMenu: ArrayList<Menu> = arrayListOf()
    private var _binding: FragmentRestoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestoBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resto = Gson().fromJson(arguments?.getString("resto"), Resto::class.java)
        viewModel = ViewModelProvider(this)[RestoViewModel::class.java]
        observer(viewModel)

        if (viewModel.listMenu.value.isNullOrEmpty()) {
            viewModel.loadResto(resto.id)
        }

        adapter = ListMenuAdapter(listMenu)
        val layoutManager = LinearLayoutManager(requireContext())

        binding.restoListMenu.adapter = adapter
        binding.restoListMenu.layoutManager = layoutManager

        binding.restoName.text = resto.nama
        binding.restoAddress.text = resto.alamat
        binding.restoRating.rating = resto.rating!!
        binding.restoTotalRating.text = resto.rating!!.toString()
        binding.restoTotalReview.text = "${resto.ratingCount} ulasan"

        Picasso.get().load(resto.photo).into(binding.restoImg)

        binding.restoBtnReview.setOnClickListener {
            val bundle = bundleOf("resto" to resto.id)
            findNavController().navigate(R.id.action_navigation_resto_to_navigation_reviews, bundle)
        }
    }

    private fun observer(viewModel: RestoViewModel) {
        viewModel.listMenu.observe(viewLifecycleOwner) {
            listMenu.clear()

            if (it.isNotEmpty()) {
                listMenu.addAll(it)

                for (i in 0 until listMenu.size) {
                    adapter.notifyItemInserted(0)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.restoLoading.visibility = View.VISIBLE
                binding.layoutResto.visibility = View.GONE
            } else {
                binding.restoLoading.visibility = View.GONE
                binding.layoutResto.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), viewModel.errorMsg.value, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}