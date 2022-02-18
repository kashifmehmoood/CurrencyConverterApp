package com.example.currencyconverterapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconversionapp.repository.MainRepository
import com.example.currencyconverterapp.Interfaces.CurrencyDao
import com.example.currencyconverterapp.Interfaces.ExchangeCurrencyDao
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
open class BaseVM @Inject constructor(
    applicationContext: Application,
    private val repository: MainRepository,
    private val currencyDao: CurrencyDao, private val exchangeCurrencyDao: ExchangeCurrencyDao
) : AndroidViewModel(applicationContext) {

    var localRepository: LocalRepository = LocalRepository(currencyDao, exchangeCurrencyDao)
    var _currencyDataFlow = MutableSharedFlow<ResponseEvent>()
    val currencyFlow: SharedFlow<ResponseEvent> = _currencyDataFlow


    var apiResponse: ResponseEvent? = null
    lateinit var mCurrencyListDAO: CurrencyListDAO
    fun setCurrencyListDAO(mCurrencyListDAO: CurrencyListDAO) {
        this.mCurrencyListDAO = mCurrencyListDAO
    }

    interface CurrencyListDAO {
        fun onTextVeiwLayoutManager()
        fun onEditTextVeiwLayoutManager()
    }

    fun getApiRequest(
        applicationContext: Context,
        dataParams: Pair<String, MutableMap<String, String>>,
        mResponseParse: ResponseParse)
    {

        viewModelScope.launch(Dispatchers.IO) {
            _currencyDataFlow.emit(ResponseEvent.Loading)

            //Check data exist in Local Respository
            val currencyListsResponse = localRepository?.getCurrencyList()
            if (currencyListsResponse?.size!! > 0) {
                _currencyDataFlow.emit(
                    ResponseEvent.Success(
                        currencyListsResponse,
                        mResponseParse
                    )
                )
            } else {
                if (UtilMethods.isInternetConnected(applicationContext)) {
                    apiResponse = repository.getApiResponse(dataParams, mResponseParse)
                    when (apiResponse) {
                        is ResponseEvent.Failure ->
                            _currencyDataFlow.emit(ResponseEvent.Failure((apiResponse as ResponseEvent.Failure).errorText))
                        is ResponseEvent.Success<*> -> {

                            val currencyListsResponse = apiResponse?.parseResponse(mResponseParse)
                            //Save Currency List Data in DATABASE
                            saveCurrencyListDataInDatabase(currencyListsResponse)
                            _currencyDataFlow.emit(
                                ResponseEvent.Success(
                                    currencyListsResponse,
                                    mResponseParse
                                )
                            )

                        }
                    }
                } else {
                    _currencyDataFlow.emit(ResponseEvent.Failure("No Internet Connected"))
                }
            }
        }
    }

    fun onTextVeiwLayoutManager() {
        mCurrencyListDAO.onTextVeiwLayoutManager()
    }

    fun onEditTextVeiwLayoutManager() {
        mCurrencyListDAO.onEditTextVeiwLayoutManager()
    }

    fun saveCurrencyListDataInDatabase(currencyListsResponse: Any?) {
        if (localRepository?.getCurrencyList()?.size == 0) {
            //Insert Currency Data on Database
            localRepository?.setCurrencyList(currencyListsResponse as CurrencyListsResponse)
        } else {
            //For update Currency List
            localRepository?.updateCurrencyList(currencyListsResponse as CurrencyListsResponse)
        }

    }
}
