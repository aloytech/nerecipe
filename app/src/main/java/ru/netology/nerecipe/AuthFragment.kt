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
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import ru.netology.nerecipe.databinding.FragmentAuthBinding
import ru.netology.nerecipe.imagestorage.IntId

class AuthFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentAuthBinding = FragmentAuthBinding.inflate(inflater, container, false)


        var auth = Firebase.auth

        var currentUser = auth.currentUser
        //if(currentUser != null){
        //  authUserId = currentUser.uid
        //  findNavController().navigate(R.id.action_authFragment_to_feedFragment)
        //}

        binding.acceptButton.setOnClickListener {
            if (validate(binding.userEmailInput) && validate(binding.userPasswordInput)) {
                val email = binding.userEmailInput.text.toString()
                val password = binding.userPasswordInput.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_FIREBASE, "createUserWithEmail:success")
                            currentUser = auth.currentUser

                            val db = Firebase.firestore

                            var intId = IntId(3)
                            db.collection("fbsummary").document("lastuserid").set(intId)
                            db.collection("fbsummary")
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        when (document.id){
                                            LAST_USER_ID ->{
                                                Log.d(TAG_FIREBASE, "last user id: ${document.data}")
                                                val lastIdJson = Gson().toJson(document.data)
                                                intId = Gson().fromJson(lastIdJson, IntId::class.java)
                                            }
                                        }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG_FIREBASE, "get failed with ", exception)
                                }

                            intId = intId.copy(current = intId.current + 1)
                            db.collection(FB_SUMMARY).document(LAST_USER_ID).set(intId)

                            val newUser = User(
                                intId.current,
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