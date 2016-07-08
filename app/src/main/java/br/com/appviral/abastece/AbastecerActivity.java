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

import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;
import br.com.appviral.abastece.Servico.CalculaTerceiro;
import br.com.appviral.abastece.Util.Dinheiro;

public class AbastecerActivity extends AppCompatActivity implements CalculaTerceiro.OnMudaEstadoSalvarListner {

    private long mId;
    private EditText mValorTotalEditText, mValorLitroEditText, mQuantidadeLitroEditText, mEtData;
    private Spinner mCombustivelSpinner;
    private DateFormat mDateFormat;
    private AbastecimentoDAO mAbastecimentoDAO;

    private boolean mSalvar = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAbastecimentoDAO = new AbastecimentoDAO(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Abastecer");
        }

        mValorTotalEditText = (EditText) findViewById(R.id.valorTotalEditTextId);
        mValorLitroEditText = (EditText) findViewById(R.id.valorLitroEditTextId);
        mQuantidadeLitroEditText = (EditText) findViewById(R.id.quantidadeLitroEditTextId);


        mDateFormat = DateFormat.getDateInstance();
        mEtData = (EditText) findViewById(R.id.etData);
        mCombustivelSpinner = (Spinner) findViewById(R.id.spCombustivel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combustivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCombustivelSpinner.setAdapter(adapter);

        if (savedInstanceState == null) {
            mId = getIntent().getLongExtra("ID", 0);
            if (mId > 0) { //ALTERAÇÃO
                Abastecimento abastecimento = mAbastecimentoDAO.getRegistroId(mId);
                mQuantidadeLitroEditText.setText(Dinheiro.deDinheiroParaString(abastecimento.getQuatidadelitros()));
                mValorLitroEditText.setText(Dinheiro.deDinheiroParaString(abastecimento.getValorLitro()));
                mValorTotalEditText.setText(Dinheiro.deDinheiroParaString(abastecimento.getValorTotal()));
                mEtData.setText(abastecimento.getData());
                switch (abastecimento.getCombustivel()) {
                    case "Gasolina":
                        mCombustivelSpinner.setSelection(0);
                        break;
                    case "Alcool":
                        mCombustivelSpinner.setSelection(1);
                        break;
                    case "Diesel":
                        mCombustivelSpinner.setSelection(2);
                        break;
                }
                mQuantidadeLitroEditText.requestFocus();

            } else {//INSERÇÃO - Prepara para um novo abastecimento
                String simbolo = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
                mValorLitroEditText.setHint(simbolo);
                mValorTotalEditText.setHint(simbolo);

                mValorTotalEditText.requestFocus();
                mEtData.setText(mDateFormat.format(Calendar.getInstance().getTime()));
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mId = savedInstanceState.getLong("ID", 0);
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
                        mEtData.setText(mDateFormat.format(data.getTime()));
                    }
                },
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH),
                data.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva() {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setId(mId);
        abastecimento.setQtdelitros(Dinheiro.deDinheiroParaFloat(mQuantidadeLitroEditText.getText().toString()));
        abastecimento.setVlrLitro(Dinheiro.deDinheiroParaFloat(mValorLitroEditText.getText().toString()));
        abastecimento.setVlrTotal(Dinheiro.deDinheiroParaFloat(mValorTotalEditText.getText().toString()));
        abastecimento.setCombustivel(mCombustivelSpinner.getSelectedItem().toString());
        abastecimento.setData(mEtData.getText().toString());


        if (mId == 0) { //ALTERAÇÃO
            if (!mAbastecimentoDAO.inserir(abastecimento))
                Toast.makeText(this, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
        } else if (!mAbastecimentoDAO.alterar(abastecimento)) //INSERÇÃO
            Toast.makeText(this, "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("ID", mId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalculaTerceiro calculaTerceiro = new CalculaTerceiro(this);
        calculaTerceiro.setValorTotal(mValorTotalEditText, R.id.valorTatalImageButtoId);
        calculaTerceiro.setValorLitro(mValorLitroEditText, R.id.valorLitroImageButtoId);
        calculaTerceiro.setQuantidadeLitro(mQuantidadeLitroEditText, R.id.quantidadeLitroImageButtonId);
    }

    @Override
    public void onMudaEstadoSalvar(boolean salvar) {
        mSalvar = salvar;
        invalidateOptionsMenu();
    }
}
