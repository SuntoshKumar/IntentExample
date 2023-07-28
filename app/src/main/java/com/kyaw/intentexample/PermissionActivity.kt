package com.kyaw.intentexample

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.kyaw.intentexample.databinding.ActivityPermissionBinding
import java.security.Permissions

class PermissionActivity : AppCompatActivity() {


    val cameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){

        if (it){
            Toast.makeText(applicationContext,"Permission Granted!",Toast.LENGTH_SHORT).show()
        }
    }

    val contactlauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){

        if (it){
            Toast.makeText(applicationContext,"Permission Granted",Toast.LENGTH_SHORT).show()

        }
    }

    private lateinit var binding:ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.contactPermission.setOnClickListener {

            contactlauncher.launch(Manifest.permission.READ_CONTACTS)
        }
        binding.cameraPermission.setOnClickListener {

            cameraLauncher.launch(Manifest.permission.CAMERA)

        }

        binding.microphonePermission.setOnClickListener {

            cameraLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
}