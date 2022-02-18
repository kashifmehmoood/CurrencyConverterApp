package com.example.currencyconverterapp.Interfaces

import androidx.room.*
import com.example.currencyconverterapp.entities.CurrencyListsResponse

@Dao
interface CurrencyDao {

    @Insert
    fun insertCurrencyList(currencyListsResponse: CurrencyListsResponse)


    @Update
    fun updateCurrencyList(currencyListsResponse: CurrencyListsResponse)

    @Query("SELECT * FROM CurrencyLists")
    fun loadCurrencyList(): List<CurrencyListsResponse>?
}