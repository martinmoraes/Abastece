package br.com.appviral.abastece;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AbastecerActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher {

    private String mOperacao;
    private int mPosicao;
    private Calendar mData;
    private EditText mEtQtde_litros, mEtVlr_litro, mEtVlr_total, mEtData;
    private Spinner mSpCombustivel;
    private DateFormat mSdf;
    private Abastecimento mAbastecimento;
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
//        getSupportActionBar().setElevation(10f);
        }

        mData = Calendar.getInstance();
        mSdf = DateFormat.getDateInstance();

        mEtQtde_litros = (EditText) findViewById(R.id.etQtde_litros);
        mEtVlr_litro = (EditText) findViewById(R.id.etVlr_litro);
        mEtVlr_total = (EditText) findViewById(R.id.etVlr_total);
        mEtData = (EditText) findViewById(R.id.etData);
        mSpCombustivel = (Spinner) findViewById(R.id.spCombustivel);

        String simbolo = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
        mEtQtde_litros.setHint(simbolo);
        mEtVlr_litro.setHint(simbolo);
        mEtVlr_total.setHint(simbolo);


        mCalculaTerceiro = new CalculaTerceiro();
        mEtQtde_litros.setOnFocusChangeListener(this);
        mEtVlr_litro.setOnFocusChangeListener(this);
        mEtVlr_total.setOnFocusChangeListener(this);

        mAbastecimento = new Abastecimento();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combustivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCombustivel.setAdapter(adapter);

        mOperacao = getIntent().getStringExtra("OPERACAO");
        if (mOperacao.equals(Abastecimento.ALTERAR)) {
            mPosicao = getIntent().getIntExtra("POSICAO", 0);
            mAbastecimento = AdaptadorAbastecimento.getAbastecimento(mPosicao);
            mEtQtde_litros.setText(Util.deFloatParaString(mAbastecimento.getQtdelitros()));
            mEtVlr_litro.setText(Util.deFloatParaString(mAbastecimento.getVlrLitro()));
            mEtVlr_total.setText(Util.deFloatParaString(mAbastecimento.getVlrTotal()));
            mEtData.setText(mAbastecimento.getData());
            switch (mAbastecimento.getCombustiviel()) {
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
        } else {//Prepara para um novo abastecimento
            mEtVlr_total.requestFocus();
            mData = Calendar.getInstance();
            mEtData.setText(mSdf.format(mData.getTime()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registraabastecimento_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
                        mEtData.setText(mSdf.format(mData.getTime()));

                    }
                },
                mData.get(Calendar.YEAR),
                mData.get(Calendar.MONTH),
                mData.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva() {

        mAbastecimento.setQtdelitros(Util.deStringParaFloat(mEtQtde_litros.getText().toString()));
        mAbastecimento.setVlrLitro(Util.deStringParaFloat(mEtVlr_litro.getText().toString()));
        mAbastecimento.setVlrTotal(Util.deStringParaFloat(mEtVlr_total.getText().toString()));
        mAbastecimento.setCombustivel(mSpCombustivel.getSelectedItem().toString());
        mAbastecimento.setData(mEtData.getText().toString());

        AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);

        switch (mOperacao) {
            case Abastecimento.INSERIR:
                long id = abastecimentoDAO.inserir(mAbastecimento);
                if (id > 0) {
                    mAbastecimento.setId(id);
                    Toast.makeText(getApplicationContext(), "Salvo!!!", Toast.LENGTH_SHORT).show();
                    AdaptadorAbastecimento.adicionaAbastecimento(mAbastecimento);
                } else {
                    Toast.makeText(getApplicationContext(), "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case Abastecimento.ALTERAR:
                if (abastecimentoDAO.alterar(mAbastecimento)) {
                    Toast.makeText(getApplicationContext(), "Salvo!!!", Toast.LENGTH_SHORT).show();
                    AdaptadorAbastecimento.alteraAbastecimento(mPosicao, mAbastecimento);
                } else {
                    Toast.makeText(getApplicationContext(), "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    @Override
    public void afterTextChanged(Editable s) {


        if(mEstaEmVlrTotal) {
            mEtVlr_total.removeTextChangedListener(this);
            mEtVlr_total.setText(Util.floatDeStringParaStrin(s.toString()));
            mEtVlr_total.setSelection(mEtVlr_total.length());
            mCalculaTerceiro.setVlrTotal(Util.deStringParaFloat(mEtVlr_total.getText().toString()));
            mEtVlr_total.addTextChangedListener(this);
        } else if(mEstaEmVlrLitro) {
            mEtVlr_litro.removeTextChangedListener(this);
            mEtVlr_litro.setText(Util.floatDeStringParaStrin(s.toString()));
            mEtVlr_litro.setSelection(mEtVlr_litro.length());
            mCalculaTerceiro.setVlrLitro(Util.deStringParaFloat(mEtVlr_litro.getText().toString()));
            mEtVlr_litro.addTextChangedListener(this);
        } else if(mEstaEmQtdeLitros) {
            mEtQtde_litros.removeTextChangedListener(this);
            mEtQtde_litros.setText(Util.floatDeStringParaStrin(s.toString()));
            mEtQtde_litros.setSelection(mEtQtde_litros.length());
            mCalculaTerceiro.setQtdeLitros(Util.deStringParaFloat(mEtQtde_litros.getText().toString()));
            mEtQtde_litros.addTextChangedListener(this);
        }


        if (mCalculaTerceiro.getVlrTotal() != 0 && !mEstaEmVlrTotal) {
            mEtVlr_total.setText(Util.deFloatParaString(mCalculaTerceiro.getVlrTotal()));
            mEtVlr_total.setSelection(mEtVlr_total.length());
        }

        if (mCalculaTerceiro.getQtdeLitros() != 0  && !mEstaEmQtdeLitros) {
            mEtQtde_litros.setText(Util.deFloatParaString(mCalculaTerceiro.getQtdeLitros()));
            mEtQtde_litros.setSelection(mEtQtde_litros.length());
        }

        if (mCalculaTerceiro.getVlrLitro() != 0 && !mEstaEmVlrLitro) {
            mEtVlr_litro.setText(Util.deFloatParaString(mCalculaTerceiro.getVlrLitro()));
            mEtVlr_litro.setSelection(mEtVlr_litro.length());
        }
    }
}
