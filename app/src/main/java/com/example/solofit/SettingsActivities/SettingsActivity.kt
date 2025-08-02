package com.example.solofit.SettingsActivities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.PopupTitlesListBinding
import com.example.solofit.databinding.SettingsPageBinding
import com.example.solofit.model.User
import com.example.solofit.utilities.Extras
import android.widget.PopupWindow
import android.view.ViewGroup
import com.example.solofit.databinding.PopupCongratsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewBinding: SettingsPageBinding
    val dbHelper = MyDatabaseHelper.Companion.getInstance(this)!!
    private var imageUri: Uri? = null
    private lateinit var currentUser: User
    private var selectedTitle: String? = null
    private lateinit var titleAdapter: TitleGridAdapter
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
        viewBinding.edtSettingsIgn.gravity = Gravity.CENTER
        viewBinding.edtSettingsIgn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
        viewBinding.txvUserTitle.text = currentUser.selectedTitle

        viewBinding.btnEditIgn.setOnClickListener {
            enableIgnEditMode()
        }

        viewBinding.btnChangeBack.setOnClickListener {
            disableIgnEditMode()
        }

        viewBinding.btnChangeSave.setOnClickListener {
            disableIgnEditMode()
            currentUser.username = viewBinding.edtSettingsIgn.text.toString()
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

        viewBinding.btnEditTitle.setOnClickListener {
            showTitlePopup()
        }

    }
    private fun enableIgnEditMode() {
        viewBinding.edtSettingsIgn.apply {
            isEnabled = true
            requestFocus()
            background = ContextCompat.getDrawable(this@SettingsActivity, R.drawable.bg_ign)
            typeface = ResourcesCompat.getFont(this@SettingsActivity, R.font.play)
            gravity = Gravity.START
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        }
        viewBinding.btnEditIgn.visibility = View.GONE
        viewBinding.lloHiddenRow.visibility = View.VISIBLE
    }

    private fun disableIgnEditMode() {
        viewBinding.edtSettingsIgn.apply {
            isEnabled = false
            background = ContextCompat.getDrawable(this@SettingsActivity, android.R.color.transparent)
            typeface = ResourcesCompat.getFont(this@SettingsActivity, R.font.nanum_myeongjo)
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
        }
        viewBinding.btnEditIgn.visibility = View.VISIBLE
        viewBinding.lloHiddenRow.visibility = View.GONE
    }

    private fun showTitlePopup() {
        val popupBinding = PopupTitlesListBinding.inflate(LayoutInflater.from(this))
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        val unlockedTitles = currentUser.unlockedTitles.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        selectedTitle = currentUser.selectedTitle

        titleAdapter = TitleGridAdapter(unlockedTitles, selectedTitle) { clickedTitle ->
            selectedTitle = clickedTitle
        }

        popupBinding.recViewTitlesList.layoutManager = GridLayoutManager(this, 2)
        popupBinding.recViewTitlesList.adapter = titleAdapter

        popupBinding.btnGoBack.setOnClickListener {
            rootView.removeView(popupBinding.root)
        }

        popupBinding.btnSaveTitle.setOnClickListener {
            if (selectedTitle != null) {
                currentUser.selectedTitle = selectedTitle.toString()
                dbHelper.updateUser(currentUser)
                viewBinding.txvUserTitle.text = selectedTitle
                rootView.removeView(popupBinding.root)
            }
        }

    }


}