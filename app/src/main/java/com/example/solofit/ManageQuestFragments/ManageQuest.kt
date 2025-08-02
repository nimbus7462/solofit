package com.example.solofit.ManageQuestFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.FragmentManageQuestBinding
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

    // Store the quest pending deletion (to know which quest to delete when confirmed)
    private var questPendingDelete: git addQuest? = null

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

        val questList = dbHelper.getAllQuests()

        adapter = ManageQuestAdapter(
            questList.toMutableList(),
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
                // Save quest for deletion on confirmation
                questPendingDelete = quest

                binding.txvConfirmationMsg.text = "Are you sure you want to delete ${quest.questName}?"
                binding.btnGoBack.text = NO
                binding.btnConfirm.text = YES
                binding.viewBackgroundBlocker.visibility = View.VISIBLE
                binding.cloConfirmation.visibility = View.VISIBLE

                // Cancel button - hide confirmation dialog
                binding.btnGoBack.setOnClickListener {
                    binding.cloConfirmation.visibility = View.INVISIBLE
                    binding.viewBackgroundBlocker.visibility = View.INVISIBLE
                    questPendingDelete = null
                }

                // Confirm delete button
                binding.btnConfirm.setOnClickListener {
                    questPendingDelete?.let {
                        dbHelper.deleteQuest(it.id)
                        val updatedList = dbHelper.getAllQuests()
                        adapter.updateList(updatedList)
                    }

                    binding.cloConfirmation.visibility = View.INVISIBLE
                    binding.viewBackgroundBlocker.visibility = View.INVISIBLE
                    questPendingDelete = null
                }
            }
        )

        binding.recViewManageQuest.layoutManager = LinearLayoutManager(requireContext())
        binding.recViewManageQuest.adapter = adapter

        binding.fabAddQuest.setOnClickListener {
            val action = ManageQuestDirections
                .actionManageQuestToAddEditQuest(-1, "ADD QUEST", "", "", "", "", "", 0, 0)
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        val updatedList = dbHelper.getAllQuests()
        adapter.updateList(updatedList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}
