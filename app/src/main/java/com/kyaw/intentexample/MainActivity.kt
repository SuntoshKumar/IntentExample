package com.kyaw.intentexample

import android.Manifest
import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kyaw.intentexample.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {


    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        // Handle the result of the camera intent
        if (result) {
            // The image was captured successfully
            // Use the imageUri to access the image
            binding.image.setImageURI(imageUri)
        } else {
            // The image capture failed
            // Show an error message or do something else
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    private val contactLauncher = registerForActivityResult(ActivityResultContracts.PickContact()){
        binding.textContact.text = it.toString()
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }


    private lateinit var imageUri:Uri
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        imageUri = createImageUri()

        binding.btnImage.setOnClickListener {

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                takePictureLauncher.launch(imageUri)
            }else{
                Snackbar.make(it,"Require permission",Snackbar.LENGTH_SHORT)
                    .setAction("Request"){
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }.show()
            }


        }

        binding.btnContact.setOnClickListener {

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                contactLauncher.launch(null)

            }else{

                MaterialAlertDialogBuilder(this)
                    .setTitle("Permission")
                    .setMessage("Reading your contact is required for this app!")
                    .setNegativeButton("Cancel"){dialog,_ ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Allow"){dialog,_ ->
                        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    .create()
                    .show()
            }

        }


        binding.btnPermission.setOnClickListener {
            val intent = Intent(this,PermissionActivity::class.java)
            startActivity(intent)
        }
    }


    private fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        return FileProvider.getUriForFile(this, "$packageName.fileprovider", imageFile)
    }

}