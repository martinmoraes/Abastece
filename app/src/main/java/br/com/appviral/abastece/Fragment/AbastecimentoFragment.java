package br.com.appviral.abastece.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.appviral.abastece.R;


public class AbastecimentoFragment extends Fragment {


    public AbastecimentoFragment() {
        // Required empty public constructor
    }

    public static AbastecimentoFragment newInstance() {
        AbastecimentoFragment fragment = new AbastecimentoFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_abastecimento, container, false);
    }

}
