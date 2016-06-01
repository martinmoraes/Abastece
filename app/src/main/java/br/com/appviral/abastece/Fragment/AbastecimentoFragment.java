package br.com.appviral.abastece.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;
import br.com.appviral.abastece.R;


public class AbastecimentoFragment extends Fragment {
    RecyclerView recyclerView;




    public static AbastecimentoFragment newInstance(boolean incluir, String lista) {
        AbastecimentoFragment fragment = new AbastecimentoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("INCLUIR" , incluir);
        bundle.putString("LISTA" , lista);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_abastecimento, container, false);
        recyclerView = (RecyclerView) viewFragment.findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        String lista = getArguments().getString("LISTA");
        List<Abastecimento> listaApresentada = defineLista(lista);
        boolean incluir =  getArguments().getBoolean("INCLUIR");
        AdaptadorAbastecimento adaptadorAbastecimento = new AdaptadorAbastecimento(getActivity(),
                listaApresentada, incluir);
        recyclerView.setAdapter(adaptadorAbastecimento);
        return viewFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdaptadorAbastecimento) recyclerView.getAdapter()).notifyDataSetChanged();
    }

    private List<Abastecimento> defineLista(String mostrar) {
        List<Abastecimento> listaApresentada = new ArrayList<>();
        switch (mostrar) {
            case "nav_abastecimentos":
                listaApresentada = (new AbastecimentoDAO(getActivity())).listarAbastecimentos();
                break;
            case "nav_mesal":
                listaApresentada = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_MENSAL);
                break;
            case "nav_bimestral":
                listaApresentada = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_BIMESTRAL);
                break;
            case "nav_trimestral":
                listaApresentada = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_TRIMESTRAL);
                break;
            case "nav_semestral":
                listaApresentada = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_SEMESTRAL);
                break;
        }
        return listaApresentada;
    }
}
