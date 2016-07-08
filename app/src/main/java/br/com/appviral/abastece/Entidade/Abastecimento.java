package br.com.appviral.abastece.Entidade;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 13/05/2016.
 */
public class Abastecimento {
    public static final String TABELA = "abastecimento";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_QTDE_LITROS = "qtde_litros";
    public static final String CAMPO_VLR_LITRO = "vlr_litro";
    public static final String CAMPO_VLR_TOTAL = "vlr_total";
    public static final String CAMPO_DATA = "data";
    public static final String CAMPO_COMBUSTIVEL = "combustivel";

    private Long mId;
    private float mQtdeLitros;
    private float mVlrLitro;
    private float mVlrTotal;
    private String mData;
    private String mCombustivel;


    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public float getQuatidadelitros() {
        return mQtdeLitros;
    }

    public void setQtdelitros(float QtdeLitros) {
        mQtdeLitros = QtdeLitros;
    }

    public float getValorLitro() {
        return mVlrLitro;
    }

    public void setVlrLitro(float vlrLitro) {
        mVlrLitro = vlrLitro;
    }

    public float getValorTotal() {
        return mVlrTotal;
    }

    public void setVlrTotal(float vlrTotal) {
        mVlrTotal = vlrTotal;
    }

    public String getData() {
        return mData;
    }

    public String getDataParaPersistir() {
        Date data = null;
        SimpleDateFormat sdfPersiste = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat sdfMostra = DateFormat.getDateInstance();

        //De String para Date
        try {
            data = sdfMostra.parse(mData);
        } catch (ParseException e) {
            Log.d("MEUAPP", "Em AbastecimentoDAO - dataParaPersistir(String dtOriginal) " + e.toString());
        }
        return sdfPersiste.format(data);
    }

    public void setDataDoBanco(String dtPersisitida) {
        Date data = null;
        SimpleDateFormat sdfPersiste = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat sdfMostra = DateFormat.getDateInstance();

        //De String para Date
        try {
            data = sdfPersiste.parse(dtPersisitida);
        } catch (ParseException e) {
            Log.d("MEUAPP", "Em AbastecimentoDAO - dataParaMostrar(String dtPersisitida) " + e.toString());
        }

        //De Date para String
        mData = sdfMostra.format(data);
    }

    public void setData(String data) {
        this.mData = data;
    }

    public void setCombustivel(String combustivel) {
        mCombustivel = combustivel;
    }

    public String getCombustivel() {
        return mCombustivel;
    }


    @Override
    public String toString() {
        return "Abastecimento{" +
                "mId=" + mId +
                ", mQtdeLitros=" + mQtdeLitros +
                ", mVlrLitro=" + mVlrLitro +
                ", mVlrTotal=" + mVlrTotal +
                ", mData='" + mData + '\'' +
                ", mCombustivel=" + mCombustivel +
                '}';
    }
}


