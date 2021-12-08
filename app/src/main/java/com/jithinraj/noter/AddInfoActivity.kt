package com.jithinraj.noter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_info.*

class AddInfoActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_info)
        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        btnsave.setOnClickListener(){
            val userName=txtusername.text.toString()
            //Toast.makeText(this, userName, Toast.LENGTH_SHORT).show()

            //saveValue(userName)


           /* val editor: SharedPreferences.Editor =  sharedPref.edit()
            editor.putString("storename",userName)
            editor.putInt("storenum",10)
            editor.commit()*/

            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("Username",userName)
            startActivity(intent)

            //startActivity(Intent(this, MainActivity::class.java))
           // finish()
        }


    }

    fun saveValue(username:String){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("storename", username)
            commit()
        }
    }
}