package com.soupdevelopers.currancyconverterdemo.main

import com.soupdevelopers.currancyconverterdemo.data.models.CurrencyApi
import com.soupdevelopers.currancyconverterdemo.data.models.CurrencyResponse
import com.soupdevelopers.currancyconverterdemo.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
        private val api:CurrencyApi
) : MainRepository{
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try{
            val response = api.getRates(base)
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Sucess(result)
            }else{
                Resource.Error(response.message())
            }
        }catch (e:Exception){
            Resource.Error(e.message ?: "An error occured")
        }
    }
}