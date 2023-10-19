package com.example.fashiondetectv11

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context?):SQLiteOpenHelper (
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME)
        onCreate(db)
    }
    fun insertRecord(
        name:String?,
        image:String?,
        rate:String?,
        reason:String?,
        addedTime:String?,
        updatedTime:String?,
    ):Long{
        val db=this.writableDatabase
        val values=ContentValues()

        values.put(Constants.C_NAME, name)
        values.put(Constants.C_IMAGE, image)
        values.put(Constants.C_RATE,rate)
        values.put(Constants.C_REASON,reason)
        values.put(Constants.C_ADDED_TIMESTAMP, addedTime)
        values.put(Constants.C_UPDATED_TIMESTAMP, updatedTime)

        val id=db.insert(Constants.TABLE_NAME,null,values)
        db.close()
        return id
    }
}