package br.com.appviral.abastece.Util;

/**
 * Created by Martin on 10/06/2016.
 */
public class CalculaTerceiro {
    public static final String VLR_TOTAL = "vlrTotal";
    public static final String VLR_LITRO = "vlrLitro";
    public static final String QTDE_LITROS = "qtdeLitros";

    private float mQtdeLitros;
    private float mVlrLitro;
    private float mVlrTotal;

    // TRUE se o valor foi digitado e FALSE se o valor foi calculado ou ainda não tem valor
    private boolean mDigitadoQtdeLitros = false;
    private boolean mDigitadoVlrLitro = false;
    private boolean mDigitadoVlrTotal = false;

    //De qual EditText veio a solicitação de calculo
    private String mEditDeOrigem;

   /* public void calcula() {
        //TODO qual a ordem de calculo - Testar em modo de alteração
        //Calcula vlr_Total
        if (mQtdeLitros > 0 && mVlrLitro > 0 && !mEditDeOrigem.equals(CalculaTerceiro.VLR_TOTAL) ) {
            mVlrTotal = mQtdeLitros * mVlrLitro;
        }

        //Calcula vlr do litro
        if (mQtdeLitros > 0 && mVlrTotal > 0 && !mEditDeOrigem.equals(CalculaTerceiro.QTDE_LITROS) ) {
            mVlrLitro = mVlrTotal / mQtdeLitros;
        }

        //Calcula Qtde litros
        if (mVlrTotal > 0 && mVlrLitro > 0 && !mEditDeOrigem.equals(CalculaTerceiro.QTDE_LITROS) ) {
            mQtdeLitros = mVlrTotal / mVlrLitro;
        }

    }*/


   public void calcula() {
        //TODO qual a ordem de calculo - Testar em modo de alteração
        //Calcula vlr_Total
        if (mQtdeLitros > 0 && mVlrLitro > 0 && !mEditDeOrigem.equals(CalculaTerceiro.VLR_TOTAL) && !mDigitadoVlrTotal) {
            mVlrTotal = mQtdeLitros * mVlrLitro;
        }

        //Calcula vlr do litro
        if (mQtdeLitros > 0 && mVlrTotal > 0 && !mEditDeOrigem.equals(CalculaTerceiro.QTDE_LITROS) && !mDigitadoVlrLitro) {
            mVlrLitro = mVlrTotal / mQtdeLitros;
        }

        //Calcula Qtde litros
        if (mVlrTotal > 0 && mVlrLitro > 0 && !mEditDeOrigem.equals(CalculaTerceiro.QTDE_LITROS) && !mDigitadoQtdeLitros) {
            mQtdeLitros = mVlrTotal / mVlrLitro;
        }

    }

    public void setQtdeLitros(float mQtdeLitros) {
        this.mQtdeLitros = mQtdeLitros;
        mDigitadoQtdeLitros = true;
        mEditDeOrigem = CalculaTerceiro.QTDE_LITROS;
        calcula();
    }

    public void setVlrLitro(float mVlrLitro) {
        this.mVlrLitro = mVlrLitro;
        mDigitadoVlrLitro = true;
        mEditDeOrigem = CalculaTerceiro.VLR_LITRO;
        calcula();
    }

    public void setVlrTotal(float mVlrTotal) {
        this.mVlrTotal = mVlrTotal;
        mDigitadoVlrTotal =true;
        mEditDeOrigem = CalculaTerceiro.VLR_TOTAL;
        calcula();
    }

    public float getQtdeLitros() {
        return mQtdeLitros;
    }

    public float getVlrLitro() {
        return mVlrLitro;
    }

    public float getVlrTotal() {
        return mVlrTotal;
    }
}
