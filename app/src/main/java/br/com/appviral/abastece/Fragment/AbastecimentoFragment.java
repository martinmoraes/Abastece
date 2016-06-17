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
    private RecyclerView mRecyclerView;
    private String mLista;
    private boolean mIncluirClick;

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
        if (getArguments() != null) {
            mLista = getArguments().getString("LISTA");
            mIncluirClick =  getArguments().getBoolean("INCLUIR");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_abastecimento, container, false);
        mRecyclerView = (RecyclerView) viewFragment.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

      //Todo Fab e toolBar sumirem ao movimentar o RecyclerView



        //TODO Tirar as collection - acesso direto ao banco

        List<Abastecimento> listaApresentar;
        listaApresentar = null;
        switch (mLista) {
            case "nav_abastecimentos":
                listaApresentar = (new AbastecimentoDAO(getActivity())).listarAbastecimentos();
                break;
         /*   case "nav_mesal":
                listaApresentar = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_MENSAL);
                break;
            case "nav_bimestral":
                listaApresentar = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_BIMESTRAL);
                break;
            case "nav_trimestral":
                listaApresentar = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_TRIMESTRAL);
                break;
            case "nav_semestral":
                listaApresentar = (new AbastecimentoDAO(getActivity())).listarMediaMensal(AbastecimentoDAO.MEDIA_SEMESTRAL);
                break;*/
        }

        AdaptadorAbastecimento adaptadorAbastecimento = new AdaptadorAbastecimento(getActivity(),
                listaApresentar, mIncluirClick);
        mRecyclerView.setAdapter(adaptadorAbastecimento);
        return viewFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdaptadorAbastecimento) mRecyclerView.getAdapter()).notifyDataSetChanged();
    }
}
