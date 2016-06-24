package br.com.appviral.abastece;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;


public class AbastecimentosFragment extends Fragment {
    private RecyclerView mRecyclerView;

    public AbastecimentosFragment() {
        Log.d("MEUAPP", "Constructor AbastecimentosFragment");
    }


    public static AbastecimentosFragment newInstance() {
        Log.d("MEUAPP", "newInstance AbastecimentosFragment");
        return new AbastecimentosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MEUAPP", "onCreate AbastecimentosFragment");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MEUAPP", "onCreateView AbastecimentosFragment");
        View view = inflater.inflate(R.layout.fragment_abastecimentos, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<Abastecimento> listaApresentar = (new AbastecimentoDAO(getActivity())).listarAbastecimentos();
        AdaptadorAbastecimento adaptadorAbastecimento = new AdaptadorAbastecimento(getActivity(), listaApresentar);
        mRecyclerView.setAdapter(adaptadorAbastecimento);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MEUAPP", "onResume AbastecimentosFragment");
        //TODO Necesário pelo inserir e alterar
        ((AdaptadorAbastecimento) mRecyclerView.getAdapter()).notifyDataSetChanged();
    }
}
