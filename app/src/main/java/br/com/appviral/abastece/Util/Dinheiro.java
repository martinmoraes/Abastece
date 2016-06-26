package br.com.appviral.abastece.Util;

import java.text.NumberFormat;

/**
 * Created by Martin on 09/06/2016.
 */
public class Dinheiro {



    public static String deDinheiroParaString(float num, int casasDecimais) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(casasDecimais);
        nf.setMinimumFractionDigits(casasDecimais);
        String s =nf.format(num);
        return s;
    }

    public static String deDinheiroParaString(Float num) {
        return deDinheiroParaString(num, 2);
    }

    public static String deDinheiroParaDinheiro(String vlr, int casasDecimais) {
        return deDinheiroParaString(deDinheiroParaFloat(vlr),casasDecimais);
    }

    public static String deDinheiroParaDinheiro(String vlr) {
        return deDinheiroParaDinheiro(vlr, 2);
    }

    public static float deDinheiroParaFloat(String vlr) {
        if (vlr.isEmpty()) { vlr = "0";}

        String padrao = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        String stringLimpa = vlr.replaceAll(padrao, "");

        double num;
        try {
            num = Double.parseDouble(stringLimpa);
        } catch (NumberFormatException e) {
            num = 0.00;
        }
        return (float) num / 100;
    }
}
