package com.gabrielnakano.fluxodecaixa

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.gabrielnakano.fluxodecaixa.database.DatabaseHandler
import com.gabrielnakano.fluxodecaixa.databinding.ActivityMainBinding
import com.gabrielnakano.fluxodecaixa.entity.Caixa
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var spTipo: Spinner
    private lateinit var spDetalhe: Spinner
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spTipo = findViewById(R.id.spTipo)
        spDetalhe = findViewById(R.id.spDetalhe)

        val tipoOptions = arrayOf("Crédito", "Débito")
        val detalheDebitoOptions = arrayOf("Alimentação", "Transporte", "Saúde", "Moradia")
        val detalheCreditoOptions = arrayOf("Salário", "Extras")

        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoOptions)
        spTipo.adapter = adapterTipo

        spTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedTipo = tipoOptions[position]

                val detalheOptions = if (selectedTipo == "Crédito") {
                    detalheCreditoOptions
                } else {
                    detalheDebitoOptions
                }

                val adapterDetalhe = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    detalheOptions
                )
                spDetalhe.adapter = adapterDetalhe
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Function obrigatória pois está implementando AdapterView
            }
        }

        banco = DatabaseHandler(this)
    }

    fun lancarOnClick(view: View) {
        try {
            val etValor = binding.etValor.text.toString()
            val valor = etValor.toDouble()
            val etDataLanc = binding.etData.text.toString()
            val tipo = binding.spTipo.selectedItem.toString()
            val detalhe = binding.spTipo.selectedItem.toString()
            val registro = Caixa(0, valor, detalhe, etDataLanc, tipo)

            banco.incluir(registro)
            Toast.makeText(this, "Lançamento efetuado com sucesso", Toast.LENGTH_LONG).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_LONG).show()
        }
    }

    fun listarSaldoOnClick(view: View) {
        var registros = banco.listar()
        var saldo = 0.0

        for (caixa in registros) {

            if (caixa.tipo == "Crédito") {
                saldo += caixa.valor
            } else {
                saldo -= caixa.valor
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saldo")
        builder.setMessage(saldo.toString())

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun listarLancamentosOnClick(view: View) {
        val intent = Intent(this, LancamentosActivity::class.java)
        startActivity(intent)
    }


}