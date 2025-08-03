package com.example.solofit.ManageQuestFragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.NavhostManageQuestBinding

class NavHostManageQuest : AppCompatActivity() {
    private lateinit var viewBinding: NavhostManageQuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = NavhostManageQuestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }
}