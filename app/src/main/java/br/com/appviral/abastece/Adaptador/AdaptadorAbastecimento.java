package br.com.appviral.abastece.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.R;
import br.com.appviral.abastece.RegistraAbastecimentoActivity;

/**
 * Created by Martin on 25/05/2016.
 */
public class AdaptadorAbastecimento extends RecyclerView.Adapter<AdaptadorAbastecimento.meuViewHolder> {

    private static List<Abastecimento> listaMostrada;
    LayoutInflater layoutInflater;
    final Context context;
    NumberFormat nf;


    public AdaptadorAbastecimento(Context context, List<Abastecimento> lista) {
        this.listaMostrada = lista;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
    }

    @Override
    public meuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_abastecimento, null);
        final meuViewHolder umViewHolder = new meuViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MEUAPP", "Clicou na posição: " + umViewHolder.getAdapterPosition());
                Intent intent = new Intent(context, RegistraAbastecimentoActivity.class);
                intent.putExtra("OPERACAO", Abastecimento.ALTERAR);
                intent.putExtra("POSICAO", umViewHolder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
        return umViewHolder;
    }

    @Override
    public void onBindViewHolder(meuViewHolder holder, int position) {
        Abastecimento abastecimento = listaMostrada.get(position);

        holder.tvData.setText(abastecimento.data);
        holder.tvQtdeLitros.setText(nf.format(abastecimento.getQtdeLitros()));
        holder.tvVlrLitro.setText(nf.format(abastecimento.getVlrLitro()));
        holder.tvVlrTotal.setText(nf.format(abastecimento.getVlrTotal()));

    }

    @Override
    public int getItemCount() {
        return listaMostrada.size();
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

    public static void adicionaAbastecimento(Abastecimento abastecimento) {
        listaMostrada.add(0, abastecimento);
    }

    public static void alteraAbastecimento(int posicao, Abastecimento umAbastecimento) {
        listaMostrada.remove(posicao);
        listaMostrada.add(posicao, umAbastecimento);
    }

    public static void removeAbastecimento(Abastecimento abastecimento) {
        listaMostrada.remove(abastecimento);
    }

    public static Abastecimento getAbastecimento(int posicao) {
        return listaMostrada.get(posicao);
    }


}
