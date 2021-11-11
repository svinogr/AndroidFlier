package com.example.androidflier.repo.localdb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Stock

const val DATABASENAME = "flier"
const val DATABASEVERSION = 1
const val TABLENAME = "Shop"

const val SHOP_COL_ID = "id"
const val SHOP_COL_USERID = "userId"
const val SHOP_COL_COORDLAT = "coordLat"
const val SHOP_COL_COORDLNT = "coordLng"
const val SHOP_COL_TITLE = "title"
const val SHOP_COL_ADDRESS = "address"
const val SHOP_COL_DESCRIPTION = "description"
const val SHOP_COL_URL = "url"
const val SHOP_COL_IMG = "img"

const val CREATE_TABLE_SHOP =
    "CREATE TABLE " + TABLENAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SHOP_COL_USERID + " INTEGER, " +
            SHOP_COL_TITLE + " CHARACTER VARYING(200), " +
            SHOP_COL_DESCRIPTION + " CHARACTER VARYING(300), " +
            SHOP_COL_ADDRESS + " CHARACTER VARYING(300), " +
            SHOP_COL_COORDLAT + " NUMERIC, " +
            SHOP_COL_COORDLNT + " NUMERIC, " +
            SHOP_COL_IMG + " CHARACTER VARYING(200), " +
            SHOP_COL_URL + " CHARACTER VARYING(200))"

private const val SQL_DELETE_TABLE_SHOP = "DROP TABLE IF EXISTS $TABLENAME"

class DataBaseHelper(var context: Context) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASEVERSION) {

    companion object {
        @Volatile
        private var instance: DataBaseHelper? = null

        fun getInstance(context: Context): DataBaseHelper =
            instance ?: synchronized(this) {
                instance ?: DataBaseHelper(context).also { instance = it }
            }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE_SHOP)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_TABLE_SHOP)
        onCreate(p0)
    }

    fun save(shop: Shop) {
        val writableDatabase = instance!!.writableDatabase

        val contValue = ContentValues().apply {
            put(SHOP_COL_ID, shop.id)
            put(SHOP_COL_USERID, shop.userId)
            put(SHOP_COL_COORDLAT, shop.coordLat)
            put(SHOP_COL_COORDLNT, shop.coordLng)
            put(SHOP_COL_TITLE, shop.title)
            put(SHOP_COL_ADDRESS, shop.address)
            put(SHOP_COL_DESCRIPTION, shop.description)
            put(SHOP_COL_URL, shop.url)
            put(SHOP_COL_IMG, shop.img)
        }

        writableDatabase.insert(TABLENAME, null, contValue)
    }

    fun getAllShop(): List<Shop> {
        val list = mutableListOf<Shop>()
        val writableDatabase = instance!!.writableDatabase
        val query = writableDatabase.query(TABLENAME, null, null, null, null, null, null)

        with(query) {
            while (moveToNext()) {
                val shop = shopFromCursor(this)
                list.add(shop)
            }
        }

        query.close()
        writableDatabase.close()

        return list
    }

    private fun shopFromCursor(cursor: Cursor?): Shop {

        return Shop(
            id = cursor!!.getLong(cursor.getColumnIndexOrThrow(SHOP_COL_ID)),
            created = "",
            updated = "",
            status = "",
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(SHOP_COL_USERID)),
            coordLat = cursor.getDouble(cursor.getColumnIndexOrThrow(SHOP_COL_COORDLAT)),
            coordLng = cursor.getDouble(cursor.getColumnIndexOrThrow(SHOP_COL_COORDLNT)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_COL_TITLE)),
            address = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_COL_ADDRESS)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_COL_DESCRIPTION)),
            url = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_COL_URL)),
            img = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_COL_IMG)),
            stocks = mutableListOf<Stock>()
        )
    }
}
