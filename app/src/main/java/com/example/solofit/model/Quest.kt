package com.example.solofit.model

data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val tag: String,           // Strength, Endurance, Vitality
    val addOnTags: String,
    val difficulty: String,    // Easy, Medium, Hard, Extreme
    val xpReward: Int,
    val statReward: Int, //
)