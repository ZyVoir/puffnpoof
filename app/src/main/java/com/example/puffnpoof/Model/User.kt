package com.example.puffnpoof.Model

class User {


    var userID : Int = 0
    var username : String = ""
    var email : String = ""
    var password : String = ""
    var phoneNum : String = ""
    var gender : String = ""

    constructor(
        userID: Int,
        username: String,
        email: String,
        password: String,
        phoneNum: String,
        gender: String
    ) {
        this.userID = userID
        this.username = username
        this.email = email
        this.password = password
        this.phoneNum = phoneNum
        this.gender = gender
    }


}