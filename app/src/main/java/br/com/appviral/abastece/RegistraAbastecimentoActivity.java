package br.com.appviral.abastece;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistraAbastecimentoActivity extends AppCompatActivity {
    String operacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraabastecimento);
        operacao = getIntent().getStringExtra("OPERACAO");
    }



    /*
    final EditText et = (EditText)findViewById(R.id.my_edit_text);
et.setOnFocusChangeListener(new View.OnFocusChangeListener()
{
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (!hasFocus)
            // TODO: the editText has just been left
    }
});
     */


    public void salva(View view){
        switch (operacao){
            case "inserir":
                break;
            case "alterar":
                break;
        }
    }
}
