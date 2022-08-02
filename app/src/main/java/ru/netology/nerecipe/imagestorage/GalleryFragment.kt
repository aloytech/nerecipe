package ru.netology.nerecipe.imagestorage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import ru.netology.nerecipe.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentGalleryBinding
import java.util.*


class GalleryFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentGalleryBinding = FragmentGalleryBinding.inflate(inflater, container, false)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        imageView = binding.imagePreview
        binding.btnChooseImage.setOnClickListener { launchGallery() }
        binding.btnUploadImage.setOnClickListener { uploadImage() }

        return binding.root
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pickImages.launch("image/*")

       //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    private val pickImages = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let { it ->
            // The image was saved into the given Uri -> do something with it
            //Picasso.get().load(it).resize(800,800).into(imageView)
            filePath = it
            imageView.setImageURI(it)
        }
    }
    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    viewModel.editServing(downloadUri.toString())
                    findNavController().navigateUp()
                    //addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

}