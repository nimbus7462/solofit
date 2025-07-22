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

class ManageQuest : Fragment() {


    private var viewBinding: FragmentManageQuestBinding? = null
    private val binding get() = viewBinding!!

    private lateinit var adapter: ManageQuestAdapter
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentManageQuestBinding.inflate(inflater, container, false)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbHelper = MyDatabaseHelper(requireContext())
        super.onViewCreated(view, savedInstanceState)

        val questList = dbHelper.getAllQuests()

        adapter = ManageQuestAdapter(questList.toMutableList(),
            onItemClick = { quest ->
                val action = ManageQuestDirections.actionManageQuestToAddEditQuest(
                    quest.id,
                    "EDIT QUEST",
                    quest.title,
                    quest.description,
                    quest.tag,
                    quest.difficulty,
                    quest.addOnTags,
                    quest.xpReward,
                    quest.statReward
                )
                findNavController().navigate(action)
            },
            onDeleteClick = { quest ->
                dbHelper.deleteQuest(quest.id)
            }
        )

        binding.recViewManageQuest.layoutManager = LinearLayoutManager(requireContext())
        binding.recViewManageQuest.adapter = adapter

        binding.btnAddQuest.setOnClickListener {
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

