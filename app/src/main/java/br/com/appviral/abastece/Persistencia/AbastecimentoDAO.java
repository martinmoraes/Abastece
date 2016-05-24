package br.com.appviral.abastece.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.appviral.abastece.Entidade.Abastecimento;


/**
 * Created by Martin on 13/05/2016.
 */
public class AbastecimentoDAO {

    Context context;
    DBSQLite dbsqLite;

    public AbastecimentoDAO(Context context) {
        this.context = context;
        dbsqLite = new DBSQLite(context);
    }


    private String dataParaPersistir(String dtOriginal){
        Date data = null;
        SimpleDateFormat sdfPersiste = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfMostra = new SimpleDateFormat("dd/MM/yyyy");

        //De String para Date
        try {
            data = sdfMostra.parse(dtOriginal);
        } catch (ParseException e) {}

        //De Date para String
        return sdfPersiste.format(data);
    }

    private String dataParaMostrar(String dtPersisitida){
        Date data = null;
        SimpleDateFormat sdfPersiste = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfMostra = new SimpleDateFormat("dd/MM/yyyy");

        //De String para Date
        try {
            data = sdfPersiste.parse(dtPersisitida);
        } catch (ParseException e) {}

        //De Date para String
        return sdfMostra.format(data);
    }

    public Long inserir(Abastecimento abastecimento) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.qtde_litros);
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.vlr_litro);
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.vlr_total);
        values.put(Abastecimento.CAMPO_DATA, dataParaPersistir(abastecimento.data));
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, String.valueOf(abastecimento.combustivel));
        Long id = db.insert(Abastecimento.TABELA, null, values);
        db.close();
        return id;
    }

    public boolean alterar(Abastecimento abastecimento) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.qtde_litros);
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.vlr_litro);
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.vlr_total);
        values.put(Abastecimento.CAMPO_DATA, dataParaPersistir(abastecimento.data));
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, String.valueOf(abastecimento.combustivel));
        String whare = Abastecimento.CAMPO_ID + " = ?";

        int id = db.update(Abastecimento.TABELA, values, whare, new String[]{String.valueOf(abastecimento.id)});
        db.close();
        return id > 0 ? true : false;
    }

    public boolean excluir(Abastecimento abastecimento) {
        return excluir(abastecimento.id);
    }

    public boolean excluir(Long id) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        String whare = Abastecimento.CAMPO_ID + " = ?";
        int ret = db.delete(Abastecimento.TABELA, whare, new String[]{String.valueOf(id)});
        db.close();
        return ret > 0 ? true : false;
    }

    public boolean excluirTudo() {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        int ret = db.delete(Abastecimento.TABELA, null, null);
        db.close();
        return ret > 0 ? true : false;
    }

    public ArrayList<Abastecimento> listar() {
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        ArrayList<Abastecimento> lista = new ArrayList<>();

       String colunas[] = {Abastecimento.CAMPO_ID, Abastecimento.CAMPO_QTDE_LITROS,
               Abastecimento.CAMPO_VLR_LITRO, Abastecimento.CAMPO_VLR_TOTAL, Abastecimento.CAMPO_DATA,
               Abastecimento.CAMPO_COMBUSTIVEL};

        Cursor cursor = db.query(Abastecimento.TABELA,colunas,null,null,null,null,Abastecimento.CAMPO_DATA + " DESC");
        Abastecimento umAbastecimento;

        if (cursor.moveToFirst()) {
            do {
                umAbastecimento = new Abastecimento();
                umAbastecimento.id = cursor.getLong(0);
                umAbastecimento.qtde_litros = cursor.getFloat(1);
                umAbastecimento.vlr_litro = cursor.getFloat(2);
                umAbastecimento.vlr_total = cursor.getFloat(3);
                umAbastecimento.data = dataParaMostrar(cursor.getString(4));
                umAbastecimento.setCombustivel(cursor.getString(5));

                lista.add(umAbastecimento);
            } while (cursor.moveToNext());
            db.close();
        }
        return lista;
    }
}
