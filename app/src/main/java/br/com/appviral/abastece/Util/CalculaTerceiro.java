package br.com.appviral.abastece.Util;

/**
 * Created by Martin on 10/06/2016.
 */
public class CalculaTerceiro {
    private float mQtdeLitros;
    private float mVlrLitro;
    private float mVlrTotal;

    private OnCalculoPorEscolhaListener mOnCalculoPorEscolhaListener = null;
    // TRUE se o valor foi digitado
    private boolean mDigitadoQtdeLitros = false;
    private boolean mDigitadoVlrLitro = false;
    private boolean mDigitadoVlrTotal = false;

    //TRUE se o valor foi calculado
    private boolean mCalculadoQtdeLitros = false;
    private boolean mCalculadoVlrLitro = false;
    private boolean mCalculadoVlrTotal = false;

    //TRUE se o calculo tem de ser por escolha do usuário
    // Condição 1: Alteração no calculado
    // Condição 2: Alteração de um que não seja o calculado após Condição 1
    private boolean mCalculoPorEscolha = false;


    public CalculaTerceiro(OnCalculoPorEscolhaListener context) {
        mOnCalculoPorEscolhaListener = context;
    }

    public CalculaTerceiro(OnCalculoPorEscolhaListener context, float mQtdeLitros, float mVlrLitro, float mVlrTotal) {
        mOnCalculoPorEscolhaListener = context;
        this.mQtdeLitros = mQtdeLitros;
        this.mVlrLitro = mVlrLitro;
        this.mVlrTotal = mVlrTotal;
        setCalculoPorEscolha();
    }

    public void calcula() {
            //Calcula vlr_Total
            if (!mDigitadoVlrTotal) {
                calculaVlrTotal();
            }

            //Calcula vlr do litro
            if (!mDigitadoVlrLitro) {
                calculaVlrLitro();
            }

            //Calcula Qtde litros
            if (!mDigitadoQtdeLitros) {
                calculaQtdeLitros();
            }
    }

    public float calculaVlrTotal() {
        if (mQtdeLitros > 0 && mVlrLitro > 0) {
            mVlrTotal = mQtdeLitros * mVlrLitro;
            mCalculadoVlrTotal = true;
            return mVlrTotal;
        }
        return 0;
    }

    public float calculaVlrLitro() {
        if (mQtdeLitros > 0 && mVlrTotal > 0) {
            mVlrLitro = mVlrTotal / mQtdeLitros;
            mCalculadoVlrLitro = true;
            return mVlrLitro;
        }
        return 0;
    }

    public float calculaQtdeLitros() {
        if (mVlrTotal > 0 && mVlrLitro > 0) {
            mQtdeLitros = mVlrTotal / mVlrLitro;
            mCalculadoQtdeLitros = true;
            return mQtdeLitros;
        }
        return 0;
    }

    public void setQtdeLitros(float mQtdeLitros) {
        this.mQtdeLitros = mQtdeLitros;
        if (!mCalculoPorEscolha) {
            if (mCalculadoQtdeLitros) {
                mCalculadoQtdeLitros = false;
                setCalculoPorEscolha();
            } else if (mQtdeLitros > 0) {
                mDigitadoQtdeLitros = true;
                calcula();
            }
        }
    }

    public void setVlrLitro(float mVlrLitro) {
        this.mVlrLitro = mVlrLitro;
        if (!mCalculoPorEscolha) {
            if (mCalculadoVlrLitro) {
                mCalculadoVlrLitro = false;
                setCalculoPorEscolha();
            } else if (mVlrLitro > 0) {
                mDigitadoVlrLitro = true;
                calcula();
            }
        }
    }

    public void setVlrTotal(float mVlrTotal) {
        this.mVlrTotal = mVlrTotal;
        if (!mCalculoPorEscolha) {
            if (mCalculadoVlrTotal) {
                mCalculadoVlrTotal = false;
                setCalculoPorEscolha();
            } else if (mVlrTotal > 0) {
                mDigitadoVlrTotal = true;
                calcula();
            }
        }
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

    public boolean isCalculadoQtdeLitros() {
        return mCalculadoQtdeLitros;
    }

    public boolean isCalculadoVlrLitro() {
        return mCalculadoVlrLitro;
    }

    public boolean isCalculadoVlrTotal() {
        return mCalculadoVlrTotal;
    }

    public boolean isCalculoPorEscolha() {
        return mCalculoPorEscolha;
    }

    public void setTiraDeCalculoPorEscolha() {
        this.mCalculoPorEscolha = false;
        mCalculadoVlrTotal = true;
        mCalculadoVlrLitro = true;
        mCalculadoQtdeLitros = true;
    }

    private void setCalculoPorEscolha() {
        if (!mCalculoPorEscolha) {
            mCalculoPorEscolha = true;
            mOnCalculoPorEscolhaListener.onCalculoPorEscolhaListener(true);
        }
    }

    public void onDetach() {
        mOnCalculoPorEscolhaListener = null;
    }


    public interface OnCalculoPorEscolhaListener {
        void onCalculoPorEscolhaListener(boolean ativado);
    }


}
