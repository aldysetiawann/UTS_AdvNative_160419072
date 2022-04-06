package com.ubaya.uts_advnative_160419072.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ubaya.uts_advnative_160419072.Globals
import com.ubaya.uts_advnative_160419072.R
import com.ubaya.uts_advnative_160419072.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        observer(viewModel)

        binding.registerBtnRegister.setOnClickListener {
            val username = binding.registerInputUsername.text
            val password = binding.registerInputPassword.text
            val repeat = binding.registerInputPasswordRepeat.text

            if (username.isNullOrEmpty() || password.isNullOrEmpty() || repeat.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Username, password dan ulang password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (password.toString() == repeat.toString()) {
                    viewModel.register(username.toString(), password.toString())
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

    private fun observer(viewModel: RegisterViewModel) {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it !== null) {
                val prefs = context?.getSharedPreferences("app", Context.MODE_PRIVATE)
                prefs?.edit {
                    this.clear()
                    this.putString("user", Gson().toJson(it))
                }
                Globals.user = it
                findNavController().navigate(R.id.action_navigation_register_to_navigation_home)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.registerInputUsername.isEnabled = false
                binding.registerInputPassword.isEnabled = false
                binding.registerInputPasswordRepeat.isEnabled = true
                binding.registerBtnRegister.isEnabled = false
            } else {
                binding.registerInputUsername.isEnabled = true
                binding.registerInputPassword.isEnabled = true
                binding.registerInputPasswordRepeat.isEnabled = true
                binding.registerBtnRegister.isEnabled = true
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                val msg = viewModel.errorMsg.value

                Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}