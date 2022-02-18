package com.example.currencyconverterapp.view

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.ListPopupWindow
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.currencyconversionapp.viewmodel.ExchangeRatesVM
import com.example.currencyconverterapp.Adapter.DisplayConvertedCurrencyListAdapter
import com.example.currencyconverterapp.Adapter.ListPopupWindowAdapter
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.databinding.ActivityMainBinding
import com.example.currencyconverterapp.entities.CurrencyCard
import com.example.currencyconverterapp.entities.CurrencyExchangeRatesResponse
import com.example.currencyconverterapp.entities.CurrencyListsResponse
import com.example.currencyconverterapp.utils.Constants
import com.example.currencyconverterapp.utils.ResponseEvent
import com.example.currencyconverterapp.utils.ResponseParse
import com.example.currencyconverterapp.viewmodel.BaseVM
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.HashMap


@AndroidEntryPoint
class MainActivity() : BaseActivity(), BaseVM.CurrencyListDAO {
    private var currency_itemsList: Map<String, String>? = null
    private var Exchangecurrency_itemsList: Map<String, String>? = null
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var DisplayConvertedCurrencyListAdapter: DisplayConvertedCurrencyListAdapter
    private val mBaseViewModel: BaseVM by viewModels()
    private val mExchangeRatesVM: ExchangeRatesVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.mCurrencyViewModel = mBaseViewModel
        mBaseViewModel.setCurrencyListDAO(this@MainActivity)
        //Call currency method Either From Remote or Local DB
        getCurrenciesList()
        lifecycleScope.launchWhenStarted {
            custom_progress.showProgress()
            mBaseViewModel.currencyFlow.collect { value: ResponseEvent ->

                when (value) {
                    is ResponseEvent.Success<*> -> {
                        custom_progress.hideProgress()
                        currency_itemsList =
                            (value.responseModel as CurrencyListsResponse).currencies!!
                    }

                    is ResponseEvent.Failure -> {
                        custom_progress.hideProgress()
                        onFailure(value.errorText)

                    }

                    is ResponseEvent.Loading -> {
                        custom_progress.showProgress()

                    }

                }
            }
            mExchangeRatesVM.currencyRatesFlow.collect { value: ResponseEvent ->

                when (value) {
                    is ResponseEvent.Success<*> -> {
                        custom_progress.hideProgress()
                        currency_itemsList =
                            (value.responseModel as CurrencyListsResponse).currencies!!
                        getExchangeRatesList()
                    }

                    is ResponseEvent.Failure -> {
                        custom_progress.hideProgress()
                        onFailure(value.errorText)

                    }

                    is ResponseEvent.Loading -> {
                        custom_progress.showProgress()

                    }

                }
            }

        }

        // getExchangeRatesList()
    }

    private fun getCurrenciesList() {
        val dataParams: MutableMap<String, String> = HashMap()
        dataParams["access_key"] = Constants.accessKey
        val data = Pair(Constants.BASE_URL + Constants.Currency_List_END_POINT, dataParams)
        mBaseViewModel.getApiRequest(applicationContext, data, ResponseParse.CurrenciesList)


    }

    private fun getExchangeRatesList() {
        val dataParams: MutableMap<String, String> = HashMap()
        dataParams["access_key"] = Constants.accessKey
        val data = Pair(Constants.BASE_URL + Constants.Exchange_Rates_END_POINT, dataParams)
        mExchangeRatesVM.getApiRequest(
            applicationContext,
            data,
            ResponseParse.ExchangeCurrenciesList
        )
        lifecycleScope.launchWhenStarted {

            mExchangeRatesVM.currencyRatesFlow.collect { value: ResponseEvent ->

                when (value) {
                    is ResponseEvent.Success<*> -> {
                        custom_progress.hideProgress()
                        Exchangecurrency_itemsList =
                            (value.responseModel as CurrencyExchangeRatesResponse).exchange_rates_currencies!!
                    }

                    is ResponseEvent.Failure -> {

                        custom_progress.hideProgress()
                        onFailure(value.errorText)

                    }

                    is ResponseEvent.Loading -> {

                        custom_progress.showProgress()
                        //consultationBinding.rvPathwayList.visibility = View.GONE

                    }

                }
            }


        }
    }

    private fun onFailure(message: String) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
    }

    // I have used https://api.currencylayer.com/live this end point because i can't use others end point on free account
    //By getting exchange rate from above api i have performed calculation on app side.
    private fun calculate_currency(
        data: Map<String, String>?,
        selected_currency: String,
        userinput: Double
    ) {
        var newList: ArrayList<CurrencyCard> = ArrayList<CurrencyCard>()
        data?.forEach { (key, value) ->
            var currencyCard: CurrencyCard = CurrencyCard()
            currencyCard.currency_code = key
            currencyCard.currency_value = value
            var selected_currency_val = data.get("USD" + selected_currency)
            var converted_usd_val: Double =
                userinput.toDouble() / selected_currency_val?.toDouble()!!
            var converted_result = converted_usd_val.toDouble() * value.toDouble()

            val number3digits: Double = String.format("%.3f", converted_result).toDouble()
            val number2digits: Double = String.format("%.2f", number3digits).toDouble()

            currencyCard.converted_value = number2digits.toString()
            newList.add(currencyCard)
        }
        DisplayConvertedCurrencyListAdapter = DisplayConvertedCurrencyListAdapter(newList)
        activityMainBinding.rvConvertedCurrencylist.layoutManager =
            GridLayoutManager(this@MainActivity, 3)
        activityMainBinding.rvConvertedCurrencylist.setHasFixedSize(true)
        activityMainBinding.rvConvertedCurrencylist.adapter = DisplayConvertedCurrencyListAdapter
    }

    override fun onTextVeiwLayoutManager() {
        showCurrencyListPopupWindow(activityMainBinding.tvSelectCurrency)
    }

    override fun onEditTextVeiwLayoutManager() {
        if (activityMainBinding.tvSelectCurrency.text.isNullOrEmpty()) {
            activityMainBinding.tvSelectCurrency.error = "Please select currency"
        } else {
            calculate_currency(
                Exchangecurrency_itemsList,
                activityMainBinding.tvSelectCurrency.text as String,
                activityMainBinding.editText.text.toString().toDouble()
            )
        }
    }

    private fun showCurrencyListPopupWindow(anchorView: View) {
        val listPopupWindow = ListPopupWindow(this)
        listPopupWindow.setWidth(anchorView.width)
        var sampleData: MutableList<String> = ArrayList()
        sampleData = currency_itemsList?.keys?.toList() as MutableList<String>
        listPopupWindow.setAnchorView(anchorView)
        val listPopupWindowAdapter =
            ListPopupWindowAdapter(this, sampleData, object :
                ListPopupWindowAdapter.OnClickCurrencyListener {
                override fun onClick(position: String) {
                    activityMainBinding.tvSelectCurrency.text = position
                    listPopupWindow.dismiss()
                    Exchangecurrency_itemsList
                    if (activityMainBinding.editText.text.toString().isNullOrEmpty()) {
                        activityMainBinding.editText.setError("Please enter amount")
                    } else {
                        calculate_currency(
                            Exchangecurrency_itemsList,
                            activityMainBinding.tvSelectCurrency.text as String,
                            activityMainBinding.editText.text.toString().toDouble()
                        )
                    }

                }
            })
        listPopupWindow.setAdapter(listPopupWindowAdapter)
        listPopupWindow.show()
    }


}

