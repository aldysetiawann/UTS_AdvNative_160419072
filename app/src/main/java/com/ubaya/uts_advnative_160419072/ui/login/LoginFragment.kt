package com.ubaya.uts_advnative_160419072.ui.login

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
import com.ubaya.uts_advnative_160419072.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        observer(viewModel)

        binding.loginBtnLogin.setOnClickListener {
            val username = binding.loginInputUsername.text
            val password = binding.loginInputPassword.text

            if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Username dan password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.login(username.toString(), password.toString())
            }
        }

        binding.loginBtnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_navigation_register)
        }
    }

    private fun observer(viewModel: LoginViewModel) {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it !== null) {
                val prefs = context?.getSharedPreferences("app", Context.MODE_PRIVATE)
                prefs?.edit {
                    this.clear()
                    this.putString("user", Gson().toJson(it))
                }
                Globals.user = it
                findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loginInputUsername.isEnabled = false
                binding.loginInputPassword.isEnabled = false
                binding.loginBtnLogin.isEnabled = false
                binding.loginBtnRegister.isEnabled = false
            } else {
                binding.loginInputUsername.isEnabled = true
                binding.loginInputPassword.isEnabled = true
                binding.loginBtnLogin.isEnabled = true
                binding.loginBtnRegister.isEnabled = true
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