package com.example.wangfeiwangfei.weatherdemo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity(),sendDataListenner {
    override fun sendDataListenner(str:String) {
        wheath_tv.text=str;
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var utils=HttpUtils()
        utils.doGet(this)

    }
}
