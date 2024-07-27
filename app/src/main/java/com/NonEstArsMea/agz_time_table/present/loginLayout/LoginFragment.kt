package com.NonEstArsMea.agz_time_table.present.loginLayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.NonEstArsMea.agz_time_table.databinding.LoginLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: LoginLayoutBinding? = null
    private val binding get() = _binding!!

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
            if (binding.username.text.toString().isNotEmpty() && binding.password.text.toString()
                    .isNotEmpty()
            ) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(requireActivity(), "ЗБС", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Поля не заполнены", Toast.LENGTH_SHORT).show()
            }
        }
    }
}