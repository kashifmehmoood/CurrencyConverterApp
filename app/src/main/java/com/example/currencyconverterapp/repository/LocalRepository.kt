package com.example.currencyconverterapp.repository

import com.example.currencyconverterapp.Interfaces.CurrencyDao
import com.example.currencyconverterapp.Interfaces.ExchangeCurrencyDao
import com.example.currencyconverterapp.entities.CurrencyExchangeRatesResponse
import com.example.currencyconverterapp.entities.CurrencyListsResponse
import javax.inject.Inject


class LocalRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val exchangecurrencyDao: ExchangeCurrencyDao
) {


    fun setCurrencyList(currencyListsResponse: CurrencyListsResponse) {
        currencyDao.insertCurrencyList(currencyListsResponse)
    }

    fun updateCurrencyList(currencyListsResponse: CurrencyListsResponse) {
        currencyDao.updateCurrencyList(currencyListsResponse)
    }

    fun getCurrencyList(): ArrayList<CurrencyListsResponse> {
        var currencyListsResponse: ArrayList<CurrencyListsResponse>
        currencyListsResponse =
            ((currencyDao.loadCurrencyList() as ArrayList<CurrencyListsResponse>?)!!)
        return currencyListsResponse

    }

    fun setExchangeCurrencyList(currencyExchangeListsResponse: CurrencyExchangeRatesResponse) {
        exchangecurrencyDao.insertExchangeCurrencyList(currencyExchangeListsResponse)
    }

    fun updateExchangeCurrencyList(currencyExchangeListsResponse: CurrencyExchangeRatesResponse) {
        exchangecurrencyDao.updateExchangeCurrencyList(currencyExchangeListsResponse)
    }

    fun getExchangeCurrencyList(): ArrayList<CurrencyExchangeRatesResponse> {
        var currencyExchangeListsResponse: ArrayList<CurrencyExchangeRatesResponse>
        currencyExchangeListsResponse = exchangecurrencyDao.loadExchangeCurrencyList()!!
        return currencyExchangeListsResponse

    }


}