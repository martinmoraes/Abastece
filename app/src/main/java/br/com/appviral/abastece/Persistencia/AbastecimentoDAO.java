package br.com.appviral.abastece.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.appviral.abastece.Entidade.Abastecimento;


/**
 * Created by Martin on 13/05/2016.
 */
public class AbastecimentoDAO {

    Context context;
    DBSQLite dbsqLite;

    public AbastecimentoDAO(Context context){
        this.context = context;
        dbsqLite = new DBSQLite(context);
    }

    public Long inserir(Abastecimento abastecimento){
        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.qtde_litros);
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.vlr_litro);
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.vlr_total);
        values.put(Abastecimento.CAMPO_DATA, abastecimento.data);
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, String.valueOf(abastecimento.combustivel));
        Long id = db.insert(Abastecimento.TABELA, null, values);
        db.close();
        return id;
    }

    public boolean alterar(Abastecimento abastecimento){
        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.qtde_litros);
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.vlr_litro);
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.vlr_total);
        values.put(Abastecimento.CAMPO_DATA, abastecimento.data);
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, String.valueOf(abastecimento.combustivel));
        String whare = Abastecimento.CAMPO_ID + " = ?";

        int id = db.update(Abastecimento.TABELA, values, whare, new String[]{String.valueOf(abastecimento.id)});
        db.close();
        return id > 0 ? true : false;
    }

    public boolean excluir(Abastecimento abastecimento){
        return excluir(abastecimento.id);
    }

    public boolean excluir(Long id){
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        String whare = Abastecimento.CAMPO_ID + " = ?";
        int ret = db.delete(Abastecimento.TABELA,whare,new String[]{String.valueOf(id)});
        db.close();
        return ret > 0 ? true : false;
    }


    public ArrayList<Abastecimento> listar(){
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        ArrayList<Abastecimento> lista = new ArrayList<>();

        String selectQuery = "SELECT  " +


                Abastecimento.CAMPO_ID + "," +
        Abastecimento.CAMPO_QTDE_LITROS + "," +
                Abastecimento.CAMPO_VLR_LITRO + "," +
                Abastecimento.CAMPO_VLR_TOTAL + "," +
                Abastecimento.CAMPO_DATA + "," +
                Abastecimento.CAMPO_COMBUSTIVEL +
                " FROM " + Abastecimento.TABELA;

        Cursor cursor = db.rawQuery(selectQuery,null);
        Abastecimento umAbastecimento;

        if(cursor.moveToFirst()){
            do{
                umAbastecimento = new Abastecimento();
                umAbastecimento.id = cursor.getLong(0);
                umAbastecimento.qtde_litros = cursor.getFloat(1);
                umAbastecimento.vlr_litro = cursor.getFloat(2);
                umAbastecimento.vlr_total = cursor.getFloat(3);
                umAbastecimento.data = cursor.getString(4);
                umAbastecimento.setCombustivel(cursor.getString(5));

                lista.add(umAbastecimento);
            }while (cursor.moveToNext());
            db.close();
        }
        return lista;
    }
}
