package com.ubaya.uts_advnative_160419072.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.uts_advnative_160419072.databinding.FragmentHomeBinding
import com.ubaya.uts_advnative_160419072.models.Resto
import com.ubaya.uts_advnative_160419072.ui.adapters.ListRestoAdapter

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ListRestoAdapter

    private var listResto: ArrayList<Resto> = arrayListOf()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        observer(viewModel)

        adapter = ListRestoAdapter(listResto)
        val layoutManager = LinearLayoutManager(requireContext())

        if (viewModel.listResto.value.isNullOrEmpty()) {
            viewModel.loadListResto()
        }

        binding.homeListResto.adapter = adapter
        binding.homeListResto.layoutManager = layoutManager
    }

    private fun observer(viewModel: HomeViewModel) {
        viewModel.listResto.observe(viewLifecycleOwner) {
            listResto.clear()

            if (it.isNotEmpty()) {
                listResto.addAll(it)

                for (i in 0 until listResto.size) {
                    adapter.notifyItemInserted(i)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.homeLoading.visibility = View.VISIBLE
                binding.homeListResto.visibility = View.GONE
            } else {
                binding.homeLoading.visibility = View.GONE
                binding.homeListResto.visibility = View.VISIBLE
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