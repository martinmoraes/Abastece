package br.com.appviral.abastece;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;
import br.com.appviral.abastece.Servico.CalculaTerceiro;
import br.com.appviral.abastece.Util.Dinheiro;

public class AbastecerActivity extends AppCompatActivity implements CalculaTerceiro.OnMudaEstadoSalvarListner {

    private String mOperacao;
    private int mPosicao;
    private EditText mValorTotalEditText, mValorLitroEditText, mQuantidadeLitroEditText, mEtData;
    private Spinner mSpCombustivel;
    private DateFormat mDf;

    private boolean mSalvar = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Abastecer");
        }

        mValorTotalEditText = (EditText) findViewById(R.id.valorTotalEditTextId);
        mValorLitroEditText = (EditText) findViewById(R.id.valorLitroEditTextId);
        mQuantidadeLitroEditText = (EditText) findViewById(R.id.quantidadeLitroEditTextId);

        
        mDf = DateFormat.getDateInstance();
        mEtData = (EditText) findViewById(R.id.etData);
        mSpCombustivel = (Spinner) findViewById(R.id.spCombustivel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combustivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCombustivel.setAdapter(adapter);

        if (savedInstanceState != null) {
            mOperacao = savedInstanceState.getString("OPERACAO");
            mPosicao = savedInstanceState.getInt("POSICAO", 0);
        } else {
            mOperacao = getIntent().getStringExtra("OPERACAO");
            mPosicao = getIntent().getIntExtra("POSICAO", 0);
        }

        if (mOperacao.equals(Abastecimento.ALTERAR)) {
            Abastecimento abastecimento = AdaptadorAbastecimento.getAbastecimento(mPosicao);
            mQuantidadeLitroEditText.setText(Dinheiro.deDinheiroParaString(abastecimento.getQtdelitros()));
            mValorLitroEditText.setText(Dinheiro.deDinheiroParaString(abastecimento.getVlrLitro()));
            mValorTotalEditText.setText(Dinheiro.deDinheiroParaString(abastecimento.getVlrTotal()));
            mEtData.setText(abastecimento.getData());
            switch (abastecimento.getCombustiviel()) {
                case "Gasolina":
                    mSpCombustivel.setSelection(0);
                    break;
                case "Alcool":
                    mSpCombustivel.setSelection(1);
                    break;
                case "Diesel":
                    mSpCombustivel.setSelection(2);
                    break;
            }
            mQuantidadeLitroEditText.requestFocus();

        } else {//Prepara para um novo abastecimento
            String simbolo = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
            mQuantidadeLitroEditText.setHint(simbolo);
            mValorLitroEditText.setHint(simbolo);
            mValorTotalEditText.setHint(simbolo);

            mValorTotalEditText.requestFocus();
            mEtData.setText(mDf.format(Calendar.getInstance().getTime()));
        }

        CalculaTerceiro calculaTerceiro = new CalculaTerceiro(this);
        calculaTerceiro.setValorTotal(mValorTotalEditText, R.id.valorTatalImageButtoId);
        calculaTerceiro.setValorLitro(mValorLitroEditText, R.id.valorLitroImageButtoId);
        calculaTerceiro.setQuantidadeLitro(mQuantidadeLitroEditText, R.id.quantidadeLitroImageButtonId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registraabastecimento_toolbar, menu);

        MenuItem item = menu.findItem(R.id.salvar);
        item.setVisible(mSalvar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.salvar) {
            salva();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pegaData(View view) {
        final Calendar data = Calendar.getInstance();
        DatePickerDialog toDatePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        data.set(year, monthOfYear, dayOfMonth);
                        mEtData.setText(mDf.format(data.getTime()));

                    }
                },
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH),
                data.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva() {
        Abastecimento abastecimento = AdaptadorAbastecimento.getAbastecimento(mPosicao);
        abastecimento.setQtdelitros(Dinheiro.deDinheiroParaFloat(mQuantidadeLitroEditText.getText().toString()));
        abastecimento.setVlrLitro(Dinheiro.deDinheiroParaFloat(mValorLitroEditText.getText().toString()));
        abastecimento.setVlrTotal(Dinheiro.deDinheiroParaFloat(mValorTotalEditText.getText().toString()));
        abastecimento.setCombustivel(mSpCombustivel.getSelectedItem().toString());
        abastecimento.setData(mEtData.getText().toString());

        AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);

        switch (mOperacao) {
            case Abastecimento.INSERIR:
                long id = abastecimentoDAO.inserir(abastecimento);
                if (id > 0) {
                    abastecimento.setId(id);
                    Toast.makeText(this, "Salvo!!!", Toast.LENGTH_SHORT).show();
                    //TODO Como eleimitar este atalho
                    AdaptadorAbastecimento.adicionaAbastecimento(abastecimento);
                } else {
                    Toast.makeText(this, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case Abastecimento.ALTERAR:
                if (abastecimentoDAO.alterar(abastecimento)) {
                    Toast.makeText(this, "Salvo!!!", Toast.LENGTH_SHORT).show();
                    //TODO Como eleimitar este atalho
                    AdaptadorAbastecimento.alteraAbastecimento(mPosicao, abastecimento);
                } else {
                    Toast.makeText(this, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("OPERACAO", mOperacao);
        outState.putInt("POSICAO", mPosicao);
    }


    @Override
    public void onMudaEstadoSalvar(boolean salvar) {
        mSalvar = salvar;
        invalidateOptionsMenu();
    }
}
