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
import android.widget.ImageButton;
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

public class AbastecerActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        TextWatcher, CalculaTerceiro.OnCalculoPorEscolhaListener, View.OnClickListener {

    private String mOperacao;
    private int mPosicao;
    private EditText mEtQtde_litros, mEtVlr_litro, mEtVlr_total, mEtData;
    private Spinner mSpCombustivel;
    private DateFormat mDf;

    private CalculaTerceiro mCalculaTerceiro;
    private int mEstaNoEditText;
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


        mDf = DateFormat.getDateInstance();

        mEtQtde_litros = (EditText) findViewById(R.id.etQtde_litros);
        mEtVlr_litro = (EditText) findViewById(R.id.etVlr_litro);
        mEtVlr_total = (EditText) findViewById(R.id.etVlr_total);
        mEtData = (EditText) findViewById(R.id.etData);
        mSpCombustivel = (Spinner) findViewById(R.id.spCombustivel);

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

            mCalculaTerceiro = new CalculaTerceiro(this, abastecimento.getQtdelitros(), abastecimento.getVlrLitro(), abastecimento.getVlrTotal());
        } else {//Prepara para um novo abastecimento
            String simbolo = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
            mEtQtde_litros.setHint(simbolo);
            mEtVlr_litro.setHint(simbolo);
            mEtVlr_total.setHint(simbolo);

            mEtVlr_total.requestFocus();
            mEtData.setText(mDf.format(Calendar.getInstance().getTime()));
            mCalculaTerceiro = new CalculaTerceiro(this);
        }
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
    public void onFocusChange(View v, boolean hasFocus) {
        mEstaNoEditText = v.getId();
        if (hasFocus) {
            switch (mEstaNoEditText) {
                case R.id.etVlr_total:
                    mEtVlr_total.addTextChangedListener(this);
                    break;
                case R.id.etVlr_litro:
                    mEtVlr_litro.addTextChangedListener(this);
                    break;
                case R.id.etQtde_litros:
                    mEtQtde_litros.addTextChangedListener(this);
                    break;
            }
        } else {
            switch (mEstaNoEditText) {
                case R.id.etVlr_total:
                    mEtVlr_total.removeTextChangedListener(this);
                    break;
                case R.id.etVlr_litro:
                    mEtVlr_litro.removeTextChangedListener(this);
                    break;
                case R.id.etQtde_litros:
                    mEtQtde_litros.removeTextChangedListener(this);
                    break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void afterTextChanged(Editable s) {
        EditText editText = null;

        switch (mEstaNoEditText) {
            case R.id.etVlr_total:
                editText = mEtVlr_total;
                mCalculaTerceiro.setVlrTotal(Util.deStringParaFloat(s.toString()));
                break;
            case R.id.etVlr_litro:
                editText = mEtVlr_litro;
                mCalculaTerceiro.setVlrLitro(Util.deStringParaFloat(s.toString()));
                break;
            case R.id.etQtde_litros:
                editText = mEtQtde_litros;
                mCalculaTerceiro.setQtdeLitros(Util.deStringParaFloat(s.toString()));
        }

        editText.removeTextChangedListener(this);
        editText.setText(Util.floatDeStringParaString(s.toString()));
        editText.setSelection(editText.length());

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

        editText.addTextChangedListener(this);
    }

    @Override
    public void onCalculoPorEscolhaListener(boolean ativado) {
        Log.d("MEUAPP", "onCalculoPorEscolhaListener");
        ImageButton ibVlrTotal = (ImageButton) findViewById(R.id.ibValorTotal);
        ImageButton ibVlrLitro = (ImageButton) findViewById(R.id.ibValorLitro);
        ImageButton ibQtdeLitro = (ImageButton) findViewById(R.id.ibQtdeLitro);
        ImageButton ibCombustivel = (ImageButton) findViewById(R.id.ibCombustivel);
        ImageButton ibData = (ImageButton) findViewById(R.id.ibData);

        if (ativado) {
            mEtVlr_total.removeTextChangedListener(this);
            mEtQtde_litros.removeTextChangedListener(this);
            mEtVlr_litro.removeTextChangedListener(this);

            mSalvar = false;
            invalidateOptionsMenu();

            ibVlrLitro.setOnClickListener(this);
            ibVlrLitro.setVisibility(View.VISIBLE);

            ibVlrTotal.setOnClickListener(this);
            ibVlrTotal.setVisibility(View.VISIBLE);

            ibQtdeLitro.setOnClickListener(this);
            ibQtdeLitro.setVisibility(View.VISIBLE);

            ibCombustivel.setVisibility(View.VISIBLE);
            ibData.setVisibility(View.VISIBLE);



        } else {
            mSalvar = true;
            invalidateOptionsMenu();
            ibVlrLitro.setVisibility(View.GONE);
            ibVlrTotal.setVisibility(View.GONE);
            ibQtdeLitro.setVisibility(View.GONE);
            ibCombustivel.setVisibility(View.GONE);
            ibData.setVisibility(View.GONE);
            mCalculaTerceiro.setTiraDeCalculoPorEscolha();
        }
    }


    @Override
    public void onClick(View v) {
        EditText editText = (EditText) findViewById(mEstaNoEditText);
        editText.removeTextChangedListener(this);
        int id = v.getId();
        switch (id) {
            case R.id.ibValorTotal:
                mEtVlr_total.setText(Util.deFloatParaString(mCalculaTerceiro.calculaVlrTotal()));
                mEtVlr_total.setSelection(mEtVlr_total.length());
                mEtVlr_total.requestFocus();
                mEtVlr_total.addTextChangedListener(this);
                mEstaNoEditText = mEtVlr_total.getId();
                break;
            case R.id.ibValorLitro:
                mEtVlr_litro.setText(Util.deFloatParaString(mCalculaTerceiro.calculaVlrLitro()));
                mEtVlr_litro.setSelection(mEtVlr_litro.length());
                mEtVlr_litro.requestFocus();
                mEtVlr_litro.addTextChangedListener(this);
                mEstaNoEditText = mEtVlr_litro.getId();
                break;
            case R.id.ibQtdeLitro:
                mEtQtde_litros.setText(Util.deFloatParaString(mCalculaTerceiro.calculaQtdeLitros()));
                mEtQtde_litros.setSelection(mEtQtde_litros.length());
                mEtQtde_litros.requestFocus();
                mEtQtde_litros.addTextChangedListener(this);
                mEstaNoEditText = mEtQtde_litros.getId();
                break;
        }
        onCalculoPorEscolhaListener(false);
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
