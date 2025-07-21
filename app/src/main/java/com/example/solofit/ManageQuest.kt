package com.example.solofit

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

    private var _binding: FragmentManageQuestBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ManageQuestAdapter
    private lateinit var dbHelper: MyDatabaseHelper  // ✅ Use your DB helper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dbHelper = MyDatabaseHelper(requireContext())

        // ✅ Load quests from DB instead of com.example.solofit.datahelpers.QuestDataHelper
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

        binding.questRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questRecyclerView.adapter = adapter

        binding.btnAddQuest.setOnClickListener {
            val action = ManageQuestDirections
                .actionManageQuestToAddEditQuest(-1, "EDIT QUEST", "", "", "", "", "", 0, 0)
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
        _binding = null
    }
}
