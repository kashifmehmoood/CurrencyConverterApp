package com.example.currencyconverterapp
import android.app.Application
import com.example.currencyconverterapp.repository.NetworkController
import com.example.currencyconverterapp.utils.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){

    lateinit var networkController: NetworkController
    override fun onCreate() {
        super.onCreate()
        networkController = NetworkController(this, Constants.BASE_URL)
    }
    companion object {
        private var instance: MainApplication? = null
        fun getInstance(): MainApplication {
            return instance!!
        }
    }

    init {
        instance = this
    }
}