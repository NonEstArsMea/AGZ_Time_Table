package com.NonEstArsMea.agz_time_table.present.loginLayout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.LoginLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragmentViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginFragment : Fragment() {

    private var _binding: LoginLayoutBinding? = null
    private val binding get() = _binding!!

    lateinit var vm: LoginViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.loginButton.setOnClickListener {
            vm.signIn(binding.username.text.toString(), binding.password.text.toString())

            vm.authResult.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is LoginViewModel.Result.Success -> findNavController().popBackStack()
                    is LoginViewModel.Result.Error -> Toast.makeText(
                        requireActivity(),
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}