package br.com.appviral.abastece;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

import br.com.appviral.abastece.Util.Util;

public class CalculoFlexActivity extends AppCompatActivity {
    EditText etVlrGasolina, etVlrAlcool;
    TextView tvResposta;
    boolean emOperacao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculoflex);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Calcula Flex");
        }

        etVlrGasolina = (EditText) findViewById(R.id.etVlr_Gasolina);
        etVlrAlcool = (EditText) findViewById(R.id.etVlr_Alcool);
        tvResposta = (TextView) findViewById(R.id.tvResposta);

//        String simbolo = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
//        etVlrGasolina.setHint(simbolo);
//        etVlrAlcool.setHint(simbolo);


        etVlrAlcool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emOperacao) {
                    emOperacao = true;
                    String str = Util.floatDeStringParaStrin(etVlrAlcool.getText().toString(), 2);
                    if (!str.equals("0,00")) //TODO deixar universal
                    etVlrAlcool.setText(str);
                    etVlrAlcool.setSelection(etVlrAlcool.length());
                    calculaFlex();
                    emOperacao = false;
                }
            }
        });

        etVlrGasolina.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emOperacao) {
                    emOperacao = true;
                    String str = Util.floatDeStringParaStrin(etVlrGasolina.getText().toString(), 2);
                    if (!str.equals("0,00")) //TODO deixar universal
                        etVlrGasolina.setText(str);
                    etVlrGasolina.setSelection(etVlrGasolina.length());
                    calculaFlex();
                    emOperacao = false;
                }
            }
        });

    }


    private void calculaFlex() {
        float vlrAlcool = Util.deStringParaFloat(etVlrAlcool.getText().toString());
        float vlrGasolina = Util.deStringParaFloat(etVlrGasolina.getText().toString());

        if (vlrAlcool > 0 & vlrGasolina > 0) {
            float resultado = vlrAlcool / vlrGasolina;
            if (resultado > 0.7) {
                tvResposta.setText("ALCOOL");
            } else {
                tvResposta.setText("GASOLINA");
            }
        }

    }
}
