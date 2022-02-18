package com.example.currencyconverterapp.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.currencyconverterapp.R


class ListPopupWindowAdapter internal constructor(
    private val mActivity: Activity,
    dataSource: List<String>,
    onClickCurrencyListener: OnClickCurrencyListener
) :
    BaseAdapter() {
    private var mDataSource: List<String> = ArrayList()
    private val layoutInflater: LayoutInflater
    private val onClickCurrencyListener: OnClickCurrencyListener
    override fun getCount(): Int {
        return mDataSource.size
    }

    override fun getItem(position: Int): String {
        return mDataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = layoutInflater.inflate(R.layout.currency_item, null)
            holder.tvTitle = convertView.findViewById(R.id.tv_currency)
            convertView.setTag(holder)
        } else {
            holder = convertView.getTag() as ViewHolder
        }

        // bind data
        holder.tvTitle!!.text = getItem(position)
        holder.tvTitle?.setOnClickListener()
        {
            onClickCurrencyListener.onClick(getItem(position))
        }

        return convertView
    }

    inner class ViewHolder {
        var tvTitle: TextView? = null
    }

    // interface to return callback to activity
    interface OnClickCurrencyListener {
        fun onClick(position: String)
    }

    init {
        mDataSource = dataSource
        layoutInflater = mActivity.layoutInflater
        this.onClickCurrencyListener = onClickCurrencyListener
    }
}