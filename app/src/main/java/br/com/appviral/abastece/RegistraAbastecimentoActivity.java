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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;
import br.com.appviral.abastece.Util.Util;

public class RegistraAbastecimentoActivity extends AppCompatActivity implements TextWatcher{

    String operacao;
    int posicao;
    Calendar data;
    EditText etQtde_litros, etVlr_litro, etVlr_total, etData;
    Spinner spCombustivel;
    DateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraabastecimento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Registra Abastecimento");
            actionBar.setElevation(10f);
        }

        data = Calendar.getInstance();

        //TODO Alterar para getDateInstance
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        etQtde_litros = (EditText) findViewById(R.id.etQtde_litros);
        etVlr_litro = (EditText) findViewById(R.id.etVlr_litro);
        etVlr_total = (EditText) findViewById(R.id.etVlr_total);
        etData = (EditText) findViewById(R.id.etData);
        spCombustivel = (Spinner) findViewById(R.id.spCombustivel);

        etQtde_litros.addTextChangedListener(this);
        etVlr_litro.addTextChangedListener(this);
        etVlr_total.addTextChangedListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combustivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCombustivel.setAdapter(adapter);

        operacao = getIntent().getStringExtra("OPERACAO");
        if (operacao.equals(Abastecimento.ALTERAR)) {
            posicao = getIntent().getIntExtra("POSICAO", 0);
            mostraAbastecimento(posicao);
        } else {
            preparaUmAbastecimento();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registraabastecimento, menu);
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

    private void preparaUmAbastecimento() {
        etVlr_total.requestFocus();
        data = Calendar.getInstance();
        etData.setText(sdf.format(data.getTime()));

    }

    public void pegaData(View view) {
        DatePickerDialog toDatePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        data.set(year, monthOfYear, dayOfMonth);
                        etData.setText(sdf.format(data.getTime()));

                    }
                },
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH),
                data.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva() {
        Abastecimento umAbastecimento = new Abastecimento();
        umAbastecimento.qtde_litros = Util.deStringParaFloat(etQtde_litros.getText().toString());
        umAbastecimento.vlr_litro = Util.deStringParaFloat(etVlr_litro.getText().toString());
        umAbastecimento.vlr_total = Util.deStringParaFloat(etVlr_total.getText().toString());
        umAbastecimento.setCombustivel(spCombustivel.getSelectedItem().toString());
        umAbastecimento.data = etData.getText().toString();

        AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);

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

    public void mostraAbastecimento(int posicao) {
        Abastecimento umAbastecimento = AdaptadorAbastecimento.getAbastecimento(posicao);
        etQtde_litros.setText(Util.formataFloat(umAbastecimento.qtde_litros));
        etVlr_litro.setText(Util.formataFloat(umAbastecimento.vlr_litro));
        etVlr_total.setText(Util.formataFloat(umAbastecimento.vlr_total));
        etData.setText(umAbastecimento.data);
        switch (umAbastecimento.getCombustiviel()) {
            case "gasolina":
                spCombustivel.setSelection(0);
                break;
            case "alcool":
                spCombustivel.setSelection(1);
                break;
            case "diesel":
                spCombustivel.setSelection(2);
                break;
        }
        etQtde_litros.requestFocus();
    }

    public void calculaTerceiro() {
        //Chamar este método com todos os TextChangedListener desativado
        float vlr_total = Util.deStringParaFloat(etVlr_total.getText().toString());
        float vlr_litro = Util.deStringParaFloat(etVlr_litro.getText().toString());
        float qtde_litros = Util.deStringParaFloat(etQtde_litros.getText().toString());

        //Calcula Qtde litros
        if (vlr_total > 0 && vlr_litro > 0) {
            qtde_litros = vlr_total / vlr_litro;
            etQtde_litros.setText(Util.formataFloat(qtde_litros));
        }

        //Calcula vlr do litro
        if (qtde_litros > 0 && vlr_total > 0) {
            vlr_litro = vlr_total / qtde_litros;
            etVlr_litro.setText(Util.formataFloat(vlr_litro));
        }

        //Calcula vlr_Total
        if (qtde_litros > 0 && vlr_litro > 0) {
            vlr_total = qtde_litros * vlr_litro;
            etVlr_total.setText(Util.formataFloat(vlr_total));
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
        etQtde_litros.removeTextChangedListener(this);
        etVlr_litro.removeTextChangedListener(this);
        etVlr_total.removeTextChangedListener(this);
        calculaTerceiro();
        etQtde_litros.addTextChangedListener(this);
        etVlr_litro.addTextChangedListener(this);
        etVlr_total.addTextChangedListener(this);


//        etVlrAlcool.setText(str);
//        etVlrAlcool.setSelection(etVlrAlcool.length());
    }
}
