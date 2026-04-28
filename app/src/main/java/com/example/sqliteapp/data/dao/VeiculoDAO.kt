package com.example.sqliteapp.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.sqliteapp.model.VeiculoModel
import com.example.sqliteapp.data.db.DBHelper

class VeiculoDAO(private val context: Context) {

    private val dbHelper = DBHelper(context)

    fun addVeiculo(veiculo: VeiculoModel): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("NOME", veiculo.nome)
            put("TIPO", veiculo.tipo)
            put("COR", veiculo.cor)
            put("PRECO", veiculo.preco)
        }
        val id = db.insert(DBHelper.TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllVeiculos(): List<VeiculoModel> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)
        val veiculoList = mutableListOf<VeiculoModel>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("NOME"))
            val tipo = cursor.getString(cursor.getColumnIndexOrThrow("TIPO"))
            val cor = cursor.getString(cursor.getColumnIndexOrThrow("COR"))
            val preco = cursor.getInt(cursor.getColumnIndexOrThrow("PRECO"))
            veiculoList.add(VeiculoModel(id, nome, tipo, cor, preco))
        }
        cursor.close()
        db.close()
        return veiculoList
    }

    fun getVeiculoById(id: Int): VeiculoModel? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_NAME, null,
            "ID=?", arrayOf(id.toString()),
            null, null, null
        )
        var veiculo: VeiculoModel? = null
        if (cursor.moveToFirst()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("NOME"))
            val tipo = cursor.getString(cursor.getColumnIndexOrThrow("TIPO"))
            val cor = cursor.getString(cursor.getColumnIndexOrThrow("COR"))
            val preco = cursor.getInt(cursor.getColumnIndexOrThrow("PRECO"))
            veiculo = VeiculoModel(id, nome, tipo, cor, preco)
        }
        cursor.close()
        db.close()
        return veiculo
    }

    fun updateVeiculo(veiculo: VeiculoModel): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("NOME", veiculo.nome)
            put("TIPO", veiculo.tipo)
            put("COR", veiculo.cor)
            put("PRECO", veiculo.preco)
        }
        val rowsAffected = db.update(
            DBHelper.TABLE_NAME, values,
            "ID=?", arrayOf(veiculo.id.toString())
        )
        db.close()
        return rowsAffected
    }

    fun deleteVeiculo(id: Int): Int {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete(
            DBHelper.TABLE_NAME,
            "ID=?", arrayOf(id.toString())
        )
        db.close()
        return rowsDeleted
    }
}