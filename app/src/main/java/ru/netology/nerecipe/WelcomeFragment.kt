package ru.netology.nerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    companion object {
        var Bundle.logonVariant: Int
            set(value) = putInt(LOGON_VARIANT, value)
            get() = getInt(LOGON_VARIANT)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentWelcomeBinding =
            FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.logonButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_authFragment,
            Bundle().apply
             { logonVariant = LOGON_VARIANT_USER_EXISTS })
        }
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_authFragment,
                Bundle().apply
                { logonVariant = LOGON_VARIANT_REGISTRATION })
        }

        return binding.root
    }
}