package com.example.androidflier.repo.localdb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASENAME = "flier"
const val DATABASEVERSION = 1
const val TABLENAME = "Shop"

const val SHOP_COL_USERID = "userId"
const val SHOP_COL_COORDLAT = "coordLat"
const val SHOP_COL_COORDLNT = "coordLng"
const val SHOP_COL_TITLE = "title"
const val SHOP_COL_ADDRESS = "address"
const val SHOP_COL_DESCRIPTION = "description"
const val SHOP_COL_URL = "url"
const val SHOP_COL_IMG = "img"

const val CREATE_TABLE_SHOP =
    "CREATE TABLE " + TABLENAME + " (id SERIAL PRIMARY KEY AUTOINCREMENT, " +
            SHOP_COL_USERID + " INTEGER, " +
            SHOP_COL_TITLE + " CHARACTER VARYING(200), " +
            SHOP_COL_DESCRIPTION + " CHARACTER VARYING(300), " +
            SHOP_COL_ADDRESS + " CHARACTER VARYING(300), " +
            SHOP_COL_COORDLAT + " NUMERIC, " +
            SHOP_COL_COORDLNT + " NUMERIC, " +
            SHOP_COL_IMG + " CHARACTER VARYING(200), " +
            SHOP_COL_URL + " CHARACTER VARYING(200))"

private const val SQL_DELETE_TABLE_SHOP = "DROP TABLE IF EXISTS " + TABLENAME

class DataBaseHelper(var context: Context) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASEVERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE_SHOP)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_TABLE_SHOP)
        onCreate(p0)
    }

    companion object {
        @Volatile
        private var INSTANCE: DataBaseHelper? = null

        fun getInstance(context: Context): DataBaseHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataBaseHelper(context).also { INSTANCE = it }
            }
    }
}