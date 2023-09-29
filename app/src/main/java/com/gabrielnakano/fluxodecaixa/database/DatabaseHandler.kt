package com.gabrielnakano.fluxodecaixa.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.gabrielnakano.fluxodecaixa.entity.Caixa

class DatabaseHandler( context : Context ) : SQLiteOpenHelper ( context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {
        private val DATABASE_NAME = "banco"
        private val DATABASE_VERSION = 3
        private val TABLE_NAME = "caixa"
        private val KEY_ID = "_id"
        private val KEY_VALOR = "valor"
        private val KEY_DATA_LANC = "data_lancamento"
        private val KEY_TIPO = "tipo"
        private val KEY_DETALHE = "detalhe"
    }

    fun incluir( caixa : Caixa ) {
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put( KEY_VALOR, caixa.valor )
        registro.put( KEY_DATA_LANC, caixa.data_lancamento )
        registro.put( KEY_TIPO, caixa.tipo )
        registro.put( KEY_DETALHE, caixa.detalhe )

        db.insert( TABLE_NAME, null, registro )
    }

    fun listar() : MutableList<Caixa> {

        val db = this.writableDatabase

        val registro = db.query( TABLE_NAME,
            null, null, null, null, null, null )

        val registros = mutableListOf<Caixa>()

        while( registro.moveToNext() ) {
            val caixa = Caixa( registro.getInt( 0 ), registro.getDouble( 1 ), registro.getString( 2 ), registro.getString( 3 ), registro.getString( 4 ) )
            registros.add( caixa )
        }

        return registros

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL( "CREATE TABLE IF NOT EXISTS ${TABLE_NAME} ( ${KEY_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${KEY_VALOR} DOUBLE, ${KEY_DETALHE} TEXT, ${KEY_DATA_LANC} TEXT, ${KEY_TIPO} TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL( "DROP TABLE ${TABLE_NAME}" )
        onCreate( db )
    }

}