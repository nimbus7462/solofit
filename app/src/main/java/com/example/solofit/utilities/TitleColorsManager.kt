package com.example.solofit.utilities
import android.content.Context
import com.example.solofit.R

enum class TitleColorCategory {
    BEGINNER_COLOR_CYAN, NOVICE_COLOR_GREEN, INTERMEDIATE_COLOR_ORANGE, ADVANCED_COLOR_RED, ELITE_COLOR_PURPLE, LEGEND_COLOR_GOLD
}

fun getTitleColorCategory(title: String): TitleColorCategory {
    return when (title) {
        // BLUE – 10
        "Beginner Brute", "Breather", "Rookie Defender", "D Rank Hunter" -> TitleColorCategory.BEGINNER_COLOR_CYAN

        // GREEN – 25
        "Power Lifter", "Jogger", "Hardy", "Determined", "C Rank Hunter" -> TitleColorCategory.NOVICE_COLOR_GREEN

        // ORANGE – 50
        "Heavy Hitter", "Long Runner", "Tank", "Persistent", "B Rank Hunter" -> TitleColorCategory.INTERMEDIATE_COLOR_ORANGE

        // RED – 75
        "Lionheart", "Stamina Lord", "Resilient Soul", "Tenacious", "A Rank Hunter" -> TitleColorCategory.ADVANCED_COLOR_RED
        // VIOLET – 100
        "Beast of Strength", "Iron Will", "Unbreakable Body", "Unbreakable", "S Rank Hunter", "Absolute", "Zenith" -> TitleColorCategory.ELITE_COLOR_PURPLE

        // GOLD – Special
        else -> TitleColorCategory.LEGEND_COLOR_GOLD
    }
}

fun Context.getColorForCategory(category: TitleColorCategory): Int {
    return when (category) {
        TitleColorCategory.BEGINNER_COLOR_CYAN -> getColor(R.color.cyan)
        TitleColorCategory.NOVICE_COLOR_GREEN -> getColor(R.color.bright_green)
        TitleColorCategory.INTERMEDIATE_COLOR_ORANGE -> getColor(R.color.orange)
        TitleColorCategory.ADVANCED_COLOR_RED -> getColor(R.color.bright_red)
        TitleColorCategory.ELITE_COLOR_PURPLE -> getColor(R.color.bright_purple)
        TitleColorCategory.LEGEND_COLOR_GOLD -> getColor(R.color.gold)
    }
}
