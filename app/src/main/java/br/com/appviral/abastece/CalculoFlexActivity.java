package br.com.appviral.abastece;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.appviral.abastece.Util.Util;

public class CalculoFlexActivity extends AppCompatActivity {
    EditText etVlrGasolina, etVlrAlcool;
    TextView tvResposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculoflex);

        etVlrGasolina = (EditText) findViewById(R.id.etVlr_Gasolina);
        etVlrAlcool = (EditText) findViewById(R.id.etVlr_Alcool);
        tvResposta = (TextView) findViewById(R.id.tvResposta);

        etVlrAlcool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etVlrAlcool.removeTextChangedListener(this);
                String str = Util.formataFloat(etVlrAlcool.getText().toString(),2);
                etVlrAlcool.setText(str);
                etVlrAlcool.setSelection(etVlrAlcool.length());
                etVlrAlcool.addTextChangedListener(this);
                calculaFlex();
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
                etVlrGasolina.removeTextChangedListener(this);
                String str = Util.formataFloat(etVlrGasolina.getText().toString(),2);
                etVlrGasolina.setText(str);
                etVlrGasolina.setSelection(etVlrGasolina.length());
                etVlrGasolina.addTextChangedListener(this);
                calculaFlex();
            }
        });

    }


    private void calculaFlex(){
        float vlrAlcool = Util.deStringParaFloat(etVlrAlcool.getText().toString());
        float vlrGasolina = Util.deStringParaFloat(etVlrGasolina.getText().toString());

        if(vlrAlcool > 0 & vlrGasolina > 0) {
            float resultado = vlrAlcool / vlrGasolina;
            if (resultado > 0.7) {
                tvResposta.setText("ALCOOL");
            } else {
                tvResposta.setText("GASOLINA");
            }
        }

    }
}
