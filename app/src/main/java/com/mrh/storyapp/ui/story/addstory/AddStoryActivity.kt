package com.mrh.storyapp.ui.story.addstory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mrh.storyapp.databinding.ActivityAddStoryBinding
import com.mrh.storyapp.ui.story.MainActivity
import com.mrh.storyapp.utils.Utils
import com.mrh.storyapp.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var utils: Utils
    private var getFile: File? = null
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var fusedLocattion: FusedLocationProviderClient

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                utils.rotateFile(file, true)
                getFile = file
                binding.previewImg.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = utils.uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.previewImg.setImageURI(uri)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val MAXIMAL_SIZE = 1000000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utils = Utils()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add New Story"
        fusedLocattion = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this@AddStoryActivity,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        utils.createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.mrh.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    @SuppressLint("MissingPermission")
    private fun uploadImage() {
        showLoading(true)
        if (!binding.cbGetLocation.isChecked) {
            if (getFile == null) {
                showLoading(false)
                Toast.makeText(
                    this@AddStoryActivity,
                    "Silahkan pilih gambar terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.edAddDescription.text.isNullOrEmpty()) {
                showLoading(false)
                Toast.makeText(this@AddStoryActivity, "Deskripsi wajib di isi", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val file = reduceFileImage(getFile as File)

                val description = binding.edAddDescription.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                addStoryViewModel.addNewStory(imageMultiPart, description, 1.0, 1.0).observe(this) {
                    if (it != null) {
                        when (it) {
                            is com.mrh.storyapp.data.Result.Success -> {
                                showLoading(false)
                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(
                                    applicationContext,
                                    it.data.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is com.mrh.storyapp.data.Result.Loading -> {
                                showLoading(true)
                            }
                            is com.mrh.storyapp.data.Result.Error -> {
                                showLoading(false)
                                Toast.makeText(applicationContext, it.error, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        } else {
            if (getFile == null) {
                showLoading(false)
                Toast.makeText(
                    this@AddStoryActivity,
                    "Silahkan pilih gambar terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.edAddDescription.text.isNullOrEmpty()) {
                showLoading(false)
                Toast.makeText(this@AddStoryActivity, "Deskripsi wajib di isi", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val file = reduceFileImage(getFile as File)

                val description = binding.edAddDescription.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                fusedLocattion.lastLocation.addOnSuccessListener { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    addStoryViewModel.addNewStory(imageMultiPart, description, latitude, longitude)
                        .observe(this) {
                            if (it != null) {
                                when (it) {
                                    is com.mrh.storyapp.data.Result.Success -> {
                                        showLoading(false)
                                        val intent =
                                            Intent(this@AddStoryActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        Toast.makeText(
                                            applicationContext,
                                            it.data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    is com.mrh.storyapp.data.Result.Loading -> {
                                        showLoading(true)
                                    }
                                    is com.mrh.storyapp.data.Result.Error -> {
                                        showLoading(false)
                                        Toast.makeText(
                                            applicationContext,
                                            it.error,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}