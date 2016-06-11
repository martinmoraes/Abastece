package br.com.appviral.abastece.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;
import br.com.appviral.abastece.R;
import br.com.appviral.abastece.RegistraAbastecimentoActivity;

/**
 * Created by Martin on 25/05/2016.
 */
public class AdaptadorAbastecimento extends RecyclerView.Adapter<AdaptadorAbastecimento.meuViewHolder> {

    public static boolean COM_CLICK = true;
    public static boolean SEM_CLICK = false;

    private static List<Abastecimento> listaMostrada;
    private LayoutInflater layoutInflater;
    private final Context context;
    private NumberFormat nf;
    private boolean incluir_click;


    public AdaptadorAbastecimento(Context context, List<Abastecimento> lista, boolean incluir) {
        this.listaMostrada = lista;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        this.incluir_click = incluir;
    }

    @Override
    public meuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_abastecimento, null);
        final meuViewHolder umViewHolder = new meuViewHolder(view);

        return umViewHolder;
    }

    @Override
    public void onBindViewHolder(meuViewHolder holder, int position) {
        Abastecimento abastecimento = listaMostrada.get(position);

        holder.tvData.setText(abastecimento.data);
        holder.tvQtdeLitros.setText(nf.format(abastecimento.getQtdeLitros()) + " litros");
        holder.tvVlrLitro.setText(nf.format(abastecimento.getVlrLitro()) + " /litro");
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


            if (incluir_click) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RegistraAbastecimentoActivity.class);
                        intent.putExtra("OPERACAO", Abastecimento.ALTERAR);
                        intent.putExtra("POSICAO", getAdapterPosition());
                        context.startActivity(intent);
                    }
                });


                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                        MenuItem menuItem = menu.add("Excluir");
                        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(context);
                                Abastecimento umAbastecimento = listaMostrada.get(getAdapterPosition());
                                if (umAbastecimento != null) {
                                    if (abastecimentoDAO.excluir(umAbastecimento)) {
                                        notifyItemRemoved(getAdapterPosition());
                                        Toast.makeText(context, "Excluído!!!", Toast.LENGTH_SHORT).show();
                                        listaMostrada.remove(umAbastecimento);
                                    } else {
                                        Toast.makeText(context, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                return true;
                            }
                        });
                    }
                });
            }
        }
    }


    // Faz chamadas direto ao Banco. Não faz estes métodos.
    public static void adicionaAbastecimento(Abastecimento abastecimento) {
        listaMostrada.add(0, abastecimento);
    }

    public static void alteraAbastecimento(int posicao, Abastecimento umAbastecimento) {
        listaMostrada.remove(posicao);
        listaMostrada.add(posicao, umAbastecimento);
    }

    public static Abastecimento getAbastecimento(int posicao) {
        return listaMostrada.get(posicao);
    }


}
