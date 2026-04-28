package com.example.sqliteapp.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqliteapp.R
import com.example.sqliteapp.data.dao.VeiculoDAO
import com.example.sqliteapp.model.VeiculoModel

class MainActivity : AppCompatActivity() {

    private lateinit var dao: VeiculoDAO
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView
    private lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dao = VeiculoDAO(this)
        listView = findViewById(R.id.listView)
        tvEmpty = findViewById(R.id.tvEmpty)
        titleTextView = findViewById(R.id.titleTextView)

        listView.setOnItemClickListener { adapterView, _, position, _ ->
            val veiculo = adapterView.getItemAtPosition(position) as VeiculoModel
            val intent = Intent(this, SecondaryActivity::class.java)

            intent.putExtra("Veiculo", veiculo)

            startActivity(intent)
        }

        titleTextView.setOnClickListener {
            abrirMenuFiltros()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarDados(null, null, false)
    }

    private fun carregarDados(buscaNome: String?, filtroTipo: String?, ordenarPreco: Boolean) {
        var lista = dao.getAllVeiculos()

        if (!buscaNome.isNullOrBlank()) {
            lista = lista.filter { it.nome.contains(buscaNome, ignoreCase = true) }
        }

        if (!filtroTipo.isNullOrBlank()) {
            lista = lista.filter { it.tipo.equals(filtroTipo, ignoreCase = true) }
        }

        if (ordenarPreco) {
            lista = lista.sortedBy { it.preco }
        }

        if (lista.isEmpty()) {
            listView.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            listView.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
            listView.adapter = adapter
        }

        val valorTotal = lista.sumOf { it.preco }
        titleTextView.text = "Garagem (Patrimônio: R$ $valorTotal)\n[Toque aqui para Filtrar]"
    }

    private fun abrirMenuFiltros() {
        val opcoes = arrayOf(
            "Listar Todos (Limpar Filtros)",
            "Buscar por Nome",
            "Filtrar por Categoria",
            "Ordenar por Preço (Menor -> Maior)"
        )

        AlertDialog.Builder(this)
            .setTitle("Opções de Listagem")
            .setItems(opcoes) { _, posicao ->
                when (posicao) {
                    0 -> carregarDados(null, null, false)
                    1 -> abrirDialogBuscaNome()
                    2 -> abrirDialogBuscaCategoria()
                    3 -> carregarDados(null, null, true)
                }
            }
            .show()
    }

    private fun abrirDialogBuscaNome() {
        val input = EditText(this)
        input.hint = "Digite o nome do veículo"

        AlertDialog.Builder(this)
            .setTitle("Buscar por Nome")
            .setView(input)
            .setPositiveButton("Buscar") { _, _ ->
                val textoBusca = input.text.toString()
                carregarDados(textoBusca, null, false)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun abrirDialogBuscaCategoria() {
        val categorias = arrayOf("CARRO", "MOTO", "VAN", "CAMINHÃO")

        AlertDialog.Builder(this)
            .setTitle("Escolha a Categoria")
            .setItems(categorias) { _, posicao ->
                val categoriaSelecionada = categorias[posicao]
                carregarDados(null, categoriaSelecionada, false)
            }
            .show()
    }

    fun novoVeiculo(view: View) {
        val intent = Intent(this, SecondaryActivity::class.java)
        startActivity(intent)
    }
}