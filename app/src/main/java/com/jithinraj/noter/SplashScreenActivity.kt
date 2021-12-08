package com.jithinraj.noter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences


class SplashScreenActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        if(sharedPref.getString("storename","0")!=null)
        {
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this@SplashScreenActivity, AddInfoActivity::class.java))
            finish()
        }

    }
}