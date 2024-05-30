package com.example.puffnpoof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class OTPPage : AppCompatActivity() {

    private  lateinit var OTPCode: EditText
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otppage)

        OTPCode = findViewById(R.id.otp_et)
        button = findViewById(R.id.next_button)

        val OTP = intent.getIntExtra("OTP", 0)

        button.setOnClickListener {
            val value1 = OTPCode.text.toString()
            val value2 = OTP.toString()

            if (value1.isEmpty()){
                Toast.makeText(this, "OTP Value must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(value1 == value2){
                val intent = Intent(this, HomeActivity::class.java)
                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "OTP code is wrong", Toast.LENGTH_SHORT).show()
            }

        }


    }
}