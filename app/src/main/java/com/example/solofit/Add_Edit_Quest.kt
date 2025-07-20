package com.example.solofit
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.solofit.model.Quest

class Add_Edit_Quest : Fragment() {

    private val args: Add_Edit_QuestArgs by navArgs() // ✅ Safe Args
    private lateinit var dbHelper: MyDatabaseHelper    // ✅ DB helper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_edit_quest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = MyDatabaseHelper(requireContext()) // ✅ Init DB helper

        val questTypeSpinner = view.findViewById<Spinner>(R.id.spnQuestType)
        val questDiffSpinner = view.findViewById<Spinner>(R.id.spnDifficulty)
        val questTypes = resources.getStringArray(R.array.quest_types)
        val questDiff = resources.getStringArray(R.array.quest_difficulties)

        val typeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            questTypes
        )
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        questTypeSpinner.adapter = typeAdapter

        val difficultyAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            questDiff
        )
        difficultyAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        questDiffSpinner.adapter = difficultyAdapter

        // ✅ Get references to EditTexts and buttons
        val edtQuestHeader = view.findViewById<EditText>(R.id.edtQuestsTitle)
        val edtQuestName = view.findViewById<EditText>(R.id.edtQuestName)
        val edtQuestTags = view.findViewById<EditText>(R.id.edtExtraTags)
        val edtExpReward = view.findViewById<EditText>(R.id.edtExpRewards)
        val edtStatReward = view.findViewById<EditText>(R.id.etdStatExpRewards)
        val edtQuestDesc = view.findViewById<EditText>(R.id.edtDescription)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnGoBack = view.findViewById<Button>(R.id.btnGoBack)
        val confirmationPanel = view.findViewById<ConstraintLayout>(R.id.clConfirmation)

        // ✅ Populate fields if editing
        edtQuestHeader.setText(args.questTitle)
        edtQuestName.setText(args.questName)
        edtQuestDesc.setText(args.questDescription)
        edtQuestTags.setText(args.questTags)
        edtExpReward.setText(args.questXpReward.toString())
        edtStatReward.setText(args.questStatReward.toString())
        questTypeSpinner.setSelection(getSpinnerIndex(questTypeSpinner, args.questType))
        questDiffSpinner.setSelection(getSpinnerIndex(questDiffSpinner, args.questDifficulty))


        val btnSave = view.findViewById<View>(R.id.btnSaveAdd)
        val btnCancel = view.findViewById<View>(R.id.btnCancelAdd)

        btnSave.setOnClickListener {
            handleSaveButton(
                edtQuestName,
                edtQuestTags,
                edtExpReward,
                edtStatReward,
                edtQuestDesc,
                questTypeSpinner,
                questDiffSpinner
            )

        }

        btnCancel.setOnClickListener {
            handleCancelButton()
        }

        // Handle Android back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleCancelButton()
                }
            })
        btnYes.setOnClickListener {
            findNavController().popBackStack()
        }
        btnGoBack.setOnClickListener {
            confirmationPanel?.visibility = View.INVISIBLE
        }


    }
    private fun getSpinnerIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(value, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    private fun handleSaveButton(
        edtQuestName: EditText,
        edtQuestTags: EditText,
        edtExpReward: EditText,
        edtStatReward: EditText,
        edtQuestDesc: EditText,
        questTypeSpinner: Spinner,
        questDiffSpinner:Spinner
    ) {
        val name = edtQuestName.text.toString().trim()
        val tags = edtQuestTags.text.toString().trim()
        val xp = edtExpReward.text.toString().trim()
        val stat = edtStatReward.text.toString().trim()
        val desc = edtQuestDesc.text.toString().trim()
        val type = questTypeSpinner.selectedItem.toString()
        val diff = questDiffSpinner.selectedItem.toString()
        var save = true

        when {
            name.isEmpty() -> {
                edtQuestName.error = "Required"
                showToast("Quest name is required")
                save = false
            }
            tags.isEmpty() -> {
                edtQuestTags.error = "Required"
                showToast("Tags are required")
                save = false
            }
            xp.isEmpty() -> {
                edtExpReward.error = "Required"
                showToast("XP reward is required")
                save = false
            }
            stat.isEmpty() -> {
                edtStatReward.error = "Required"
                showToast("Stat reward is required")
                save = false
            }
            desc.isEmpty() -> {
                edtQuestDesc.error = "Required"
                showToast("Description is required")
                save = false
            }
        }

        val exp = xp.toIntOrNull()
        val statVal = stat.toIntOrNull()

        if (exp == null || exp == 0) {
            edtExpReward.error = "Must be a valid number > 0"
            showToast("XP reward must be a valid number")
            save = false
        }

        if (statVal == null || statVal == 0) {
            edtStatReward.error = "Must be a valid number > 0"
            showToast("Stat reward must be a valid number")
            save = false
        }

        if (save) {
            val temp_icon = when (type) {
                "Strength" -> R.drawable.dumbell_icon
                "Vitality" -> R.drawable.meditate
                else -> R.drawable.footprint
            }

            val quest = Quest(
                id = if (args.questId == -1) 0 else args.questId, // 0 = AUTO_INCREMENT
                title = name,
                description = desc,
                tag = type,
                addOnTags = tags,
                difficulty = diff,
                xpReward = exp!!,
                statReward = statVal!!,
            )

            val success = if (args.questId == -1) {
                dbHelper.addQuest(quest)
            } else {
                dbHelper.updateQuest(quest)
            }

            if (success) {
                showToast("Quest saved!")
                findNavController().popBackStack()
            } else {
                showToast("Failed to save quest")
            }
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun handleCancelButton() {
        val confirmationPanel = view?.findViewById<ConstraintLayout>(R.id.clConfirmation)
        confirmationPanel?.visibility = View.VISIBLE
    }


}
