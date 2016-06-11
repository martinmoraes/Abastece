package br.com.appviral.abastece.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.NumberFormat;

/**
 * Created by Martin on 10/06/2016.
 */
public class Calcula implements TextWatcher{

    public static float qtde_litros;
    public static float vlr_litro;
    public static float vlr_total;


    public static void calculaTerceiro(float Pqtde_litros, float Pvlr_litro, float Pvlr_total)  {
        //Chamar este método com todos os TextChangedListener desativado
        qtde_litros = Pqtde_litros;
        vlr_litro = Pvlr_litro;
        vlr_total = Pvlr_total;

        //Calcula Qtde litros
        if (vlr_total > 0 && vlr_litro > 0) {
            qtde_litros = vlr_total / vlr_litro;
        } else {

            //Calcula vlr do litro
            if (qtde_litros > 0 && vlr_total > 0) {
                vlr_litro = vlr_total / qtde_litros;
            } else {

                //Calcula vlr_Total
                if (qtde_litros > 0 && vlr_litro > 0) {
                    vlr_total = qtde_litros * vlr_litro;
                }
            }
        }

    }



    @Override
    public void afterTextChanged(Editable s) {
       /* Log.d("MEUAPP", "etQtde_litros: "+ s.toString());
        etQtde_litros.removeTextChangedListener(this);
        etQtde_litros.setText(Util.formataFloat(s.toString()));
        etQtde_litros.setSelection(etQtde_litros.length());
        etQtde_litros.addTextChangedListener(this);

        float vlr_total = Util.deStringParaFloat(etVlr_total.getText().toString());
        float vlr_litro = Util.deStringParaFloat(etVlr_litro.getText().toString());
        float qtde_litros = Util.deStringParaFloat(etQtde_litros.getText().toString());

        Calcula.calculaTerceiro(qtde_litros, vlr_litro, vlr_total);

        if (Calcula.vlr_litro != 0) {
            etVlr_litro.removeTextChangedListener(this);
            etVlr_litro.setText(Util.formataFloat(Calcula.vlr_litro));
            etVlr_litro.setSelection(etVlr_litro.length());
            etVlr_litro.addTextChangedListener(this);
        }

        if (Calcula.vlr_total != 0) {
            etVlr_total.removeTextChangedListener(this);
            etVlr_total.setText(Util.formataFloat(Calcula.vlr_total));
            etVlr_total.setSelection(etVlr_total.length());
            etVlr_total.addTextChangedListener(this);
        }        */



    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}


}
