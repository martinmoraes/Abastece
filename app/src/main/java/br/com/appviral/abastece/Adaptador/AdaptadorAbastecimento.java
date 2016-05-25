package br.com.appviral.abastece.Adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.R;

/**
 * Created by Martin on 25/05/2016.
 */
public class AdaptadorAbastecimento extends RecyclerView.Adapter<AdaptadorAbastecimento.meuViewHolder>{

    private List<Abastecimento> listaAbastecimentos;
    LayoutInflater layoutInflater;


    public AdaptadorAbastecimento(Context context, List<Abastecimento> lista) {
        this.listaAbastecimentos = lista;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public meuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_abastecimento, null);
        meuViewHolder umViewHolder = new meuViewHolder(view);
        return umViewHolder;
    }

    @Override
    public void onBindViewHolder(meuViewHolder holder, int position) {
        Abastecimento abastecimento = listaAbastecimentos.get(position);
        holder.tvData.setText(abastecimento.data);
        holder.tvQtdeLitros.setText(abastecimento.getQtdeLitros());
        holder.tvVlrLitro.setText(abastecimento.getVlrLitro());
        holder.tvVlrTotal.setText(abastecimento.getVlrTotal());

    }

    @Override
    public int getItemCount() {
        return listaAbastecimentos.size();
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {
        public TextView tvData, tvQtdeLitros, tvVlrTotal, tvVlrLitro;

        public meuViewHolder(View itemView) {
            super(itemView);

            tvData = (TextView) itemView.findViewById(R.id.tv_data_abastecimento);
            tvQtdeLitros = (TextView) itemView.findViewById(R.id.tv_qtde_litros);
            tvVlrTotal = (TextView) itemView.findViewById(R.id.tv_vlt_total);
            tvVlrLitro = (TextView) itemView.findViewById(R.id.tv_vlt_litro);
        }
    }
}
