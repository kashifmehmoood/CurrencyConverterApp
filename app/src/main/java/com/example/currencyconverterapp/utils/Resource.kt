package com.example.currencyconverterapp.utils


import com.example.currencyconverterapp.entities.CurrencyExchangeRatesResponse
import com.example.currencyconverterapp.entities.CurrencyListsResponse
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

sealed class ResponseEvent {
    class Success<T>(val responseModel: T, val parseResponse: ResponseParse) : ResponseEvent()
    class Failure(val errorText: String) : ResponseEvent()
    object Loading : ResponseEvent()
}

sealed class ResponseParse {
    object CurrenciesList : ResponseParse()
    object ExchangeCurrenciesList : ResponseParse()
}

fun ResponseEvent.parseResponse(responseParse: ResponseParse): Any? {

    return when (responseParse) {

        is ResponseParse.CurrenciesList -> {
            try {
                val currenciesListResponse = Gson().fromJson(
                    (this as ResponseEvent.Success<*>).responseModel.toString(), CurrencyListsResponse::class.java
                )
                currenciesListResponse
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        is ResponseParse.ExchangeCurrenciesList -> {
            try {
                val exchangecurrenciesListResponse = Gson().fromJson(
                    (this as ResponseEvent.Success<*>).responseModel.toString(),
                    CurrencyExchangeRatesResponse::class.java
                )
                exchangecurrenciesListResponse
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    }


}