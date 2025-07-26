package com.example.solofit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.SettingsPageBinding
import com.example.solofit.model.User
import com.example.solofit.utilities.Extras

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewBinding: SettingsPageBinding
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    private var imageUri: Uri? = null
    private lateinit var currentUser: User
    private val pfpResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            imageUri = result.data!!.data

            // Persist permission to access image
            contentResolver.takePersistableUriPermission(
                imageUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // Load image into the ImageView (assumes you have an ImageView in layout)
            viewBinding.imvSettingsPfp.setImageURI(imageUri)

            // Optionally: Save image URI to the database
            val user = dbHelper.getUserById(Extras.DEFAULT_USER_ID)
            user?.pfpUri = imageUri.toString()
            dbHelper.updateUser(user!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = SettingsPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        currentUser = dbHelper.getUserById(Extras.DEFAULT_USER_ID)!!
        viewBinding.edtSettingsIgn.setText(currentUser?.username ?: "Player")
        viewBinding.imvSettingsPfp.setImageURI(currentUser.pfpUri?.let { Uri.parse(it) })


        viewBinding.btnEditIgn.setOnClickListener {
            viewBinding.edtSettingsIgn.isEnabled = true
            viewBinding.edtSettingsIgn.requestFocus()
            viewBinding.btnEditIgn.visibility = View.GONE
            viewBinding.lloHiddenRow.visibility = View.VISIBLE
        }
        viewBinding.btnChangeBack.setOnClickListener {
            viewBinding.edtSettingsIgn.isEnabled = false
            viewBinding.btnEditIgn.visibility = View.VISIBLE
            viewBinding.lloHiddenRow.visibility = View.GONE
        }
        viewBinding.btnChangeSave.setOnClickListener {
            viewBinding.edtSettingsIgn.isEnabled = false
            viewBinding.btnEditIgn.visibility = View.VISIBLE
            viewBinding.lloHiddenRow.visibility = View.GONE
            val newIgn = viewBinding.edtSettingsIgn.text
            currentUser.username = newIgn.toString()
            Log.d("USER_LOG", "Updated user: $currentUser")
            dbHelper.updateUser(currentUser)
        }

        viewBinding.btnChangePfp.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            pfpResultLauncher.launch(Intent.createChooser(intent, "Select Profile Picture"))
        }

    }

}