package com.example.puffnpoof

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Sms
import android.telephony.SmsManager
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.puffnpoof.Model.User
import com.example.puffnpoof.database.DatabaseHelper
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var usernameField : EditText
    lateinit var passwordField : EditText
    lateinit var btnSeePass : ImageButton
    lateinit var btnLogin : Button
    lateinit var tvRegister : TextView
    lateinit var smsManager: SmsManager
    private val OTPCode = Random.nextInt(1000, 10000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // global variable
        val global = application as GlobalVar
        smsManager = SmsManager.getDefault()
        // listener for form
        usernameField = findViewById(R.id.et_username)
        passwordField = findViewById(R.id.et_password)

        // btn listener for validate form
        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            val db = DatabaseHelper(this);
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (db.isUserExist(username)){
                // if there exist user name
                val user : User? = db.checkUserCredential(username,password)

                if (user != null){

                    global.enableOTPUser(user)
                    global.enableActiveUser(user)
                    checkSendSMSPermission(global.getOTPPhoneNumber(), "HERE IS THE VERIFICATION CODE FOR PUFFandPOOF: $OTPCode")

                }
                else {
                    // the username isnt exsit
                    Toast.makeText(this,"Wrong Password", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "This username not exist", Toast.LENGTH_SHORT).show()
            }
        }

        // btn listener for seeing password
        var btnSeePassState = false
        btnSeePass = findViewById(R.id.imgbtn_seepassword)
        btnSeePass.setOnClickListener{
            btnSeePassState = !btnSeePassState
            if (btnSeePassState) {
                passwordField.transformationMethod = null
            } else {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //redirect to register page
        tvRegister = findViewById(R.id.tv_register)
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    fun checkSendSMSPermission(phonenumber: String, message: String){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 100)
        }else{
            sendSMS(phonenumber, message)
        }
    }

    fun sendSMS(phonenumber: String, message: String){
        smsManager.sendTextMessage(phonenumber, null, message, null, null)
        val intent = Intent(this, OTPPage::class.java).apply {
            putExtra("OTP", OTPCode)
        }
        Toast.makeText(this, "OTP Code has been sent", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }
}