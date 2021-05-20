package com.soupdevelopers.currancyconverterdemo.main

import android.util.Log
import android.widget.TextView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soupdevelopers.currancyconverterdemo.data.models.Rates
import com.soupdevelopers.currancyconverterdemo.util.DispatcherProvider
import com.soupdevelopers.currancyconverterdemo.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(
        private val repository: MainRepository,
        private val dispatchers : DispatcherProvider
): ViewModel() {

    sealed class CurrencyEvent {

        class Sucess (val resultText : String) : CurrencyEvent()
        class Failure (val errorText : String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()

    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
            amountStr : String,
            fromCurrency : String,
            toCurrency : String
    ){
        val fromAmount = amountStr.toFloatOrNull()
        Log.e("fromAmount_log","$fromAmount")
        if(fromAmount == null){
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Sucess -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency,rates)
                    if(rate == null){
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    }else{
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Sucess(
                                "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }
}