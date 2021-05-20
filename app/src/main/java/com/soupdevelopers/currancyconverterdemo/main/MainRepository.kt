package com.soupdevelopers.currancyconverterdemo.main

import com.soupdevelopers.currancyconverterdemo.data.models.CurrencyResponse
import com.soupdevelopers.currancyconverterdemo.util.Resource

interface MainRepository {
    // main api call

    suspend fun getRates(base : String) : Resource<CurrencyResponse>
}