package br.com.appviral.abastece;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;

public class RegistraAbastecimentoActivity extends AppCompatActivity implements TextWatcher, OnFocusChangeListener {

    String operacao;
    int posicao;
    Calendar data;
    EditText etQtde_litros, etVlr_litro, etVlr_total, etData;
    boolean etQtde_litrosB, etVlr_litroB, etVlr_totalB;
    RadioButton rbGasolina, rbAlcool, rbDiesel;
    DateFormat sdf;
    Abastecimento umAbastecimento = null;
    NumberFormat nf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraabastecimento);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Registra Abastecimento");
        }

        data = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        //sdf = DateFormat.getDateInstance();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        etQtde_litros = (EditText) findViewById(R.id.etQtde_litros);
        etVlr_litro = (EditText) findViewById(R.id.etVlr_litro);
        etVlr_total = (EditText) findViewById(R.id.etVlr_total);
        etData = (EditText) findViewById(R.id.etData);
        rbGasolina = (RadioButton) findViewById(R.id.rbGasolina);
        rbAlcool = (RadioButton) findViewById(R.id.rbAlcool);
        rbDiesel = (RadioButton) findViewById(R.id.rbDiesel);

//        etQtde_litros.addTextChangedListener(this);
//        etVlr_litro.addTextChangedListener(this);
//        etVlr_total.addTextChangedListener(this);

        etQtde_litros.setOnFocusChangeListener(this);
        etVlr_litro.setOnFocusChangeListener(this);
        etVlr_total.setOnFocusChangeListener(this);

        operacao = getIntent().getStringExtra("OPERACAO");
        if (operacao.equals(Abastecimento.ALTERAR)) {
            posicao = getIntent().getIntExtra("POSICAO", 0);
            mostraAbastecimento();
        } else {
            preparaUmAbastecimento();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registra_abastecimento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.as_excluir) {
            AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);
            if (umAbastecimento != null) {
                if (abastecimentoDAO.excluir(umAbastecimento)) {
                    AdaptadorAbastecimento.removeAbastecimento(umAbastecimento);
                    Toast.makeText(getApplicationContext(), "Excluído!!!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preparaUmAbastecimento() {
        etVlr_total.requestFocus();
        data = Calendar.getInstance();
        etData.setText(sdf.format(data.getTime()));

        umAbastecimento = new Abastecimento();
        umAbastecimento.setCombustivel(combustivelSelecionado());
        umAbastecimento.data = etData.getText().toString();
    }

    public void pegaData(View view) {
        DatePickerDialog toDatePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        data.set(year, monthOfYear, dayOfMonth);
                        etData.setText(sdf.format(data.getTime()));
                        umAbastecimento.data = etData.getText().toString();
                    }
                },
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH),
                data.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva(View view) {
        AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);
        umAbastecimento.setCombustivel(combustivelSelecionado());
        switch (operacao) {
            case Abastecimento.INSERIR:
                long id = abastecimentoDAO.inserir(umAbastecimento);
                if (id > 0) {
                    umAbastecimento.id = id;
                    Toast.makeText(getApplicationContext(), "Salvo!!!", Toast.LENGTH_SHORT).show();
                    AdaptadorAbastecimento.adicionaAbastecimento(umAbastecimento);
                } else {
                    Toast.makeText(getApplicationContext(), "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case Abastecimento.ALTERAR:
                if (abastecimentoDAO.alterar(umAbastecimento)) {
                    Toast.makeText(getApplicationContext(), "Salvo!!!", Toast.LENGTH_SHORT).show();
                    AdaptadorAbastecimento.alteraAbastecimento(posicao, umAbastecimento);
                } else {
                    Toast.makeText(getApplicationContext(), "Operação não realizada!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        finish();
    }

    private Abastecimento.tipo_combustivel combustivelSelecionado() {
        if (rbGasolina.isChecked()) {
            return Abastecimento.tipo_combustivel.gasolina;
        } else {
            if (rbAlcool.isChecked()) {
                return Abastecimento.tipo_combustivel.alcool;
            } else {
                if (rbDiesel.isChecked()) {
                    return Abastecimento.tipo_combustivel.diesel;
                }
            }
        }
        rbGasolina.setChecked(true);
        return Abastecimento.tipo_combustivel.gasolina;
    }

    public void mostraAbastecimento() {
        umAbastecimento = AdaptadorAbastecimento.getAbastecimento(posicao);
        etQtde_litros.setText(nf.format(umAbastecimento.qtde_litros));
        etVlr_litro.setText(nf.format(umAbastecimento.vlr_litro));
        etVlr_total.setText(nf.format(umAbastecimento.vlr_total));
        etData.setText(umAbastecimento.data);
        switch (umAbastecimento.getCombustiviel()) {
            case "gasolina":
                rbGasolina.setChecked(true);
                break;
            case "alcool":
                rbAlcool.setChecked(true);
                break;
            case "diesel":
                rbDiesel.setChecked(true);
                break;
        }
        etQtde_litros.requestFocus();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        umAbastecimento.calculaTerceiro(
                etQtde_litros.getText().toString(),
                etVlr_litro.getText().toString(),
                etVlr_total.getText().toString());
        if (umAbastecimento.isCalculou) {

            if (!etQtde_litrosB)
                etQtde_litros.setText(nf.format(umAbastecimento.qtde_litros));
            if (!etVlr_litroB)
                etVlr_litro.setText(nf.format(umAbastecimento.vlr_litro));
            if (!etVlr_totalB)
                etVlr_total.setText(nf.format(umAbastecimento.vlr_total));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            etQtde_litrosB = false;
            etVlr_litroB = false;
            etVlr_totalB = false;
            etQtde_litros.removeTextChangedListener(this);
            etVlr_total.removeTextChangedListener(this);
            etVlr_litro.removeTextChangedListener(this);
            int id = v.getId();
            switch (id) {
                case R.id.etVlr_total:
                    etVlr_totalB = true;
                    etVlr_total.addTextChangedListener(this);
                    break;
                case R.id.etVlr_litro:
                    etVlr_litroB = true;
                    etVlr_litro.addTextChangedListener(this);
                    break;
                case R.id.etQtde_litros:
                    etQtde_litrosB = true;
                    etQtde_litros.addTextChangedListener(this);
                    break;
            }
        }

    }
}
