package com.example.currencyconversionapp.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconversionapp.repository.MainRepository
import com.example.currencyconverterapp.Database.AppDatabase
import com.example.currencyconverterapp.MainApplication
import com.example.currencyconverterapp.repository.DefaultMainRepository
import com.example.currencyconverterapp.repository.LocalRepository
import com.example.currencyconverterapp.repository.WebApiMethods
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideServicesRestApi(): WebApiMethods {
        return MainApplication.getInstance().networkController.initAPIService(WebApiMethods::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindNetworkRepository(
        networkRepositoryImp: DefaultMainRepository
    ): MainRepository


    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "SaveCurrencyRecord"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideYourCurrencyDao(db: AppDatabase) = db.currencyDao()

    @Singleton
    @Provides
    fun provideYourExchangeCurrencyDao(db: AppDatabase) = db.exchangeCurrencyDao()

}
