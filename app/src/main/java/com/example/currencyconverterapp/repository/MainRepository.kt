package com.example.currencyconversionapp.repository
import com.example.currencyconverterapp.utils.ResponseEvent
import com.example.currencyconverterapp.utils.ResponseParse

interface MainRepository {
    suspend fun getApiResponse(dataParams : kotlin.Pair<String, Map<String, String>>, parse: ResponseParse): ResponseEvent
    //suspend fun postApiRequest(dataParams : kotlin.Pair<String,Map<String, String>>,parse: ResponseParse): ResponseEvent
}