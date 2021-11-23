package com.th.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sec : Int = 0
        val tv : TextView = findViewById(R.id.tv_hello)

        timer(period = 1000, initialDelay = 1000){
            sec++
            runOnUiThread {
                tv.text = sec.toString()
            }

            print(sec)
        }



//        val btn : Button = findViewById(R.id.btn_kor)
//
//        btn.setOnClickListener{
//            tv.text = "안녕"
//        }



    }
}