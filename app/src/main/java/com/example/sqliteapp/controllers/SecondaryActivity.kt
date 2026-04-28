package com.example.sqliteapp.controllers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqliteapp.R
import com.example.sqliteapp.data.dao.VeiculoDAO
import com.example.sqliteapp.model.VeiculoModel
import com.google.android.material.textfield.TextInputLayout

class SecondaryActivity : AppCompatActivity() {

    private lateinit var tilNome: TextInputLayout
    private lateinit var spTipo: Spinner
    private lateinit var spCor: Spinner
    private lateinit var tilPreco: TextInputLayout
    private lateinit var btnExcluir: Button

    private lateinit var dao: VeiculoDAO
    private var veiculoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_secondary)

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dao = VeiculoDAO(this)

        tilNome = findViewById(R.id.textInputLayout2)
        spTipo = findViewById(R.id.spinner)
        spCor = findViewById(R.id.spinner2)
        tilPreco = findViewById(R.id.textInputLayoutPreco)
        btnExcluir = findViewById(R.id.btnExcluir)

        veiculoId = intent.getIntExtra("VEICULO_ID", 0)

        if (veiculoId != 0) {
            val veiculo = dao.getVeiculoById(veiculoId)
            if (veiculo != null) {
                tilNome.editText?.setText(veiculo.nome)
                tilPreco.editText?.setText(veiculo.preco.toString())
                btnExcluir.visibility = View.VISIBLE
            }
        }
    }

    fun salvarVeiculo(view: View) {
        val nome = tilNome.editText?.text.toString()
        val tipo = spTipo.selectedItem?.toString() ?: "Carro"
        val cor = spCor.selectedItem?.toString() ?: "Branco"
        val precoStr = tilPreco.editText?.text.toString()

        if (nome.isBlank()) {
            Toast.makeText(this, "Nome é obrigatório", Toast.LENGTH_SHORT).show()
            tilNome.editText?.requestFocus()
            return
        }

        val preco = precoStr.toIntOrNull() ?: 0
        if (preco <= 0) {
            Toast.makeText(this, "Preço inválido", Toast.LENGTH_SHORT).show()
            tilPreco.editText?.requestFocus()
            return
        }

        val veiculo = VeiculoModel(veiculoId, tipo, nome, preco, cor)

        if (veiculoId == 0) {
            dao.addVeiculo(veiculo)
            Toast.makeText(this, "Veículo adicionado com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            dao.updateVeiculo(veiculo)
            Toast.makeText(this, "Veículo atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    fun excluirVeiculo(view: View) {
        if (veiculoId != 0) {
            dao.deleteVeiculo(veiculoId)
            Toast.makeText(this, "Veículo excluído com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun voltar(view: View) {
        finish()
    }
}