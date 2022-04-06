package com.ubaya.uts_advnative_160419072.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.uts_advnative_160419072.R
import com.ubaya.uts_advnative_160419072.databinding.FragmentSearchBinding
import com.ubaya.uts_advnative_160419072.models.Resto
import com.ubaya.uts_advnative_160419072.ui.adapters.ListRestoAdapter
import com.ubaya.uts_advnative_160419072.ui.home.HomeViewModel
import java.util.*

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ListRestoAdapter
    private lateinit var searchView: SearchView

    private var listResto: ArrayList<Resto> = arrayListOf()
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        observer(viewModel)

        adapter = ListRestoAdapter(listResto)
        val layoutManager = LinearLayoutManager(requireContext())

        if (viewModel.listResto.value.isNullOrEmpty()) {
            viewModel.loadListResto()
        }

        binding.searchListResto.adapter = adapter
        binding.searchListResto.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(this)
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
                binding.searchLoading.visibility = View.VISIBLE
                binding.searchListResto.visibility = View.GONE
            } else {
                binding.searchLoading.visibility = View.GONE
                binding.searchListResto.visibility = View.VISIBLE
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
        searchView.setOnQueryTextListener(null)
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        listResto.clear()
        if (!query.isNullOrEmpty()) {
            val newList = viewModel.listResto.value!!.filter {
                it.nama.lowercase(Locale.getDefault()).contains(query, true)
            }
            Log.d("NEWLIST", newList.toString())
            listResto.addAll(newList)
            adapter.notifyDataSetChanged()
        } else {
            listResto.addAll(viewModel.listResto.value!!)
            adapter.notifyDataSetChanged()
        }

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        listResto.clear()
        if (!query.isNullOrEmpty()) {
            val newList = viewModel.listResto.value!!.filter {
                it.nama.lowercase(Locale.getDefault()).contains(query, true)
            }
            Log.d("NEWLIST", newList.toString())
            listResto.addAll(newList)
        } else {
            listResto.addAll(viewModel.listResto.value!!)
        }

        adapter.notifyDataSetChanged()

        return false
    }
}