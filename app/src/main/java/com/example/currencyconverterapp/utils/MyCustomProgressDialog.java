package com.example.currencyconverterapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.example.currencyconverterapp.R;

public class MyCustomProgressDialog extends ProgressDialog {

    public MyCustomProgressDialog(Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialog);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}