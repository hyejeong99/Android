package com.example.guru_oneulmaru

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class SplashActivity: Activity() {//로딩화면
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(2000)//화면 띄우는 시간 설정
        }
        catch (e: InterruptedException) {
            Log.d("SplashActivity", "InterruptedException")
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}