package br.com.appviral.abastece;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    }


    public static AbastecimentosFragment newInstance(String param1, String param2) {
        AbastecimentosFragment fragment = new AbastecimentosFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_abastecimentos, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<Abastecimento> listaApresentar = (new AbastecimentoDAO(getContext())).listarAbastecimentos();
        AdaptadorAbastecimento adaptadorAbastecimento = new AdaptadorAbastecimento(getContext(), listaApresentar);
        mRecyclerView.setAdapter(adaptadorAbastecimento);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdaptadorAbastecimento) mRecyclerView.getAdapter()).notifyDataSetChanged();
    }
}
