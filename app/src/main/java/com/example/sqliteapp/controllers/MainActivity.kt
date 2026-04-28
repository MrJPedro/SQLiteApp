package com.example.sqliteapp.controllers

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqliteapp.R
import com.example.sqliteapp.model.VeiculoModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listView)

        val veiculos = arrayOf(
            VeiculoModel(1, "CARRO", "Logan", 1000, "PRETO"),
            VeiculoModel(2, "MOTO", "Moto1", 1000, "PRETO"),
            VeiculoModel(3, "VAN", "Van1", 1000, "PRETO"),
            VeiculoModel(4, "CARRO", "Tiggo7", 5000, "CINZA"),
            VeiculoModel(5, "MOTO", "Moto2", 2000, "BRANCO")
        )

        val arrayAdapter: ArrayAdapter<VeiculoModel> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, veiculos
        )

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Selecionado " + veiculos[position], Toast.LENGTH_LONG).show()
        }
    }
}