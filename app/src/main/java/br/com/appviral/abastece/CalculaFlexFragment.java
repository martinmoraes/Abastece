package br.com.appviral.abastece;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import br.com.appviral.abastece.Util.Util;


public class CalculaFlexFragment extends Fragment {
    EditText etVlrGasolina, etVlrAlcool;
    TextView tvResposta;
    boolean emOperacao = false;


    public CalculaFlexFragment() {
    }


    public static CalculaFlexFragment newInstance() {
        CalculaFlexFragment fragment = new CalculaFlexFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calcula_flex, container, false);

        etVlrGasolina = (EditText) view.findViewById(R.id.etVlr_Gasolina);
        etVlrAlcool = (EditText) view.findViewById(R.id.etVlr_Alcool);
        tvResposta = (TextView) view.findViewById(R.id.tvResposta);

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
                    String str = Util.floatDeStringParaString(etVlrAlcool.getText().toString(), 2);
                    if (!str.equals(Util.deFloatParaString(0f)))
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
                    String str = Util.floatDeStringParaString(etVlrGasolina.getText().toString(), 2);
                    if (!str.equals(Util.deFloatParaString(0f)))
                        etVlrGasolina.setText(str);
                    etVlrGasolina.setSelection(etVlrGasolina.length());
                    calculaFlex();
                    emOperacao = false;
                }
            }
        });

        return view;
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
