package br.com.appviral.abastece.Adaptador;

import android.content.Context;
import android.content.Intent;
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

import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;
import br.com.appviral.abastece.R;
import br.com.appviral.abastece.AbastecerActivity;

/**
 * Created by Martin on 25/05/2016.
 */
public class AdaptadorAbastecimento extends RecyclerView.Adapter<AdaptadorAbastecimento.OViewHolder> {

    private LayoutInflater mLayoutInflater;
    private final Context mContext;
    private NumberFormat mNumberFormat;
    private AbastecimentoDAO mAbastecimentoDAO;


    public AdaptadorAbastecimento(Context context) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mNumberFormat = NumberFormat.getInstance();
        mNumberFormat.setMinimumFractionDigits(2);

        mAbastecimentoDAO = new AbastecimentoDAO(context);
    }

    @Override
    public OViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_abastecimento, null);
        final OViewHolder umViewHolder = new OViewHolder(view);
        return umViewHolder;
    }

    @Override
    public void onBindViewHolder(OViewHolder holder, int position) {
        Abastecimento abastecimento = mAbastecimentoDAO.getRegistroPosicao(position);

        holder.id = abastecimento.getId();
        holder.mDataTextView.setText(abastecimento.getData());
        holder.mQuatidadeLitrosTextView.setText(mNumberFormat.format(abastecimento.getQuatidadelitros()) + " litros");
        holder.mValorLitroTextView.setText(mNumberFormat.format(abastecimento.getValorLitro()) + " /litro");
        holder.mValorTotalTextView.setText(mNumberFormat.format(abastecimento.getValorTotal()));

    }

    @Override
    public int getItemCount() {
        return mAbastecimentoDAO.getQuantidadeRegistros();
    }


    public class OViewHolder extends RecyclerView.ViewHolder {
        private TextView mDataTextView, mQuatidadeLitrosTextView, mValorTotalTextView, mValorLitroTextView;
        private long id;

        public OViewHolder(View itemView) {
            super(itemView);

            mDataTextView = (TextView) itemView.findViewById(R.id.tv_data_abastecimento);
            mQuatidadeLitrosTextView = (TextView) itemView.findViewById(R.id.tv_qtde_litros);
            mValorTotalTextView = (TextView) itemView.findViewById(R.id.tv_vlt_total);
            mValorLitroTextView = (TextView) itemView.findViewById(R.id.tv_vlt_litro);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AbastecerActivity.class);
                    intent.putExtra("ID", id);
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
                            if (mAbastecimentoDAO.excluir(getAdapterPosition())) {
                                notifyItemRemoved(getAdapterPosition());
                            } else {
                                Toast.makeText(mContext, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    });
                }
            });
        }
    }
}
