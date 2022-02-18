package com.example.currencyconverterapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.currencyconverterapp.Interfaces.CurrencyDao
import com.example.currencyconverterapp.Interfaces.ExchangeCurrencyDao
import com.example.currencyconverterapp.entities.CurrencyExchangeRatesResponse
import com.example.currencyconverterapp.entities.CurrencyListsResponse
import com.example.currencyconverterapp.utils.MapTypeConverter

@Database(
    entities = [CurrencyListsResponse::class, CurrencyExchangeRatesResponse::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MapTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangeCurrencyDao(): ExchangeCurrencyDao

    companion object {
        private val LOG_TAG = AppDatabase::class.java.simpleName
        private val LOCK = Any()
        private const val DATABASE_NAME = "SaveCurrencyRecord"
        private var sInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                )
                    .build()

            }
            return sInstance as AppDatabase
        }

    }
}