package ru.netology.nerecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.netology.nerecipe.WelcomeFragment.Companion.logonVariant
import ru.netology.nerecipe.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentAuthBinding = FragmentAuthBinding.inflate(inflater, container, false)

        var logonVariant = -1
        arguments?.logonVariant
            ?.let {
                logonVariant = it
            }

        when (logonVariant) {
            LOGON_VARIANT_USER_EXISTS -> {
                binding.acceptButton.visibility = View.INVISIBLE
                binding.userNameInput.visibility = View.INVISIBLE
                binding.forgotButton.visibility = View.VISIBLE
                binding.logonButton.visibility = View.VISIBLE
            }
            else -> {
                binding.acceptButton.visibility = View.VISIBLE
                binding.userNameInput.visibility = View.VISIBLE
                binding.forgotButton.visibility = View.INVISIBLE
                binding.logonButton.visibility = View.INVISIBLE
            }
        }

        val auth = Firebase.auth
        var currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_authFragment_to_feedFragment)
        }

        binding.logonButton.setOnClickListener {
            if (validate(binding.userEmailInput) && validate(binding.userPasswordInput)) {
                val email = binding.userEmailInput.text.toString()
                val password = binding.userPasswordInput.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_FIREBASE, "signInWithEmail:success")
                            findNavController().navigate(R.id.action_authFragment_to_feedFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_FIREBASE, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        binding.acceptButton.setOnClickListener {
            if (validate(binding.userEmailInput) && validate(binding.userPasswordInput)) {
                val email = binding.userEmailInput.text.toString()
                val password = binding.userPasswordInput.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_FIREBASE, "createUserWithEmail:success")
                            currentUser = auth.currentUser
                            val db = Firebase.firestore
                            val newUser = User(
                                currentUser!!.uid,
                                binding.userNameInput.text.toString(),
                                null,
                                password.hashCode()
                            )
                            db.collection("users").document(currentUser!!.uid).set(newUser)


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_FIREBASE, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        return binding.root
    }

    private fun validate(editText: EditText): Boolean {
        if (editText.text.toString().isEmpty()) {
            editText.error = "Field cannot be blank"
            return false
        }
        return true
    }

}