package com.example.solofit.model

data class User(
    val level: Int,
    val xpTotal: Int,
    val xpThreshold: Int, // Threshold, increments by 100?
    val streak: Int,
    val strengthPoints: Int,
    val endurancePoints: Int,
    val vitalityPoints: Int,
    val completedQuestsTotal: ArrayList<Quest>
)