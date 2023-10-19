package com.example.fashiondetectv11

object Constants {
    //db name
    const val DB_NAME="FASHION_DB"
    const val  DB_VERSION=2
    //table name
    const val  TABLE_NAME="FASHION_TABLE"
    //column/field
    private const val C_ID="ID"
    const val C_NAME="NAME"
    const val C_IMAGE="IMAGE"
    const val C_RATE="RATE"
    const val C_REASON="REASON"
    const val C_ADDED_TIMESTAMP="ADDED_TIME_STAMP"
    const val C_UPDATED_TIMESTAMP="UPDATED_TIMESTAMP"

    const val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "("
                    + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_NAME + " TEXT,"
                    + C_IMAGE + " TEXT,"
                    + C_RATE + " TEXT,"
                    + C_REASON + " TEXT,"
                    + C_ADDED_TIMESTAMP + " TEXT,"
                    + C_UPDATED_TIMESTAMP + " TEXT"
                    + ")"
    )

}