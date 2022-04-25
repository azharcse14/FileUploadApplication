package com.azhar.fileuploadapplication.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.azhar.fileuploadapplication.R
import com.azhar.fileuploadapplication.databinding.ActivityMainBinding
import com.azhar.fileuploadapplication.model.repository.Repository
import com.azhar.fileuploadapplication.view.utils.UploadRequestBody
import com.azhar.fileuploadapplication.view.utils.getFileName
import com.azhar.fileuploadapplication.view.utils.snackbar
import com.azhar.fileuploadapplication.viewModel.MainViewModel
import com.azhar.fileuploadapplication.viewModel.MainViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            openImageChooser()
        }

        binding.buttonUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image",this)

        val mainViewModelFactory = MainViewModelFactory(Repository())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        mainViewModel.fileUpload(
            MultipartBody.Part.createFormData(
            "image",
            file.name,
            body
        ), RequestBody.create(MediaType.parse("multipart/form-data"), "json"))


        mainViewModel.uploadResponse.observe(this, Observer {
            binding.layoutRoot.snackbar(it.RespMsg)
            binding.progressBar.progress = 100

        })

    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }

}