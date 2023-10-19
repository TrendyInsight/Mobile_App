package com.example.fashiondetectv11


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addRecordBtn: FloatingActionButton = findViewById(R.id.addRecordBtn)

        addRecordBtn.setOnClickListener{
            startActivity(Intent(this,AddUpdateRecordActivity::class.java))
        }

    }
}