package br.com.appviral.abastece.Servico;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import br.com.appviral.abastece.R;
import br.com.appviral.abastece.Util.Dinheiro;

/**
 * Created by Martin on 07/07/2016.
 */
public class CalculaFlex implements TextWatcher {
    EditText mValorGasolinaEditText, mValorAlcoolEditText;
    TextView mRespostaTextView;


    public CalculaFlex(EditText valorGasolinaEditText, EditText valorAlcoolEditText, TextView respostaTextView){
        mValorGasolinaEditText = valorGasolinaEditText;
        mValorAlcoolEditText = valorAlcoolEditText;
        mRespostaTextView = respostaTextView;

        mValorGasolinaEditText.addTextChangedListener(this);
        mValorAlcoolEditText.addTextChangedListener(this);
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

        float valorAlcool = Dinheiro.deDinheiroParaFloat(mValorAlcoolEditText.getText().toString());
        float valorGasolina = Dinheiro.deDinheiroParaFloat(mValorGasolinaEditText.getText().toString());

        if (valorAlcool > 0 & valorGasolina > 0)
            mRespostaTextView.setText(determinaFlex(valorAlcool, valorGasolina));
    }

    private void povoaEditTextSemListner(EditText editText, String valor) {
        editText.removeTextChangedListener(this);
        editText.setText(Dinheiro.deDinheiroParaDinheiro(valor));
        editText.setSelection(editText.length());
        editText.addTextChangedListener(this);
    }

    private EditText getEditTextAtual(CharSequence s) {
        if (s == mValorGasolinaEditText.getText())
            return mValorGasolinaEditText;
        else if (s == mValorAlcoolEditText.getText())
            return mValorAlcoolEditText;
        return null;
    }


    private String determinaFlex(float valorAlcool, float valorGasolina) {
        float resultado = valorAlcool / valorGasolina;
        if (resultado <= 0.7)
            return "ALCOOL";
        else
            return "GASOLINA";
    }


}
