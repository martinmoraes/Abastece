package br.com.appviral.abastece.Util;

import java.text.NumberFormat;

/**
 * Created by Martin on 09/06/2016.
 */
public class Util {

    private static NumberFormat nf;

    //Para ser usado atrelado ao EditText.setSelection(EditText.length());
    public static String formataFloat(String vlr, int casasDecimais) {
        if (vlr == null) {
            return null;
        }

        String temp, strCasasDecimais = "0.";
        casasDecimais = casasDecimais > 0 ? casasDecimais : 1;
        vlr = vlr.replace(".", "");
        vlr = vlr.replaceAll(",", "");
        while (vlr.startsWith("0"))
            vlr = vlr.substring(1);
        int tamanho = vlr.length();

        if (tamanho == casasDecimais) {
            temp = strCasasDecimais + vlr;
        } else {
            if (tamanho > casasDecimais) {
                temp = vlr.substring(0, tamanho - casasDecimais) + "." + vlr.substring(tamanho - casasDecimais, tamanho);
            } else {
                for (int x = 0; x < casasDecimais - tamanho; x++) {
                    strCasasDecimais += "0";
                }
                temp = strCasasDecimais + vlr;
            }
        }
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(casasDecimais);
        nf.setMinimumFractionDigits(casasDecimais);
        return nf.format(Double.valueOf(temp));
    }

    public static String formataFloat(String vlr){
        return formataFloat(vlr,2);
    }


    public static String formataFloat(float vlr, int casasDecimais){
        String str_vlr = String.valueOf(vlr);
        int posPonto = str_vlr.indexOf(".");
        int tamanho = str_vlr.length();
        int contemCasasDecimais = tamanho - posPonto;
        if(contemCasasDecimais > casasDecimais){
            str_vlr = str_vlr.substring(0, contemCasasDecimais+casasDecimais);
        }
        return formataFloat(str_vlr, casasDecimais);
    }

    public static String formataFloat(float vlr){
        return formataFloat(vlr,2);
    }

    public static double deStringParaDouble(String vlr) {
        if (vlr.isEmpty()) {
            vlr = "0.0";
        }
        vlr = vlr.replaceAll(",", "");
        return Double.valueOf(vlr);
    }

    public static float deStringParaFloat(String vlr) {
        if (vlr.isEmpty()) {
            vlr = "0.0";
        }
        vlr = vlr.replaceAll(",", "");
        return Float.valueOf(vlr);
    }


}
