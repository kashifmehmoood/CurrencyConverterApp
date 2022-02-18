package com.example.currencyconverterapp.view
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.utils.MyCustomProgressDialog

abstract class BaseActivity : AppCompatActivity() {

    lateinit var custom_progress: MyCustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        custom_progress = MyCustomProgressDialog(this, R.style.CustomDialogTheme)
    }

    public  fun MyCustomProgressDialog.showProgress() {
        custom_progress.show()
    }

    fun MyCustomProgressDialog.hideProgress() {
        custom_progress.dismiss()
    }


}