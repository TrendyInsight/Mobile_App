package com.example.fashiondetect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity3)

        val secondLayoutBtn = findViewById<TextView>(R.id.getStarted)
        secondLayoutBtn.setOnClickListener{
            val Intent=Intent(this,SecondLayout::class.java)
            startActivity(Intent)
        }
    }
}