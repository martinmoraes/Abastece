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
        return nf.format(Double.valueOf(temp));
    }


    public static double deStringParaDouble(String vlr) {
        vlr = vlr.replaceAll(",", "");
        return Double.valueOf(vlr);
    }

    public static double deStringParaFloat(String vlr) {
        vlr = vlr.replaceAll(",", "");
        return Float.valueOf(vlr);
    }


}
