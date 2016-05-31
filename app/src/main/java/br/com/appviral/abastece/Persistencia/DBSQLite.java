package br.com.appviral.abastece.Persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.appviral.abastece.Entidade.Abastecimento;


/**
 * Created by Martin on 13/05/2016.
 */
public class DBSQLite extends SQLiteOpenHelper {
    private static final String NOME_BASE = "base.db";
    private static final int VERSAO = 7;
    String CRIA_TABELA_PESSOA;

    public DBSQLite(Context context) {
        super(context, NOME_BASE, null, VERSAO);
        CRIA_TABELA_PESSOA = "CREATE TABLE " + Abastecimento.TABELA + "("
                + Abastecimento.CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Abastecimento.CAMPO_QTDE_LITROS + " REAL, "
                + Abastecimento.CAMPO_VLR_LITRO + " REAL, "
                + Abastecimento.CAMPO_VLR_TOTAL + " REAL, "
                + Abastecimento.CAMPO_DATA + " TEXT, "
                + Abastecimento.CAMPO_COMBUSTIVEL + " TEXT)";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRIA_TABELA_PESSOA);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Abastecimento.TABELA);
        db.execSQL(CRIA_TABELA_PESSOA);
    }
}
