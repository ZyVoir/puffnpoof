package com.example.puffnpoof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.puffnpoof.Model.User
import com.example.puffnpoof.database.DatabaseHelper


class RegisterActivity : AppCompatActivity() {

    lateinit var emailField: EditText
    lateinit var usernameField: EditText
    lateinit var passwordField: EditText
    lateinit var telpNumField: EditText
    lateinit var genderField: RadioGroup
    lateinit var genderBtn: RadioButton
    lateinit var btnRegister: Button
    lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //global variable
        val global = application as GlobalVar

        // gender Field
        genderField = findViewById(R.id.rg_gender)
        genderField.check(R.id.rb_male)

        // listener for the form
        emailField = findViewById(R.id.et_email)
        usernameField = findViewById(R.id.et_username)
        passwordField = findViewById(R.id.et_password)
        telpNumField = findViewById(R.id.et_telpNum)
        genderBtn = findViewById(genderField.checkedRadioButtonId)

        // button regis listener
        btnRegister = findViewById(R.id.btn_register)
        btnRegister.setOnClickListener {
            if (validateRegiser(global)) {
                var email = emailField.text.toString()
                var username = usernameField.text.toString()
                var password = passwordField.text.toString()
                var phoneNumber = telpNumField.text.toString()
                var gender = genderBtn.text.toString()

                val db = DatabaseHelper(this)

                //add user
                db.insertUser(User(0,username,email,password,phoneNumber,gender))




                // clear all of the form
                emailField.setText("")
                usernameField.setText("")
                passwordField.setText("")
                telpNumField.setText("")

                Toast.makeText(this,"Register Success", Toast.LENGTH_SHORT).show()

                // direct user straight to home page
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        // redirect login page
        tvLogin = findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateRegiser(global: GlobalVar): Boolean {
        var email = emailField.text.toString()
        var username = usernameField.text.toString()
        var password = passwordField.text.toString()
        var phoneNumber = telpNumField.text.toString()
        var gender = genderBtn.text.toString()

        var db = DatabaseHelper(this)

        if (email.isEmpty() or username.isEmpty() or password.isEmpty() or phoneNumber.isEmpty() or gender.isEmpty()) {
            Toast.makeText(this, "All field must be filled!", Toast.LENGTH_SHORT).show()
            return false
        } else if (!email.endsWith("@puff.com")) {
            Toast.makeText(this, "Email must ends with @puff.com", Toast.LENGTH_SHORT).show()
            return false
        } else if (db.isUserExist(username)) {
            Toast.makeText(this, "username has already been taken", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 8) {
            Toast.makeText(this, "Password must be 8 chars long", Toast.LENGTH_SHORT).show()
            return false
        } else if (!global.isNumeric(phoneNumber)) {
            Toast.makeText(this, "Telephone must be numeric", Toast.LENGTH_SHORT).show()
            return false
        } else if (phoneNumber.length !in 11..13) {
            Toast.makeText(this, "Telephone must be 11 - 13 digit", Toast.LENGTH_SHORT).show()
            return false
        } else if (gender != "Female" && gender != "Male") {
            Toast.makeText(this, "Pick a gender :)", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}