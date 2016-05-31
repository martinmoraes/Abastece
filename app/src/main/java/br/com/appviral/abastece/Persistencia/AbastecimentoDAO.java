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
        List<Abastecimento> lista = new ArrayList<>();

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

    public List<Abastecimento> listarMediaMensal(int media) {
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        List<Abastecimento> lista = new ArrayList<>();
        List<Abastecimento> listaTEMP = new ArrayList<>();

        String referenciaDeTotalizacao = null;
        Cursor cursor = db.query(Abastecimento.TABELA,
                colunas,
                null, null, null, null,
                Abastecimento.CAMPO_DATA + " DESC");

        if (cursor.moveToFirst()) {
            String referencia;
            do {
                Abastecimento umAbastecimento = povoaAbastecimento(cursor);
                referencia = pegaReferencia(umAbastecimento.data, media);
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

    public Abastecimento totalizadorList(List<Abastecimento> lista, String referencia) {
        float Qtde_litros = 0;
        float Total_Gasto = 0;

        for (Abastecimento abastecimento : lista) {
            Qtde_litros += abastecimento.qtde_litros;
            Total_Gasto += abastecimento.vlr_total;
        }
        Abastecimento abastecimenTotalizado = new Abastecimento();
        abastecimenTotalizado.data = referencia;
        abastecimenTotalizado.qtde_litros = Qtde_litros;
        abastecimenTotalizado.vlr_total = Total_Gasto;
        abastecimenTotalizado.vlr_litro = Total_Gasto / Qtde_litros;

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
    }
}
