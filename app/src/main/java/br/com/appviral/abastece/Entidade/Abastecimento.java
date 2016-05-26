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


    public Long id;
    public float qtde_litros;
    public float vlr_litro;
    public float vlr_total;
    public String data;
    private tipo_combustivel combustivel;

    public boolean isCalculou = false;


    private float retornaFloat(String s) {
        String novoValor = s;
        int ultimaVirgula = s.lastIndexOf(',');
        int ultimoPonto = s.lastIndexOf('.');

        if (ultimaVirgula > -1) {
            //Vírgula no decimal
            if (ultimoPonto > -1)
                novoValor = s.replace(".", "");
            novoValor = novoValor.replace(",", ".");
        }
        return Float.valueOf(novoValor);
    }

    public void calculaTerceiro(String qtde_litrosS, String vlr_litroS, String vlr_totalS) {
        if (!qtde_litrosS.equals(""))
            qtde_litros = retornaFloat(qtde_litrosS);
        if (!vlr_litroS.equals(""))
            vlr_litro = retornaFloat(vlr_litroS);
        if (!vlr_totalS.equals(""))
            vlr_total = retornaFloat(vlr_totalS);

        isCalculou = false;
        DecimalFormat df = new DecimalFormat("0.00");

        //Calcula vlr_Total
        if (qtde_litros > 0 && vlr_litro > 0) {
            vlr_total = qtde_litros * vlr_litro;
            String vlr = df.format(vlr_total).replace(",", ".");
            vlr_total = Float.parseFloat(vlr);
            isCalculou = true;
        }

        //Calcula vlr do litro
        if (qtde_litros > 0 && vlr_total > 0) {
            vlr_litro = vlr_total / qtde_litros;
            String vlr = df.format(vlr_litro).replace(",", ".");
            vlr_litro = Float.parseFloat(vlr);
            isCalculou = true;
        }

        //Calcula Qtde litros
        if (vlr_total > 0 && vlr_litro > 0) {
            qtde_litros = vlr_total / vlr_litro;
            String vlr = df.format(qtde_litros).replace(",", ".");
            qtde_litros = Float.parseFloat(vlr);
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

    public String getCombustiviel(){
        return combustivel.toString();
    }

    public String getVlrLitro(){
        return String.valueOf(vlr_litro);
    }

    public String getVlrTotal(){
        return String.valueOf(vlr_total);
    }

    public String getQtdeLitros(){
        return String.valueOf(qtde_litros);
    }

    public enum tipo_combustivel {
        gasolina, alcool, diesel;
    }

}


