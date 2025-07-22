package com.example.solofit.ManageQuestFragments

import com.example.solofit.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.AddEditQuestBinding
import com.example.solofit.model.Quest

class AddEditQuest : Fragment() {

    private lateinit var dbHelper: MyDatabaseHelper
    private var viewBinding: AddEditQuestBinding? = null
    private val binding get() = viewBinding!!

    private var questId: Int = -1
    private var pageHeader: String? = null
    private var questTitle: String? = null
    private var questDescription: String? = null
    private var questExtraTags: String? = null
    private var questXpReward: Int = 0
    private var questStatReward: Int = 0
    private var questType: String? = null
    private var questDifficulty: String? = null
    private lateinit var tempQuest: Quest
    private var lastActionWasSave = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = AddEditQuestArgs.fromBundle(requireArguments())
        questId = args.questId
        pageHeader = args.pageHeader
        questTitle = args.questTitle
        questDescription = args.questDescription
        questExtraTags = args.questExtraTags
        questXpReward = args.questXpReward
        questStatReward = args.questStatReward
        questType = args.questType
        questDifficulty = args.questDifficulty
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AddEditQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = MyDatabaseHelper(requireContext())

        val questTypeSpinner = binding.spnQuestType
        val questDiffSpinner = binding.spnDifficulty
        val questTypes = resources.getStringArray(R.array.quest_types)
        val questDiff = resources.getStringArray(R.array.quest_difficulties)

        val typeAdapter = ArrayAdapter(requireContext(), R.layout.selected_spinner_item, questTypes)
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        questTypeSpinner.adapter = typeAdapter

        val difficultyAdapter = ArrayAdapter(requireContext(), R.layout.selected_spinner_item, questDiff)
        difficultyAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        questDiffSpinner.adapter = difficultyAdapter

        binding.txvAddEditQuestHeader.text = pageHeader
        binding.edtQuestName.setText(questTitle)
        binding.edtExtraTags.setText(questExtraTags)
        if(questXpReward != 0) {
            binding.edtExpRewards.setText(questXpReward.toString())
        }
        if(questStatReward != 0) {
            binding.edtStatRewards.setText(questStatReward.toString())
        }
        binding.edtDescription.setText(questDescription)
        questTypeSpinner.setSelection(getSpinnerIndex(questTypeSpinner, questType ?: ""))
        questDiffSpinner.setSelection(getSpinnerIndex(questDiffSpinner, questDifficulty ?: ""))

        binding.btnSaveAddEdit.setOnClickListener {
            val quest = validateInputForQuest()
            if (quest != null) {
                lastActionWasSave = true
                tempQuest = quest
                showConfirmationMessage("Save")
            }
        }


        viewBinding!!.btnGoBack.text = getString(R.string.no)
        viewBinding!!.btnConfirm.text =  getString(R.string.yes)


        binding.btnCancelAddEdit.setOnClickListener {
            showConfirmationMessage("Cancel")
        }

        binding.btnCloseMsg.setOnClickListener {
            binding.cloErrorMessages.visibility = View.INVISIBLE
            binding.viewBackgroundBlocker.visibility = View.INVISIBLE
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showConfirmationMessage("Cancel")
                }
            })

        binding.btnConfirm.setOnClickListener {
            if (lastActionWasSave) {
                if (questId == -1) {
                    val insertedId = dbHelper.insertQuest(tempQuest)
                    if (insertedId != -1L) {
                        showToast("Quest saved!")
                        findNavController().popBackStack()
                    } else {
                        showToast("Failed to save quest")
                    }
                } else {
                    val rowsUpdated = dbHelper.updateQuest(tempQuest)
                    if (rowsUpdated > 0) {
                        showToast("Quest saved!")
                        findNavController().popBackStack()
                    } else {
                        showToast("Failed to save quest")
                    }
                }
                lastActionWasSave = false
            } else {
                findNavController().popBackStack()
            }
        }

        binding.btnGoBack.setOnClickListener {
            binding.cloConfirmation.visibility = View.INVISIBLE
            binding.viewBackgroundBlocker.visibility = View.INVISIBLE
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

    private fun validateInputForQuest() : Quest? {
        val name = binding.edtQuestName.text.toString().trim()
        val extraTags = binding.edtExtraTags.text.toString().trim()
        val xp = binding.edtExpRewards.text.toString().trim().toIntOrNull()
        val stat = binding.edtStatRewards.text.toString().trim().toIntOrNull()
        val desc = binding.edtDescription.text.toString().trim()
        val questType = binding.spnQuestType.selectedItem.toString()
        val difficulty = binding.spnDifficulty.selectedItem.toString()

        var isValid = true
        val errorMessages = mutableListOf<String>()

        if (name.isEmpty()) {
            errorMessages.add("• Quest name is required")
            isValid = false
        }

        if (xp == null || xp == 0) {
            errorMessages.add("• XP reward must be > 0")
            isValid = false
        }

        if (stat == null || stat == 0) {
            errorMessages.add("• Stat reward must be > 0")
            isValid = false
        }

        if (desc.isEmpty()) {
            errorMessages.add("• Description is required")
            isValid = false
        }

        if (questType.isBlank()) {
            errorMessages.add("• Quest type is required")
            isValid = false
        }

        if (difficulty.isBlank()) {
            errorMessages.add("• Quest difficulty is required")
            isValid = false
        }

        if (!isValid) {
            showErrorMessages(errorMessages)
        }
        return if (isValid) {
            Quest(
                id = if (questId == -1) 0 else questId,
                title = name,
                description = desc,
                tag = questType,
                addOnTags = extraTags,
                difficulty = difficulty,
                xpReward = xp!!.toInt(),
                statReward = stat!!.toInt()
            )
        } else {
            null
        }
    }

    private fun showConfirmationMessage(action: String) {
        handleConfirmationMsg()
        val actionText = if (pageHeader == "ADD QUEST") "adding new" else "editing current"
        viewBinding!!.txvConfirmationMsg.text = "$action $actionText quest?"
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessages(errors: List<String>) {
        val formattedErrors = errors.joinToString("\n") { it }

        binding.txvErrorMsgList.text = formattedErrors

        binding.cloErrorMessages.visibility = View.VISIBLE
        binding.viewBackgroundBlocker.visibility = View.VISIBLE
    }

    private fun handleConfirmationMsg() {
        binding.viewBackgroundBlocker.visibility = View.VISIBLE
        binding.cloConfirmation.visibility = View.VISIBLE
    }
}
