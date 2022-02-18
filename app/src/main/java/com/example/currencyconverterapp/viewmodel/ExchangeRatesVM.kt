package com.example.currencyconversionapp.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconversionapp.repository.MainRepository
import com.example.currencyconverterapp.Interfaces.CurrencyDao
import com.example.currencyconverterapp.Interfaces.ExchangeCurrencyDao
import com.example.currencyconverterapp.entities.CurrencyExchangeRatesResponse
import com.example.currencyconverterapp.entities.CurrencyListsResponse
import com.example.currencyconverterapp.repository.LocalRepository
import com.example.currencyconverterapp.utils.ResponseEvent
import com.example.currencyconverterapp.utils.ResponseParse
import com.example.currencyconverterapp.utils.UtilMethods
import com.example.currencyconverterapp.utils.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class ExchangeRatesVM @Inject constructor(
    applicationContext: Application,
    private val repository: MainRepository,
    private val exchangeCurrencyDao: ExchangeCurrencyDao,
    private val currencyDao: CurrencyDao
) : AndroidViewModel(applicationContext) {
    var localRepository: LocalRepository = LocalRepository(currencyDao, exchangeCurrencyDao)

    var _currencyRatesDataFlow = MutableSharedFlow<ResponseEvent>()
    val currencyRatesFlow: SharedFlow<ResponseEvent> = _currencyRatesDataFlow
    var apiResponse: ResponseEvent? = null


    fun getApiRequest(
        applicationContext: Context,
        dataParams: Pair<String, MutableMap<String, String>>,
        mResponseParse: ResponseParse
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            _currencyRatesDataFlow.emit(ResponseEvent.Loading)

            //Check data exist in Local Respository
            val exchnageCurrencyListsResponse = localRepository?.getExchangeCurrencyList()
            if (exchnageCurrencyListsResponse?.size!! > 0) {
                _currencyRatesDataFlow.emit(
                    ResponseEvent.Success(
                        exchnageCurrencyListsResponse,
                        mResponseParse
                    )
                )
            } else {
                if (UtilMethods.isInternetConnected(applicationContext)) {
                    apiResponse = repository.getApiResponse(dataParams, mResponseParse)
                    when (apiResponse) {
                        is ResponseEvent.Failure ->
                            _currencyRatesDataFlow.emit(ResponseEvent.Failure((apiResponse as ResponseEvent.Failure).errorText))
                        is ResponseEvent.Success<*> -> {
                            val exchangecurrencyListsResponse =
                                apiResponse?.parseResponse(mResponseParse)
                            _currencyRatesDataFlow.emit(
                                ResponseEvent.Success(
                                    exchangecurrencyListsResponse,
                                    mResponseParse
                                )
                            )
                            //Insert Data in Local Database
                            saveExchangeCurrencyListDataInDatabase(exchangecurrencyListsResponse)
                        }
                    }
                } else {
                    _currencyRatesDataFlow.emit(ResponseEvent.Failure("No Internet Connected"))

                }
            }


        }
    }

    fun saveExchangeCurrencyListDataInDatabase(currencyExchangeListsResponse: Any?) {
        if (localRepository?.getExchangeCurrencyList()?.size == 0) {
            //Insert ExchangeCurrency Data on Database
            localRepository?.setExchangeCurrencyList(currencyExchangeListsResponse as CurrencyExchangeRatesResponse)
        } else {
            //For update Currency List
            localRepository?.updateExchangeCurrencyList(currencyExchangeListsResponse as CurrencyExchangeRatesResponse)
        }

    }

}
