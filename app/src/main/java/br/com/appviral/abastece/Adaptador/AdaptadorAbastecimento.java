package br.com.appviral.abastece.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import br.com.appviral.abastece.AbastecerActivity;

/**
 * Created by Martin on 25/05/2016.
 */
public class AdaptadorAbastecimento extends RecyclerView.Adapter<AdaptadorAbastecimento.OViewHolder> {

    public static boolean COM_CLICK = true;
    public static boolean SEM_CLICK = false;

    private static List<Abastecimento> mListaMostrar;
    private LayoutInflater mLayoutInflater;
    private final Context mContext;
    private NumberFormat mNF;
    private boolean mIncluir_click;


    public AdaptadorAbastecimento(Context context, List<Abastecimento> lista, boolean incluir) {
        this.mListaMostrar = lista;
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mNF = NumberFormat.getInstance();
        mNF.setMinimumFractionDigits(2);
        this.mIncluir_click = incluir;
    }

    @Override
    public OViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_abastecimento, null);
        final OViewHolder umViewHolder = new OViewHolder(view);
        return umViewHolder;
    }

    @Override
    public void onBindViewHolder(OViewHolder holder, int position) {
        Abastecimento abastecimento = mListaMostrar.get(position);

        holder.tvData.setText(abastecimento.getData());
        holder.tvQtdeLitros.setText(mNF.format(abastecimento.getQtdelitros()) + " litros");
        holder.tvVlrLitro.setText(mNF.format(abastecimento.getVlrLitro()) + " /litro");
        holder.tvVlrTotal.setText(mNF.format(abastecimento.getVlrTotal()));

    }

    @Override
    public int getItemCount() {
        return mListaMostrar.size();
    }


    public class OViewHolder extends RecyclerView.ViewHolder {
        public TextView tvData, tvQtdeLitros, tvVlrTotal, tvVlrLitro;

        public OViewHolder(View itemView) {
            super(itemView);

            tvData = (TextView) itemView.findViewById(R.id.tv_data_abastecimento);
            tvQtdeLitros = (TextView) itemView.findViewById(R.id.tv_qtde_litros);
            tvVlrTotal = (TextView) itemView.findViewById(R.id.tv_vlt_total);
            tvVlrLitro = (TextView) itemView.findViewById(R.id.tv_vlt_litro);


            if (mIncluir_click) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AbastecerActivity.class);
                        intent.putExtra("OPERACAO", Abastecimento.ALTERAR);
                        intent.putExtra("POSICAO", getAdapterPosition());
                        mContext.startActivity(intent);
                    }
                });


                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                        MenuItem menuItem = menu.add("Excluir");
                        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(mContext);
                                Abastecimento umAbastecimento = mListaMostrar.get(getAdapterPosition());
                                if (umAbastecimento != null) {
                                    if (abastecimentoDAO.excluir(umAbastecimento)) {
                                        mListaMostrar.remove(umAbastecimento);
                                        notifyItemRemoved(getAdapterPosition());//TODO ver se faz falta desta lista
                                        Toast.makeText(mContext, "Excluído!!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
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


    // TODO Faz chamadas direto ao Banco. Não faz estes métodos.
    public static void adicionaAbastecimento(Abastecimento abastecimento) {
        mListaMostrar.add(0, abastecimento);
    }

    public static void alteraAbastecimento(int posicao, Abastecimento umAbastecimento) {
        mListaMostrar.remove(posicao);
        mListaMostrar.add(posicao, umAbastecimento);
    }

    public static Abastecimento getAbastecimento(int posicao) {
        return mListaMostrar.get(posicao);
    }


}
