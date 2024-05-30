package com.example.puffnpoof.Model

class Transaction {
    var transactionID : Int = 0
    var userID : Int = 0
    var dollID : Int = 0
    var quantity : Int = 0
    var transactionDate : String = ""

    constructor(
        transactionID: Int,
        userID: Int,
        dollID: Int,
        quantity: Int,
        transactionDate: String
    ) {
        this.transactionID = transactionID
        this.userID = userID
        this.dollID = dollID
        this.quantity = quantity
        this.transactionDate = transactionDate
    }
}