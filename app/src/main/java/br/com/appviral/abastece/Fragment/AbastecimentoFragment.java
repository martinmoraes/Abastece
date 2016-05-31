package br.com.appviral.abastece.Fragment;

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
import br.com.appviral.abastece.R;


public class AbastecimentoFragment extends Fragment {
    RecyclerView recyclerView;
    List<Abastecimento> listaApresentada;

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
        View view = inflater.inflate(R.layout.fragment_abastecimento, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        listaApresentada = (new AbastecimentoDAO(getActivity())).listarAbastecimentos();

        AdaptadorAbastecimento adaptadorAbastecimento = new AdaptadorAbastecimento(getActivity(), listaApresentada);
        recyclerView.setAdapter(adaptadorAbastecimento);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdaptadorAbastecimento) recyclerView.getAdapter()).notifyDataSetChanged();
    }

    public void alteraLista(String mostrar) {
        listaApresentada.removeAll(listaApresentada);
        switch (mostrar) {
            case "nav_abastecimentos":
                listaApresentada.addAll((new AbastecimentoDAO(getActivity())).listarAbastecimentos());
                break;
            case "nav_sobre":
                break;
            case "nav_mesal":
                listaApresentada.addAll((new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_MENSAL));
                break;
            case "nav_bimestral":
                listaApresentada.addAll((new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_BIMESTRAL));
                break;
            case "nav_trimestral":
                listaApresentada.addAll((new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_TRIMESTRAL));
                break;
            case "nav_semestral":
                listaApresentada.addAll((new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_SEMESTRAL));
                break;
        }
        ((AdaptadorAbastecimento) recyclerView.getAdapter()).notifyDataSetChanged();
    }
}
