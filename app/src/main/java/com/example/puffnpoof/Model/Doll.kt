package com.example.puffnpoof.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream

class Doll {
    var dollID : Int = 0
    var name : String = ""
    var size : String = ""
    var rating : Double = 0.0
    var price : Int = 0
    var imgDir : String = ""
    var imgDesc : String = ""

    constructor(
        dollID: Int,
        name: String,
        size: String,
        rating: Double,
        price: Int,
        imgDir: String,
        imgDesc: String
    ) {
        this.dollID = dollID
        this.name = name
        this.size = size
        this.rating = rating
        this.price = price
        this.imgDir = imgDir
        this.imgDesc = imgDesc
    }
}