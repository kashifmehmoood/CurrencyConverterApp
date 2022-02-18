package com.example.currencyconverterapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.currencyconverterapp.utils.MapTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ExchangeCurrencyLists")
data class CurrencyExchangeRatesResponse(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "terms")
    @field:SerializedName("terms")
    val terms: String? = null,

    @ColumnInfo(name = "success")
    @field:SerializedName("success")
    val success: Boolean? = null,

    @ColumnInfo(name = "privacy")
    @field:SerializedName("privacy")
    val privacy: String? = null,

    @TypeConverters(MapTypeConverter::class)
    @field:SerializedName("quotes")
    val exchange_rates_currencies: Map<String, String>? = null
)
