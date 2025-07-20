package com.example.solofit.model

data class Quote(
    val quoteID: Int,           // Primary key
    val quoteText: String,
    val dateSaved: String       // Format: "YYYY-MM-DD"
)
