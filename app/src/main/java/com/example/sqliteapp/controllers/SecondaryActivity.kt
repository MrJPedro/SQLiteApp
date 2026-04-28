package com.example.sqliteapp.controllers

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

    private val listaTipos = arrayOf("CARRO", "MOTO", "VAN", "CAMINHÃO")
    private val listaCores = arrayOf("BRANCO", "PRETO", "PRATA", "CINZA", "VERMELHO", "AZUL")

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

        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaTipos)
        spTipo.adapter = adapterTipo

        val adapterCor = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaCores)
        spCor.adapter = adapterCor

        val veiculo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Veiculo", VeiculoModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Veiculo") as? VeiculoModel
        }

        if (veiculo != null) {
            veiculoId = veiculo.id
            tilNome.editText?.setText(veiculo.nome)
            tilPreco.editText?.setText(veiculo.preco.toString())

            val posicaoTipo = listaTipos.indexOf(veiculo.tipo)
            if (posicaoTipo >= 0) spTipo.setSelection(posicaoTipo)

            val posicaoCor = listaCores.indexOf(veiculo.cor)
            if (posicaoCor >= 0) spCor.setSelection(posicaoCor)

            btnExcluir.visibility = View.VISIBLE
        } else {
            btnExcluir.visibility = View.GONE
        }
    }

    fun salvarVeiculo(view: View) {
        val nome = tilNome.editText?.text.toString().trim()
        val tipo = spTipo.selectedItem?.toString() ?: "CARRO"
        val cor = spCor.selectedItem?.toString() ?: "BRANCO"
        val precoStr = tilPreco.editText?.text.toString().trim()

        if (nome.isBlank()) {
            Toast.makeText(this, "O nome do veículo é obrigatório", Toast.LENGTH_SHORT).show()
            tilNome.editText?.requestFocus()
            return
        }

        if (nome.length < 2 || nome.length > 50) {
            Toast.makeText(this, "O nome deve ter entre 2 e 50 caracteres", Toast.LENGTH_SHORT).show()
            tilNome.editText?.requestFocus()
            return
        }

        if (precoStr.isBlank()) {
            Toast.makeText(this, "O preço do veículo é obrigatório", Toast.LENGTH_SHORT).show()
            tilPreco.editText?.requestFocus()
            return
        }

        val preco = precoStr.toIntOrNull()

        if (preco == null || preco <= 0) {
            Toast.makeText(this, "Insira um valor numérico positivo para o preço", Toast.LENGTH_SHORT).show()
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