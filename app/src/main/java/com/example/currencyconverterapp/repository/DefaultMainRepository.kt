package com.example.currencyconverterapp.repository

import android.util.Pair
import com.example.currencyconversionapp.repository.MainRepository
import com.example.currencyconverterapp.Interfaces.CurrencyDao
import com.example.currencyconverterapp.utils.ResponseEvent
import com.example.currencyconverterapp.utils.ResponseParse
import javax.inject.Inject


class DefaultMainRepository @Inject constructor(private val webApi: WebApiMethods) :
    MainRepository {

    override suspend fun getApiResponse(
        dataParams: kotlin.Pair<String, Map<String, String>>,
        mResponseParse: ResponseParse
    ): ResponseEvent {

        return try {
            val response = webApi.getApiResponseAsync(dataParams.first, dataParams.second)
            val result = response.body()?.string()
            if (response.isSuccessful && result != null) {
                ResponseEvent.Success(result, mResponseParse)
            } else {
                ResponseEvent.Failure("An Error occurred")
            }
        } catch (e: Exception) {
            ResponseEvent.Failure("An $e occurred")
        }
    }


}