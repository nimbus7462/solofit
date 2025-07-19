package com.example.solofit.model

data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val tag: String,           // Strength, Endurance, Vitality
    val addOnTags: String,
    val difficulty: String,    // Easy, Medium, Hard
    val xpReward: Int,
    val statReward: Int,
    val isCompleted: Boolean,
    val isCancelled: Boolean,
    val icon: Int,
    val log: String             // for user logging
)