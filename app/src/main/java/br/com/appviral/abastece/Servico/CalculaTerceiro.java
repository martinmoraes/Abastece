package br.com.appviral.abastece.Servico;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import br.com.appviral.abastece.R;
import br.com.appviral.abastece.Util.Dinheiro;

/**
 * Created by Martin on 25/06/2016.
 */
public class CalculaTerceiro implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener {

    private EditText mTerceiroEditText;

    private EditText mValorTotalEditText;
    private EditText mValorLitroEditText;
    private EditText mQuantidadeLitroEditText;

    private ImageButton mValorTotalImageButton;
    private ImageButton mValorLitroImageButton;
    private ImageButton mQuantidadeLitroImageButton;

    private AppCompatActivity mActivity = null;

    public CalculaTerceiro(AppCompatActivity activity) {
        mActivity = activity;
    }

    public void setValorTotal(EditText valorTotalEditText, int valotTotalImageButtonId) {
        mValorTotalEditText = valorTotalEditText;
        mValorTotalImageButton = (ImageButton) mActivity.findViewById(valotTotalImageButtonId);

        mValorTotalEditText.addTextChangedListener(this);
        mValorTotalEditText.setOnFocusChangeListener(this);

        mValorTotalImageButton.setOnClickListener(this);
    }

    public void setValorLitro(EditText valorLitroEditText, int valotLitroImageButtonId) {
        mValorLitroEditText = valorLitroEditText;
        mValorLitroImageButton = (ImageButton) mActivity.findViewById(valotLitroImageButtonId);

        mValorLitroEditText.addTextChangedListener(this);
        mValorLitroEditText.setOnFocusChangeListener(this);

        mValorLitroImageButton.setOnClickListener(this);
    }

    public void setQuantidadeLitro(EditText quantidadeLitroEditText, int quantidadeLitroImageButtonId) {
        mQuantidadeLitroEditText = quantidadeLitroEditText;
        mQuantidadeLitroImageButton = (ImageButton) mActivity.findViewById(quantidadeLitroImageButtonId);

        mQuantidadeLitroEditText.addTextChangedListener(this);
        mQuantidadeLitroEditText.setOnFocusChangeListener(this);

        mQuantidadeLitroImageButton.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        EditText editTextAtual = getEditTextAtual(s);

        povoaEditTextSemListner(editTextAtual, s.toString());

        List<EditText> editTextsVazios = pegaEditTextsVazios(editTextAtual);

        if (editTextsVazios.size() == 1 || mTerceiroEditText != null) {

            if (mTerceiroEditText == null)
                mTerceiroEditText = editTextsVazios.get(0);
            povoaEditTextSemListner(mTerceiroEditText, caluclaTerceiroEditText(mTerceiroEditText));

        } else if (editTextsVazios.size() == 0) {
            valoresInconsistentes();
        }

    }

    private void chamaOnMudaEstadoSalvarListner(boolean salvar) {
        ((OnMudaEstadoSalvarListner) mActivity).onMudaEstadoSalvar(salvar);
    }

    private void valoresInconsistentes() {
        mValorTotalImageButton.setVisibility(View.VISIBLE);
        mValorLitroImageButton.setVisibility(View.VISIBLE);
        mQuantidadeLitroImageButton.setVisibility(View.VISIBLE);
        chamaOnMudaEstadoSalvarListner(false);
    }

    private void valoresConsistentes() {
        mValorTotalImageButton.setVisibility(View.GONE);
        mValorLitroImageButton.setVisibility(View.GONE);
        mQuantidadeLitroImageButton.setVisibility(View.GONE);
        chamaOnMudaEstadoSalvarListner(true);
    }


    private void povoaEditTextSemListner(EditText editText, String valor) {
        editText.removeTextChangedListener(this);
        editText.setText(Dinheiro.deDinheiroParaDinheiro(valor));
        editText.setSelection(editText.length());
        editText.addTextChangedListener(this);
    }


    private String caluclaTerceiroEditText(EditText terceiroEditText) {
        float valorTerceiroCampo = 0;
        float total = Dinheiro.deDinheiroParaFloat(mValorTotalEditText.getText().toString()),
                valor = Dinheiro.deDinheiroParaFloat(mValorLitroEditText.getText().toString()),
                quantidade = Dinheiro.deDinheiroParaFloat(mQuantidadeLitroEditText.getText().toString());

        if (mValorTotalEditText == terceiroEditText)
            valorTerceiroCampo = quantidade * valor;
        else if (mValorLitroEditText == terceiroEditText)
            valorTerceiroCampo = total / quantidade;
        else if (mQuantidadeLitroEditText == terceiroEditText)
            valorTerceiroCampo = total / valor;
        return Dinheiro.deDinheiroParaString(valorTerceiroCampo);
    }


    private List<EditText> pegaEditTextsVazios(EditText editTextAtual) {
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(mValorTotalEditText);
        editTexts.add(mValorLitroEditText);
        editTexts.add(mQuantidadeLitroEditText);

        editTexts.remove(editTextAtual);

        List<EditText> editTextsVazios = new ArrayList<>();

        for (EditText editText : editTexts)
            if (editText.getText().toString().isEmpty())
                editTextsVazios.add(editText);
        return editTextsVazios;
    }

    private EditText getEditTextAtual(CharSequence s) {
        if (s == mValorTotalEditText.getText())
            return mValorTotalEditText;
        else if (s == mValorLitroEditText.getText())
            return mValorLitroEditText;
        else if (s == mQuantidadeLitroEditText.getText())
            return mQuantidadeLitroEditText;
        return null;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            mTerceiroEditText = null;
        }
    }


    @Override
    public void onClick(View v) {
        int imageButtonClicadoId = v.getId();
        EditText editText = null;
        switch (imageButtonClicadoId) {
            case R.id.valorTatalImageButtoId:
                editText = mValorTotalEditText;
                break;
            case R.id.valorLitroImageButtoId:
                editText = mValorLitroEditText;
                break;
            case R.id.quantidadeLitroImageButtonId:
                editText = mQuantidadeLitroEditText;
                break;
        }
        povoaEditTextSemListner(editText, caluclaTerceiroEditText(editText));

        valoresConsistentes();
    }


    public interface OnMudaEstadoSalvarListner {
        public void onMudaEstadoSalvar(boolean salvar);

    }
}
