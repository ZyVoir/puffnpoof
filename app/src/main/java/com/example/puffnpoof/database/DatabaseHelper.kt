package com.example.puffnpoof.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.puffnpoof.GlobalVar
import com.example.puffnpoof.Model.Doll
import com.example.puffnpoof.Model.Transaction
import com.example.puffnpoof.Model.User
import org.json.JSONException
import org.json.JSONObject

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "pnf.db", null,1){

    private lateinit var requestQueue: RequestQueue
    var context = context;

    private fun parseJSON(jsonObject: JSONObject): ArrayList<Doll>{
        val dollList = ArrayList<Doll>()

        try{
            val dollArray = jsonObject.getJSONArray("dolls")
            for(i in 0 until dollArray.length()){
                val dollObject = dollArray.getJSONObject(i)
                val desc  = dollObject.getString("desc")
                val name = dollObject.getString("name")
                val size = dollObject.getString("size")
                val price = dollObject.getInt("price")
                val rating = dollObject.getDouble("rating")
                val imageLink = dollObject.getString("imageLink")

                dollList.add(Doll(i+1, name, size, rating, price, imageLink, desc))
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
        return dollList
    }

    private fun fetchAPIData(context: Context){
        requestQueue = Volley.newRequestQueue(context)

        val url = "https://api.npoint.io/9d7f4f02be5d5631a664"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,

            Response.Listener { response ->
                try{
                    val dollList = parseJSON(response)
                    storeAPIDB(dollList)

                }catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Volley Error", error.toString())
            }
        )

        requestQueue.add(request);
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserQuery = """
            CREATE TABLE IF NOT EXISTS Users (
                UserID INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT UNIQUE,
                Email TEXT,
                Password TEXT,
                TelephoneNumber TEXT,
                Gender TEXT
            );
        """.trimIndent()

        val createDollQuery = """
            CREATE TABLE IF NOT EXISTS Dolls (
                DollID INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT,
                Size TEXT,
                Rating REAL,
                Price INTEGER,
                Image TEXT,
                Description TEXT
            );
        """.trimIndent()

        val createTransactionQuery = """
            CREATE TABLE IF NOT EXISTS Transactions (
                TransactionID INTEGER PRIMARY KEY AUTOINCREMENT,
                TransactionDate DATE,
                TransactionQuantity INTEGER,
                UserID INTEGER,
                DollID INTEGER,
                FOREIGN KEY(UserID) REFERENCES users(UserID),
                FOREIGN KEY(DollID) REFERENCES dolls(DollID)
            )
        """.trimIndent()

        db?.execSQL(createUserQuery)
        db?.execSQL(createDollQuery)
        db?.execSQL(createTransactionQuery)

        fetchAPIData(context)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Transactions")
        db?.execSQL("DROP TABLE IF EXISTS Users")
        db?.execSQL("DROP TABLE IF EXISTS Dolls")

        onCreate(db)
    }

    fun insertUser(user : User){
        // for register
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Username", user.username)
            put("Email", user.email)
            put("Password", user.password)
            put("TelephoneNumber", user.phoneNum)
            put("Gender", user.gender)
        }
        db.insert("Users", null, values)
        print("Inserted")
        db.close()
    }

    fun isUserExist(username : String) : Boolean{
        val db = readableDatabase
        val query = """SELECT username FROM users where Username = "${username}" """.trimIndent()

        val cursor = db.rawQuery(query,null)

        cursor.moveToFirst()
        if (cursor.count > 0){
            // if data exist
            return true
        }
        return false;
    }

    fun checkUserCredential(username : String, password : String) : User? {
        val db = readableDatabase
        val query = """SELECT * FROM users where Username = "${username}" AND Password = "${password}"  """.trimIndent()

        val cursor = db.rawQuery(query,null)

        cursor.moveToFirst()
        //  if user and password matches
        if (cursor.count > 0){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))
            val username = cursor.getString(cursor.getColumnIndexOrThrow("Username"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("Password"))
            val telpnum = cursor.getString(cursor.getColumnIndexOrThrow("TelephoneNumber"))
            val gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"))

            val user = User(id, username, email, password,telpnum,gender)

            cursor.close()
            db.close()

            return user
        }
        cursor.close()
        db.close()
        return null;
    }



    private fun storeAPIDB(dollList : ArrayList<Doll>){
        // for register
        val db = writableDatabase

        for (i in 0 until dollList.size){
            var doll = dollList[i]

            val values = ContentValues().apply {
                put("Name", doll.name)
                put("Size", doll.size)
                put("Rating", doll.rating)
                put("Price", doll.price)
                put("Image", doll.imgDir)
                put("Description", doll.imgDesc)
            }

            db.insert("Dolls", null, values)
        }
        db.close()

    }

    fun getAllDolls() : ArrayList<Doll> {
        val dollList = ArrayList<Doll>()
        val db = readableDatabase

        val query = "SELECT * FROM Dolls"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                val size = cursor.getString(cursor.getColumnIndexOrThrow("Size"))
                val rating = cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"))
                val imgLink = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow("Description"))

                dollList.add(Doll(id,name,size,rating,price,imgLink,desc))
            } while (cursor.moveToNext());
        }

        cursor.close()
        db.close()

        return dollList
    }

    fun getDoll(id : Int) : Doll? {
        val db = readableDatabase

        val query = """SELECT * FROM Dolls WHERE DollID = "${id}" """
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
            val size = cursor.getString(cursor.getColumnIndexOrThrow("Size"))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"))
            val imgLink = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow("Description"))

            return Doll(id,name,size,rating,price,imgLink,desc);
        }
        return null;
    }

    fun insertTransaction(transaction : Transaction) {
        // for register
        val db = writableDatabase
        val values = ContentValues().apply {
            put("TransactionDate", transaction.transactionDate)
            put("TransactionQuantity", transaction.quantity)
            put("UserID", transaction.userID)
            put("DollID", transaction.dollID)
        }
        db.insert("Transactions", null, values)
        db.close()
    }

    fun getAllTransaction() : ArrayList<Transaction>{
        val transactionList = ArrayList<Transaction>()
        val db = readableDatabase

        val query = "SELECT * FROM Transactions"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            do {
                val tid = cursor.getInt(cursor.getColumnIndexOrThrow("TransactionID"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("TransactionDate"))
                val qty = cursor.getInt(cursor.getColumnIndexOrThrow("TransactionQuantity"))
                val did = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
                val uid = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))

                transactionList.add(Transaction(tid,uid,did,qty,date))
            } while (cursor.moveToNext());
        }

        cursor.close()
        db.close()

        return transactionList
    }

    fun getTransaction(tid : Int) : Transaction? {
        val db = readableDatabase

        val query = """SELECT * FROM Transactions WHERE TransactionID = "${tid}" """
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        if (cursor.count > 0){

                val tid = cursor.getInt(cursor.getColumnIndexOrThrow("TransactionID"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("TransactionDate"))
                val qty = cursor.getInt(cursor.getColumnIndexOrThrow("TransactionQuantity"))
                val did = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
                val uid = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))

               return Transaction(tid,uid,did,qty,date)
        }

        cursor.close()
        db.close()

        return null
    }

    fun getDollInfoFromTransaction(tid : Int) : Doll? {
        val db = readableDatabase

        val query = """SELECT DollID FROM Transactions WHERE TransactionID = "${tid}" """
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            val did = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
            return getDoll(did);
        }
        return null;
    }

    fun updateTransaction(transaction: Transaction, updatedQty : Int){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("TransactionQuantity", updatedQty)
            put("TransactionDate", transaction.transactionDate)
            put("DollID", transaction.dollID)
            put("UserID", transaction.userID)
        }
        db.update("Transactions", values, "TransactionID = ?", arrayOf(transaction.transactionID.toString()))
        db.close()
    }

    fun deleteTransaction(tid : Int){
        val db = writableDatabase
        db.delete("Transactions", "TransactionID = ?", arrayOf(tid.toString()))
        db.close();
    }

    fun countTransaction() : Int{
        val db = readableDatabase

        val query = "SELECT * FROM Transactions"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        val count = cursor.count

        cursor.close()
        db.close()

        return count
    }
}