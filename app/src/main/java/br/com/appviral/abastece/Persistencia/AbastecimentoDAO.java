package br.com.appviral.abastece.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.appviral.abastece.Entidade.Abastecimento;


/**
 * Created by Martin on 13/05/2016.
 */
public class AbastecimentoDAO {

    public static final int DIREITO_DE_ESCRITA = 1;
    public static final int DIREITO_DE_LEITURA = 2;


    private DBSQLite mDBSqLite = null;
    private static AbastecimentoDAO sAbastecimentoDAO = null;
    private String mColunas[] = {Abastecimento.CAMPO_ID, Abastecimento.CAMPO_QTDE_LITROS,
            Abastecimento.CAMPO_VLR_LITRO, Abastecimento.CAMPO_VLR_TOTAL, Abastecimento.CAMPO_DATA,
            Abastecimento.CAMPO_COMBUSTIVEL};

    private final String SQL_CONTAGEM = "select count(*) from abastecimento";


    public AbastecimentoDAO(Context context) {
        mDBSqLite = new DBSQLite(context);
    }

    public boolean inserir(Abastecimento abastecimento) {
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.getQuatidadelitros());
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.getValorLitro());
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.getValorTotal());
        values.put(Abastecimento.CAMPO_DATA, abastecimento.getDataParaPersistir());
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, abastecimento.getCombustiviel());
        Long id = db.insert(Abastecimento.TABELA, null, values);
        db.close();
        return id > 0;
    }

    public boolean alterar(Abastecimento abastecimento) {
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.getQuatidadelitros());
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.getValorLitro());
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.getValorTotal());
        values.put(Abastecimento.CAMPO_DATA, abastecimento.getDataParaPersistir());
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, abastecimento.getCombustiviel());
        String whare = Abastecimento.CAMPO_ID + " = ?";

        int retorno = db.update(Abastecimento.TABELA, values, whare, new String[]{String.valueOf(abastecimento.getId())});
        db.close();
        return retorno > 0;
    }

    public boolean excluir(int posicao) {
        long id = getRegistroPosicao(posicao).getId();
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();
        String whare = Abastecimento.CAMPO_ID + " = ?";
        int ret = db.delete(Abastecimento.TABELA, whare, new String[]{String.valueOf(id)});
        db.close();
        return ret > 0 ? true : false;
    }

    public boolean excluirTudo() {
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();
        int ret = db.delete(Abastecimento.TABELA, null, null);
        db.close();
        return ret > 0 ? true : false;
    }

    public int getQuantidadeRegistros() {
        SQLiteDatabase db = mDBSqLite.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_CONTAGEM, null);
        int quantidade = -1;
        cursor.moveToFirst();
        quantidade = cursor.getInt(0);
        db.close();
        return quantidade;
    }

    public Abastecimento getRegistroId(long id) {

        /*
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();
        String whare = Abastecimento.CAMPO_ID + " = ?";
        int ret = db.delete(Abastecimento.TABELA, whare, new String[]{String.valueOf(id)});
        db.close();
        return ret > 0 ? true : false;
         */

        SQLiteDatabase db = mDBSqLite.getReadableDatabase();
        String where = Abastecimento.CAMPO_ID + " = ?";
        Cursor cursor = db.query(Abastecimento.TABELA,
                mColunas,
                where,
                new String[]{String.valueOf(id)}
                , null, null, null);
        Abastecimento abastecimento = null;
        if (cursor.moveToFirst())
            abastecimento = povoaAbastecimento(cursor);
        db.close();
        return abastecimento;

    }

    public Abastecimento getRegistroPosicao(int posicao) {
        SQLiteDatabase db = mDBSqLite.getReadableDatabase();
        Cursor cursor = db.query(Abastecimento.TABELA,
                mColunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC LIMIT 1 OFFSET " + posicao);
        Abastecimento abastecimento = null;
        if (cursor.moveToFirst())
            abastecimento = povoaAbastecimento(cursor);
        db.close();
        return abastecimento;
    }

    private Abastecimento povoaAbastecimento(Cursor cursor) {
        Abastecimento umAbastecimento = new Abastecimento();
        umAbastecimento.setId(cursor.getLong(0));
        umAbastecimento.setQtdelitros(cursor.getFloat(1));
        umAbastecimento.setVlrLitro(cursor.getFloat(2));
        umAbastecimento.setVlrTotal(cursor.getFloat(3));
        umAbastecimento.setDataDoBanco(cursor.getString(4));
        umAbastecimento.setCombustivel(cursor.getString(5));
        return umAbastecimento;
    }

}
