package com.example.currencyconverterapp.Interfaces

import androidx.room.*
import com.example.currencyconverterapp.entities.CurrencyExchangeRatesResponse
import com.example.currencyconverterapp.entities.CurrencyListsResponse

@Dao
interface ExchangeCurrencyDao {

    @Insert
    fun insertExchangeCurrencyList(currencyExchangeRatesResponse: CurrencyExchangeRatesResponse)


    @Update
    fun updateExchangeCurrencyList(currencyExchangeRatesResponse: CurrencyExchangeRatesResponse)

    @Query("SELECT * FROM ExchangeCurrencyLists")
    fun loadExchangeCurrencyList(): ArrayList<CurrencyExchangeRatesResponse>?
}