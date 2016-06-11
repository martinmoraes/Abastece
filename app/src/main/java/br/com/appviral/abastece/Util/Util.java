package br.com.appviral.abastece.Util;

import android.util.Log;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Martin on 09/06/2016.
 */
public class Util {

    // Esta classe considera todos os valores a direita como valores válidos. E despresa os valores ZERO que estão a esquerda.
    // Para ser usado atrelada ao EditText.setSelection(EditText.length());

    private static NumberFormat nf = NumberFormat.getInstance();


    public static String deFloatParaString(float num, int casasDecimais) {

        nf.setMaximumFractionDigits(casasDecimais);
        nf.setMinimumFractionDigits(casasDecimais);

        return nf.format(num);
    }

    public static String deFloatParaString(Float num) {
        return deFloatParaString(num, 2);
    }

    public static String floatDeStringParaStrin(String vlr, int casasDecimais) {
        return deFloatParaString(deStringParaFloat(vlr));
    }

    public static String floatDeStringParaStrin(String vlr) {
        return floatDeStringParaStrin(vlr, 2);
    }

    public static float deStringParaFloat(String vlr) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (vlr.isEmpty()) { vlr = "0";}

        String substituivel = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        String stringLimpa = vlr.toString().replaceAll(substituivel, "");

        double num;
        try {
            num = Double.parseDouble(stringLimpa);
        } catch (NumberFormatException e) {
            num = 0.00;
        }
        return (float) num / 100;
    }
}
