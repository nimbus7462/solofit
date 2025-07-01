package com.example.solofit

data class Quest(
    val title: String,
    val description: String,
    val tag: String,           // Strength, Endurance, Vitality
    val difficulty: String,    // Easy, Medium, Hard
    val xpReward: Int,
    val isCompleted: Boolean,
    val isCancelled: Boolean,
    val icon: Int
)