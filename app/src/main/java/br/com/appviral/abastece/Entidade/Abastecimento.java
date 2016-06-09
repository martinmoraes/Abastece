package br.com.appviral.abastece.Entidade;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

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

    public static final String INSERIR = "inserir";
    public static final String ALTERAR = "alterar";


    public Long id;
    public float qtde_litros;
    public float vlr_litro;
    public float vlr_total;
    public String data;
    private tipo_combustivel combustivel;


    public void setCombustivel(String combustivel) {
        switch (combustivel) {
            case "gasolina":
            case "Gasolina":
                this.combustivel = tipo_combustivel.gasolina;
                break;
            case "alcool":
            case "Alcool":
                this.combustivel = tipo_combustivel.alcool;
                break;
            case "diesel":
            case "Diesel":
                this.combustivel = tipo_combustivel.diesel;
                break;
        }
    }

    public String getCombustiviel() {
        return combustivel.toString();
    }

    public Float getVlrLitro() {
        return vlr_litro;
    }

    public Float getVlrTotal() {
        return vlr_total;
    }

    public Float getQtdeLitros() {
        return qtde_litros;
    }

    public enum tipo_combustivel {
        gasolina, alcool, diesel;
    }

    @Override
    public String toString() {
        return "Abastecimento{" +
                "id=" + id +
                ", qtde_litros=" + qtde_litros +
                ", vlr_litro=" + vlr_litro +
                ", vlr_total=" + vlr_total +
                ", data='" + data + '\'' +
                ", combustivel=" + combustivel +
                '}';
    }
}


