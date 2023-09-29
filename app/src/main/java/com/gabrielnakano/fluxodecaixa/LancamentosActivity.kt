package com.gabrielnakano.fluxodecaixa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.gabrielnakano.fluxodecaixa.database.DatabaseHandler
import com.gabrielnakano.fluxodecaixa.databinding.ActivityMainBinding
import com.gabrielnakano.fluxodecaixa.entity.Caixa

class LancamentosActivity : AppCompatActivity() {

    private lateinit var banco: DatabaseHandler
    private lateinit var lvLancamentos : ListView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        lvLancamentos = findViewById(R.id.lvLancamentos)
        banco = DatabaseHandler( this)

        val registros = banco.listar()
        val listaLancamentos = recuperaArrayString(registros)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaLancamentos)

        lvLancamentos.adapter = adapter
    }

    fun recuperaArrayString( registros : MutableList<Caixa> ) : MutableList<String> {

        val listaLancamentos = mutableListOf<String>()

        for (caixa in registros) {
            if (caixa.tipo == "Crédito") {
                listaLancamentos.add("C: ${caixa.data_lancamento} - ${caixa.detalhe} - ${caixa.valor}")
            } else {
                listaLancamentos.add("D: ${caixa.data_lancamento} - ${caixa.detalhe} - ${caixa.valor}")
            }

        }

        return listaLancamentos
    }
}