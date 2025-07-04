package com.example.solofit

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
    val icon: Int
)