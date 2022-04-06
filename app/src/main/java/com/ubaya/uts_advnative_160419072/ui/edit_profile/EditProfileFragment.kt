package com.ubaya.uts_advnative_160419072.ui.edit_profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ubaya.uts_advnative_160419072.R
import com.ubaya.uts_advnative_160419072.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var viewModel: EditProfileViewModel

    private var _binding: FragmentEditProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        observer(viewModel)

        binding.editProfilBtnEdit.setOnClickListener {
            val username = binding.editProfilInputUsername.text
            val password = binding.editProfilInputPassword.text
            val repeat = binding.editProfilInputPasswordRepeat.text

            if (username.isNullOrEmpty() || password.isNullOrEmpty() || repeat.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Username, password dan ulang password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (password.toString() == repeat.toString()) {
                    viewModel.edit(username.toString(), password.toString())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password tidak cocok",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observer(viewModel: EditProfileViewModel) {
        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingEditProfil.visibility = View.VISIBLE
                binding.layoutRegister.visibility = View.GONE
            } else {
                binding.loadingEditProfil.visibility = View.GONE
                binding.layoutRegister.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), viewModel.errorMsg.value, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}