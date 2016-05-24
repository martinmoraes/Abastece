package br.com.appviral.abastece.Entidade;

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


    public Long id;
    public float qtde_litros;
    public float vlr_litro;
    public float vlr_total;
    public String data;
    public tipo_combustivel combustivel;

    public boolean isCalculou = false;


    public void calculaTerceiro(String qtde_litrosS, String vlr_litroS, String vlr_totalS) {
        if (!qtde_litrosS.equals(""))
            qtde_litros = Float.valueOf(qtde_litrosS);
        if (!vlr_litroS.equals(""))
            vlr_litro = Float.valueOf(vlr_litroS);
        if (!vlr_totalS.equals(""))
            vlr_total = Float.valueOf(vlr_totalS);

        isCalculou = false;

        //Calcula vlr_Total
        if (qtde_litros > 0 && vlr_litro > 0) {
            vlr_total = qtde_litros * vlr_litro;
            isCalculou = true;
        }

        //Calcula vlr do litro
        if (qtde_litros > 0 && vlr_total > 0) {
            vlr_litro = vlr_total / qtde_litros;
            isCalculou = true;
        }

        //Calcula Qtde litros
        if (vlr_total > 0 && vlr_litro > 0) {
            qtde_litros = vlr_total / vlr_litro;
            isCalculou = true;
        }


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

    public void setCombustivel(String combustivel) {
        switch (combustivel) {
            case "gasolina":
                this.combustivel = tipo_combustivel.gasolina;
                break;
            case "alcool":
                this.combustivel = tipo_combustivel.alcool;
                break;
            case "diesel":
                this.combustivel = tipo_combustivel.diesel;
                break;
        }



    }


    public enum tipo_combustivel {
        gasolina, alcool, diesel;
    }

}


