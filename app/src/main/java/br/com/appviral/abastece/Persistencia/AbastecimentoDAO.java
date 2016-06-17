package br.com.appviral.abastece.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.appviral.abastece.Entidade.Abastecimento;


/**
 * Created by Martin on 13/05/2016.
 */
public class AbastecimentoDAO {


    private DBSQLite mDBSqLite;
    private String mColunas[] = {Abastecimento.CAMPO_ID, Abastecimento.CAMPO_QTDE_LITROS,
            Abastecimento.CAMPO_VLR_LITRO, Abastecimento.CAMPO_VLR_TOTAL, Abastecimento.CAMPO_DATA,
            Abastecimento.CAMPO_COMBUSTIVEL};

    public AbastecimentoDAO(Context context) {
        mDBSqLite = new DBSQLite(context);
    }


    public Long inserir(Abastecimento abastecimento) {
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.getQtdelitros());
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.getVlrLitro());
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.getVlrTotal());
        values.put(Abastecimento.CAMPO_DATA, abastecimento.getDataParaPersistir());
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, abastecimento.getCombustiviel());
        Long id = db.insert(Abastecimento.TABELA, null, values);
        db.close();
        return id;
    }

    public boolean alterar(Abastecimento abastecimento) {
        SQLiteDatabase db = mDBSqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.getQtdelitros());
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.getVlrLitro());
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.getVlrTotal());
        values.put(Abastecimento.CAMPO_DATA, abastecimento.getDataParaPersistir());
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, abastecimento.getCombustiviel());
        String whare = Abastecimento.CAMPO_ID + " = ?";

        int ret = db.update(Abastecimento.TABELA, values, whare, new String[]{String.valueOf(abastecimento.getId())});
        db.close();
        return ret > 0;
    }

    public boolean excluir(Abastecimento abastecimento) {
        return excluir(abastecimento.getId());
    }

    public boolean excluir(Long id) {
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

    private Cursor consultaDB(){
        SQLiteDatabase db = mDBSqLite.getReadableDatabase();
        Cursor cursor = db.query(Abastecimento.TABELA,
                mColunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC");
        db.close();
        return cursor;
    }

    public List<Abastecimento> listarAbastecimentos() {
        List<Abastecimento> lista = new ArrayList<>();
//        Cursor cursor = consultaDB();
        SQLiteDatabase db = mDBSqLite.getReadableDatabase();
        Cursor cursor = db.query(Abastecimento.TABELA,
                mColunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC");
        if (cursor.moveToFirst()) {
            do {
                lista.add(povoaAbastecimento(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public int getQtdeRegistros(){
        Cursor cursor = consultaDB();
        return cursor.getCount();
    }

    public Abastecimento getRegistro(int posicao){
        Cursor cursor = consultaDB();
        cursor.moveToPosition(posicao);
        return povoaAbastecimento(cursor);
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


/*


    public List<Abastecimento> listarMediaMensal(int media) {
        SQLiteDatabase db = mDBSqLite.getReadableDatabase();
        List<Abastecimento> lista = new ArrayList<>();
        List<Abastecimento> listaTEMP = new ArrayList<>();

        String referenciaDeTotalizacao = null;
        Cursor cursor = db.query(Abastecimento.TABELA,
                mColunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC");

        if (cursor.moveToFirst()) {
            String referencia;
            do {
                Abastecimento umAbastecimento = povoaAbastecimento(cursor);
                referencia = pegaReferencia(umAbastecimento.getData(), media);
                if (referenciaDeTotalizacao == null) {
                    referenciaDeTotalizacao = referencia;
                }
                if (!referenciaDeTotalizacao.equals(referencia)) {
                    //Se mudou o mês/ano sumariza a lista e começa uma nova lista
                    lista.add(totalizadorList(listaTEMP, referenciaDeTotalizacao));
                    listaTEMP = new ArrayList<>();
                    referenciaDeTotalizacao = referencia;
                }
                listaTEMP.add(umAbastecimento);
            } while (cursor.moveToNext());
            lista.add(totalizadorList(listaTEMP, referencia));
            db.close();
        }
        return lista;
    }


    public Abastecimento totalizadorList(List<Abastecimento> lista, String referencia) {
        float Qtde_litros = 0;
        float Total_Gasto = 0;

        for (Abastecimento abastecimento : lista) {
            Qtde_litros += abastecimento.getQtdelitros();
            Total_Gasto += abastecimento.getVlrTotal();
        }
        Abastecimento abastecimenTotalizado = new Abastecimento();
        abastecimenTotalizado.setData(referencia);
        abastecimenTotalizado.setQtdelitros(Qtde_litros);
        abastecimenTotalizado.setVlrTotal(Total_Gasto);
        abastecimenTotalizado.setVlrLitro(Total_Gasto / Qtde_litros);

        return abastecimenTotalizado;
    }


    public static int MEDIA_MENSAL = 1;
    public static int MEDIA_BIMESTRAL = 2;
    public static int MEDIA_TRIMESTRAL = 3;
    public static int MEDIA_SEMESTRAL = 4;

    private String pegaReferencia(String dt, int media) {
        final int MES = 1;
        final int ANO = 2;
        String dtCampos[] = dt.split("/");

        String referencia[][] = {
                {"01", "JAN/", "1ºBi/", "1ºTri/", "1ºSem/"},
                {"02", "FEV/", "1ºBi/", "1ºTri/", "1ºSem/"},
                {"03", "MAR/", "2ºBi/", "1ºTri/", "1ºSem/"},
                {"04", "ABR/", "2ºBi/", "2ºTri/", "1ºSem/"},
                {"05", "MAI/", "3ºBi/", "2ºTri/", "1ºSem/"},
                {"06", "JUN/", "3ºBi/", "2ºTri/", "1ºSem/"},
                {"07", "JUL/", "4ºBi/", "3ºTri/", "2ºSem/"},
                {"08", "AGO/", "4ºBi/", "3ºTri/", "2ºSem/"},
                {"09", "SET/", "5ºBi/", "3ºTri/", "2ºSem/"},
                {"10", "OUT/", "5ºBi/", "4ºTri/", "2ºSem/"},
                {"11", "NOV/", "6ºBi/", "4ºTri/", "2ºSem/"},
                {"12", "DEZ/", "6ºBi/", "4ºTri/", "2ºSem/"}
        };
        int oMES = Integer.parseInt(dtCampos[MES]) - 1;
        return referencia[oMES][media]+ dtCampos[ANO];
    }*/
}
