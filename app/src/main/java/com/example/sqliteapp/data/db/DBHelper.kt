package com.example.sqliteapp.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "veiculos.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "veiculo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOME TEXT NOT NULL,
                TIPO TEXT NOT NULL,
                COR TEXT NOT NULL,
                PRECO INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}