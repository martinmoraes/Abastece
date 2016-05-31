package br.com.appviral.abastece.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    Context context;
    DBSQLite dbsqLite;
    String colunas[];
    List<Abastecimento> lista;
    List<Abastecimento> listaJan;
    List<Abastecimento> listaFev;
    List<Abastecimento> listaMar;
    List<Abastecimento> listaAbr;
    List<Abastecimento> listaMai;
    List<Abastecimento> listaJun;
    List<Abastecimento> listaJul;
    List<Abastecimento> listaAgo;
    List<Abastecimento> listaSet;
    List<Abastecimento> listaOut;
    List<Abastecimento> listaNov;
    List<Abastecimento> listaDez;

    public AbastecimentoDAO(Context context) {
        this.context = context;
        dbsqLite = new DBSQLite(context);

        String colunas[] = {Abastecimento.CAMPO_ID, Abastecimento.CAMPO_QTDE_LITROS,
                Abastecimento.CAMPO_VLR_LITRO, Abastecimento.CAMPO_VLR_TOTAL, Abastecimento.CAMPO_DATA,
                Abastecimento.CAMPO_COMBUSTIVEL};
    }

    public Long inserir(Abastecimento abastecimento) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Abastecimento.CAMPO_QTDE_LITROS, abastecimento.qtde_litros);
        values.put(Abastecimento.CAMPO_VLR_LITRO, abastecimento.vlr_litro);
        values.put(Abastecimento.CAMPO_VLR_TOTAL, abastecimento.vlr_total);
        values.put(Abastecimento.CAMPO_DATA, dataParaPersistir(abastecimento.data));
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, abastecimento.getCombustiviel());
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
        values.put(Abastecimento.CAMPO_COMBUSTIVEL, abastecimento.getCombustiviel());
        String whare = Abastecimento.CAMPO_ID + " = ?";

        int ret = db.update(Abastecimento.TABELA, values, whare, new String[]{String.valueOf(abastecimento.id)});
        db.close();
        return ret > 0 ? true : false;
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

    public List<Abastecimento> listarAbastecimentos() {
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        lista = new ArrayList<>();

        Cursor cursor = db.query(Abastecimento.TABELA,
                colunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                lista.add(povoaAbastecimento(cursor));
            } while (cursor.moveToNext());
            db.close();
        }
        return lista;
    }

    public List<Abastecimento> listarMediaMensal() {
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        lista = new ArrayList<>();
        concatenaIniciaNovasListas(null);


        String anoReferencia = null;
        Cursor cursor = db.query(Abastecimento.TABELA,
                colunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC");

        final int MES = 1;
        final int ANO = 2;

        if (cursor.moveToFirst()) {
            do {
                Abastecimento umAbastecimento = povoaAbastecimento(cursor);
                String dtCampos[] = umAbastecimento.data.split("/");
                if (anoReferencia == null) {
                    anoReferencia = dtCampos[ANO];
                }
                if (!anoReferencia.equals(dtCampos[ANO])) {
                    //Se mudou o ano guarda a lista anterior e começa uma nova lista
                    concatenaIniciaNovasListas(anoReferencia);
                    anoReferencia = dtCampos[ANO];

                }
                formaListaMes(umAbastecimento, dtCampos[MES]);
            } while (cursor.moveToNext());
            concatenaIniciaNovasListas(anoReferencia);
            db.close();
        }
        return lista;
    }


    private String dataParaPersistir(String dtOriginal) {
        Date data = null;
        SimpleDateFormat sdfPersiste = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfMostra = new SimpleDateFormat("dd/MM/yyyy");

        //De String para Date
        try {
            data = sdfMostra.parse(dtOriginal);
        } catch (ParseException e) {
        }

        //De Date para String
        return sdfPersiste.format(data);
    }

    private String dataParaMostrar(String dtPersisitida) {
        Date data = null;
        SimpleDateFormat sdfPersiste = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfMostra = new SimpleDateFormat("dd/MM/yyyy");

        //De String para Date
        try {
            data = sdfPersiste.parse(dtPersisitida);
        } catch (ParseException e) {
        }

        //De Date para String
        return sdfMostra.format(data);
    }

    public Abastecimento povoaAbastecimento(Cursor cursor) {
        Abastecimento umAbastecimento = new Abastecimento();
        umAbastecimento.id = cursor.getLong(0);
        umAbastecimento.qtde_litros = cursor.getFloat(1);
        umAbastecimento.vlr_litro = cursor.getFloat(2);
        umAbastecimento.vlr_total = cursor.getFloat(3);
        umAbastecimento.data = dataParaMostrar(cursor.getString(4));
        umAbastecimento.setCombustivel(cursor.getString(5));
        return umAbastecimento;
    }

    public Abastecimento totalizadorList(List<Abastecimento> lista, String anoReferencia, String mes) {
        float Qtde_litros = 0;
        float Total_Gasto = 0;

        for (Abastecimento abastecimento : lista) {
            Qtde_litros += abastecimento.qtde_litros;
            Total_Gasto += abastecimento.vlr_total;
        }
        Abastecimento abastecimenTotalizado = new Abastecimento();
        abastecimenTotalizado.data = mes +"/" + anoReferencia;
        abastecimenTotalizado.qtde_litros = Qtde_litros;
        abastecimenTotalizado.vlr_total = Total_Gasto;
        abastecimenTotalizado.vlr_litro = Total_Gasto / Qtde_litros;

        return abastecimenTotalizado;
    }

    public void formaListaMes(Abastecimento umAbastecimento, String mes) {
        switch (mes) {
            case "01":
                listaJan.add(umAbastecimento);
                break;
            case "02":
                listaFev.add(umAbastecimento);
                break;
            case "03":
                listaMar.add(umAbastecimento);
                break;
            case "04":
                listaAbr.add(umAbastecimento);
                break;
            case "05":
                listaMai.add(umAbastecimento);
                break;
            case "06":
                listaJun.add(umAbastecimento);
                break;
            case "07":
                listaJul.add(umAbastecimento);
                break;
            case "08":
                listaAgo.add(umAbastecimento);
                break;
            case "09":
                listaSet.add(umAbastecimento);
                break;
            case "10":
                listaOut.add(umAbastecimento);
                break;
            case "11":
                listaNov.add(umAbastecimento);
                break;
            case "12":
                listaDez.add(umAbastecimento);
                break;
        }
    }


    private boolean concatenaIniciaNovasListas(String anoReferencia) {

        //Concatena todas as listas
        if (listaJan != null && listaJan.size() > 0)
            lista.add(totalizadorList(listaJan, anoReferencia, "JAN"));
        if (listaFev != null && listaFev.size() > 0)
            lista.add(totalizadorList(listaFev, anoReferencia, "FEV"));
        if (listaMar != null && listaMar.size() > 0)
            lista.add(totalizadorList(listaMar, anoReferencia, "MAR"));
        if (listaAbr != null && listaAbr.size() > 0)
            lista.add(totalizadorList(listaAbr, anoReferencia, "ABR"));
        if (listaMai != null && listaMai.size() > 0)
            lista.add(totalizadorList(listaMai, anoReferencia, "MAI"));
        if (listaJun != null && listaJun.size() > 0)
            lista.add(totalizadorList(listaJun, anoReferencia, "JUN"));
        if (listaJul != null && listaJul.size() > 0)
            lista.add(totalizadorList(listaJul, anoReferencia, "JUL"));
        if (listaAgo != null && listaAgo.size() > 0)
            lista.add(totalizadorList(listaAgo, anoReferencia, "AGO"));
        if (listaSet != null && listaSet.size() > 0)
            lista.add(totalizadorList(listaSet, anoReferencia, "SET"));
        if (listaOut != null && listaOut.size() > 0)
            lista.add(totalizadorList(listaOut, anoReferencia, "OUT"));
        if (listaNov != null && listaNov.size() > 0)
            lista.add(totalizadorList(listaNov, anoReferencia, "NOV"));
        if (listaDez != null && listaDez.size() > 0)
            lista.add(totalizadorList(listaDez, anoReferencia, "DEZ"));


        listaJan = new ArrayList<>();
        listaFev = new ArrayList<>();
        listaMar = new ArrayList<>();
        listaAbr = new ArrayList<>();
        listaMai = new ArrayList<>();
        listaJun = new ArrayList<>();
        listaJul = new ArrayList<>();
        listaAgo = new ArrayList<>();
        listaSet = new ArrayList<>();
        listaOut = new ArrayList<>();
        listaNov = new ArrayList<>();
        listaDez = new ArrayList<>();

        return lista.size() > 0 ? true : false;
    }
}
