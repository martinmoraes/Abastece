package br.com.appviral.abastece;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import br.com.appviral.abastece.Util.CalculaTerceiro;
import br.com.appviral.abastece.Util.Util;

public class AbastecerActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher, CalculaTerceiro.OnCalculoPorEscolhaListener {

    private String mOperacao;
    private int mPosicao;
    private Calendar mData;
    private EditText mEtQtde_litros, mEtVlr_litro, mEtVlr_total, mEtData;
    private Spinner mSpCombustivel;
    private DateFormat mDf;
    private Boolean mInativaOnFocusChangeListener = false;

    private CalculaTerceiro mCalculaTerceiro;
    private boolean mEstaEmQtdeLitros, mEstaEmVlrTotal, mEstaEmVlrLitro;


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

        mData = Calendar.getInstance();
        mDf = DateFormat.getDateInstance();

        mEtQtde_litros = (EditText) findViewById(R.id.etQtde_litros);
        mEtVlr_litro = (EditText) findViewById(R.id.etVlr_litro);
        mEtVlr_total = (EditText) findViewById(R.id.etVlr_total);
        mEtData = (EditText) findViewById(R.id.etData);
        mSpCombustivel = (Spinner) findViewById(R.id.spCombustivel);

        String simbolo = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
        mEtQtde_litros.setHint(simbolo);
        mEtVlr_litro.setHint(simbolo);
        mEtVlr_total.setHint(simbolo);

        mEtQtde_litros.setOnFocusChangeListener(this);
        mEtVlr_litro.setOnFocusChangeListener(this);
        mEtVlr_total.setOnFocusChangeListener(this);

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
            mEtQtde_litros.setText(Util.deFloatParaString(abastecimento.getQtdelitros()));
            mEtVlr_litro.setText(Util.deFloatParaString(abastecimento.getVlrLitro()));
            mEtVlr_total.setText(Util.deFloatParaString(abastecimento.getVlrTotal()));
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
            mEtQtde_litros.requestFocus();

            // Prepara para calcula o terceiro -- float mQtdeLitros, float mVlrLitro, float mVlrTotal
            mCalculaTerceiro = new CalculaTerceiro(this, abastecimento.getQtdelitros(), abastecimento.getVlrLitro(), abastecimento.getVlrTotal());
        } else {//Prepara para um novo abastecimento
            mEtVlr_total.requestFocus();
            mData = Calendar.getInstance();
            mEtData.setText(mDf.format(mData.getTime()));
            mCalculaTerceiro = new CalculaTerceiro(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registraabastecimento_toolbar, menu);
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
        DatePickerDialog toDatePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mData.set(year, monthOfYear, dayOfMonth);
                        mEtData.setText(mDf.format(mData.getTime()));

                    }
                },
                mData.get(Calendar.YEAR),
                mData.get(Calendar.MONTH),
                mData.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva() {
        Abastecimento abastecimento = AdaptadorAbastecimento.getAbastecimento(mPosicao);
        abastecimento.setQtdelitros(Util.deStringParaFloat(mEtQtde_litros.getText().toString()));
        abastecimento.setVlrLitro(Util.deStringParaFloat(mEtVlr_litro.getText().toString()));
        abastecimento.setVlrTotal(Util.deStringParaFloat(mEtVlr_total.getText().toString()));
        abastecimento.setCombustivel(mSpCombustivel.getSelectedItem().toString());
        abastecimento.setData(mEtData.getText().toString());

        AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);

        switch (mOperacao) {
            case Abastecimento.INSERIR:
                long id = abastecimentoDAO.inserir(abastecimento);
                if (id > 0) {
                    abastecimento.setId(id);
                    Toast.makeText(this, "Salvo!!!", Toast.LENGTH_SHORT).show();
                    AdaptadorAbastecimento.adicionaAbastecimento(abastecimento);
                } else {
                    Toast.makeText(this, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case Abastecimento.ALTERAR:
                if (abastecimentoDAO.alterar(abastecimento)) {
                    Toast.makeText(this, "Salvo!!!", Toast.LENGTH_SHORT).show();
                    AdaptadorAbastecimento.alteraAbastecimento(mPosicao, abastecimento);
                } else {
                    Toast.makeText(this, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!mInativaOnFocusChangeListener) {
            int id = v.getId();
            if (hasFocus) {
                switch (id) {
                    case R.id.etVlr_total:
                        mEtVlr_total.addTextChangedListener(this);
                        mEstaEmVlrTotal = true;
                        break;
                    case R.id.etVlr_litro:
                        mEtVlr_litro.addTextChangedListener(this);
                        mEstaEmVlrLitro = true;
                        break;
                    case R.id.etQtde_litros:
                        mEtQtde_litros.addTextChangedListener(this);
                        mEstaEmQtdeLitros = true;
                        break;
                }
            } else {
                switch (id) {
                    case R.id.etVlr_total:
                        mEtVlr_total.removeTextChangedListener(this);
                        mEstaEmVlrTotal = false;
                        break;
                    case R.id.etVlr_litro:
                        mEtVlr_litro.removeTextChangedListener(this);
                        mEstaEmVlrLitro = false;
                        break;
                    case R.id.etQtde_litros:
                        mEtQtde_litros.removeTextChangedListener(this);
                        mEstaEmQtdeLitros = false;
                        break;
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void leEpreparaEditText(EditText et, String s) {
        et.removeTextChangedListener(this);
        et.setText(Util.floatDeStringParaStrin(s));
        et.setSelection(et.length());
        switch (et.getId()) {
            case R.id.etQtde_litros:
                mCalculaTerceiro.setQtdeLitros(Util.deStringParaFloat(et.getText().toString()));
                break;
            case R.id.etVlr_litro:
                mCalculaTerceiro.setVlrLitro(Util.deStringParaFloat(et.getText().toString()));
                break;
            case R.id.etVlr_total:
                mCalculaTerceiro.setVlrTotal(Util.deStringParaFloat(et.getText().toString()));
                break;
        }
        et.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (mEstaEmVlrTotal) {
            leEpreparaEditText(mEtVlr_total, s.toString());
        } else if (mEstaEmVlrLitro) {
            leEpreparaEditText(mEtVlr_litro, s.toString());
        } else if (mEstaEmQtdeLitros) {
            leEpreparaEditText(mEtQtde_litros, s.toString());
        }


        if (mCalculaTerceiro.isCalculadoVlrTotal()) {
            mEtVlr_total.setText(Util.deFloatParaString(mCalculaTerceiro.getVlrTotal()));
            mEtVlr_total.setSelection(mEtVlr_total.length());
        }

        if (mCalculaTerceiro.isCalculadoQtdeLitros()) {
            mEtQtde_litros.setText(Util.deFloatParaString(mCalculaTerceiro.getQtdeLitros()));
            mEtQtde_litros.setSelection(mEtQtde_litros.length());
        }

        if (mCalculaTerceiro.isCalculadoVlrLitro()) {
            mEtVlr_litro.setText(Util.deFloatParaString(mCalculaTerceiro.getVlrLitro()));
            mEtVlr_litro.setSelection(mEtVlr_litro.length());
        }
    }

    @Override
    public void onCalculoPorEscolhaListener() {
        //TODO redesenhar o layout para escolha do campo a ser calculado
        mEtVlr_total.removeTextChangedListener(this);
        mEtQtde_litros.removeTextChangedListener(this);
        mEtVlr_litro.removeTextChangedListener(this);
        mInativaOnFocusChangeListener = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("OPERACAO", mOperacao);
        outState.putInt("POSICAO", mPosicao);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCalculaTerceiro != null) {
            mCalculaTerceiro.onDetach();
        }
    }
}
