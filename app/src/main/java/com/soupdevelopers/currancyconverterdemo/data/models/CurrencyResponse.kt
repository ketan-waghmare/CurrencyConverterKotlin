package com.soupdevelopers.currancyconverterdemo.data.models

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)