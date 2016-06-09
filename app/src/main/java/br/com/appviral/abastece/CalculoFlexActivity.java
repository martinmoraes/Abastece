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
    boolean bEtVlrGasolina, bEtVlrAlcool;

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
                String str = Util.formataFloat(etVlrAlcool.getText().toString(),3);
                etVlrAlcool.setText(str);
                etVlrAlcool.setSelection(etVlrAlcool.length());
                etVlrAlcool.addTextChangedListener(this);
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
            }
        });

    }


    private void calculaFlex(){
//        float vlrAlcool = etVlrAlcool.
    }





   /* @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int id = v.getId();
            switch (id) {
                case R.id.etVlr_Alcool:
                    bEtVlrAlcool = true;
                    bEtVlrGasolina = false;
                    etVlrAlcool.addTextChangedListener(this);
                    etVlrGasolina.removeTextChangedListener(this);
                    break;
                case R.id.etVlr_Gasolina:
                    bEtVlrAlcool = false;
                    bEtVlrGasolina = true;
                    etVlrGasolina.addTextChangedListener(this);
                    etVlrAlcool.removeTextChangedListener(this);
                    break;
            }
        }

    }*/
}
