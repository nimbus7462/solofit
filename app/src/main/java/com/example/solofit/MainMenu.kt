package com.example.solofit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.solofit.databinding.MainMenuBinding

class MainMenu : Fragment() {

    private var _binding: MainMenuBinding? = null
    private val binding get() = _binding!!  // Safe access

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe to access binding here!
        binding.manageQuestBtnImg.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_manage_quest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
