package com.example.solofit.ManageQuestFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.FragmentManageQuestBinding
import com.example.solofit.databinding.PopupLegendBinding
import com.example.solofit.model.Quest


class ManageQuest : Fragment() {

    companion object {
        const val YES = "Yes"
        const val NO = "No"
    }

    private var viewBinding: FragmentManageQuestBinding? = null
    private val binding get() = viewBinding!!

    private lateinit var adapter: ManageQuestAdapter
    private lateinit var dbHelper: MyDatabaseHelper

    private var originalQuestList: List<Quest> = emptyList()
    private var questPendingDelete: Quest? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentManageQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbHelper = MyDatabaseHelper(requireContext())
        super.onViewCreated(view, savedInstanceState)

        originalQuestList = dbHelper.getAllQuests()
        binding.imbLegend.setOnClickListener {
            showDifficultyLegendPopup()
        }
        adapter = ManageQuestAdapter(
            originalQuestList.toMutableList(),
            onItemClick = { quest ->
                val action = ManageQuestDirections.actionManageQuestToAddEditQuest(
                    quest.id,
                    "EDIT QUEST",
                    quest.questName,
                    quest.description,
                    quest.questType,
                    quest.difficulty,
                    quest.extraTags,
                    quest.xpReward,
                    quest.statReward
                )
                findNavController().navigate(action)
            },
            onDeleteClick = { quest ->
                questPendingDelete = quest

                binding.txvConfirmationMsg.text = "Are you sure you want to delete ${quest.questName}? All user records with this quest will be deleted as well."
                binding.btnGoBack.text = NO
                binding.btnConfirm.text = YES
                binding.viewBackgroundBlocker.visibility = View.VISIBLE
                binding.cloConfirmation.visibility = View.VISIBLE

                binding.btnGoBack.setOnClickListener {
                    binding.cloConfirmation.visibility = View.INVISIBLE
                    binding.viewBackgroundBlocker.visibility = View.INVISIBLE
                    questPendingDelete = null
                }

                binding.btnConfirm.setOnClickListener {
                    questPendingDelete?.let {
                        dbHelper.deleteQuestAndMarkUQAs(it.id)
                        originalQuestList = dbHelper.getAllQuests()
                        applySortAndFilter()
                    }


                    binding.cloConfirmation.visibility = View.INVISIBLE
                    binding.viewBackgroundBlocker.visibility = View.INVISIBLE
                    questPendingDelete = null
                }
            }
        )

        binding.recViewManageQuest.layoutManager = LinearLayoutManager(requireContext())
        binding.recViewManageQuest.adapter = adapter

        binding.ibAddQuest.setOnClickListener {
            val action = ManageQuestDirections
                .actionManageQuestToAddEditQuest(-1, "ADD QUEST", "", "", "", "", "", 0, 0)
            findNavController().navigate(action)
        }

        setupSpinners()
    }

    private fun setupSpinners() {
        val sortOptions = listOf("Easiest First", "Hardest First")
        val filterOptions = listOf("All", "Easy", "Normal", "Hard", "Extreme", "Strength", "Endurance", "Vitality")

        val sortAdapter = ArrayAdapter(requireContext(), R.layout.spinner_selected_blank, sortOptions)
        sortAdapter.setDropDownViewResource(R.layout.spinner_item_white)
        binding.spinnerSort.adapter = sortAdapter

        val filterAdapter = ArrayAdapter(requireContext(), R.layout.spinner_selected_blank, filterOptions)
        filterAdapter.setDropDownViewResource(R.layout.spinner_item_white)
        binding.spinnerFilter.adapter = filterAdapter

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                applySortAndFilter()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                applySortAndFilter()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    private fun applySortAndFilter() {
        val selectedSort = binding.spinnerSort.selectedItem.toString()
        val selectedFilter = binding.spinnerFilter.selectedItem.toString()

        var filteredList = originalQuestList

        // Apply filter
        filteredList = when (selectedFilter) {
            "Easy", "Normal", "Hard", "Extreme" -> {
                filteredList.filter { it.difficulty.equals(selectedFilter, ignoreCase = true) }
            }
            "Strength", "Endurance", "Vitality" -> {
                filteredList.filter { it.questType.equals(selectedFilter, ignoreCase = true) }
            }
            else -> filteredList // "All"
        }

        // Apply sort
        val difficultyOrder = listOf("Easy", "Normal", "Hard", "Extreme")
        filteredList = when (selectedSort) {
            "Easiest First" -> {
                filteredList.sortedBy { difficultyOrder.indexOf(it.difficulty) }
            }
            "Hardest First" -> {
                filteredList.sortedByDescending { difficultyOrder.indexOf(it.difficulty) }
            }
            else -> filteredList
        }

        adapter.updateList(filteredList)
    }

    override fun onResume() {
        super.onResume()
        originalQuestList = dbHelper.getAllQuests()
        applySortAndFilter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }


    private fun showDifficultyLegendPopup() {
        val popupBinding = PopupLegendBinding.inflate(layoutInflater)
        val rootView = requireActivity().findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        popupBinding.btnGoBack.setOnClickListener {
            rootView.removeView(popupBinding.root)
        }
    }
}
