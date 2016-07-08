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

import br.com.appviral.abastece.Servico.CalculaFlex;
import br.com.appviral.abastece.Util.Dinheiro;


public class CalculaFlexFragment extends Fragment {

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

        EditText valorGasolinaEditText = (EditText) view.findViewById(R.id.valorGasolinaEditText);
        EditText valorAlcoolEditText = (EditText) view.findViewById(R.id.valorAlcoolEditText);
        TextView respostaTextView = (TextView) view.findViewById(R.id.respostaTextView);

        new CalculaFlex(valorGasolinaEditText, valorAlcoolEditText, respostaTextView);

        return view;
    }
}
