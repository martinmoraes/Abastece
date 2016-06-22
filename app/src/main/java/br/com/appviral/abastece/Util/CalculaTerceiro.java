package br.com.appviral.abastece.Util;

import android.content.Context;
import android.util.Log;

import java.io.Closeable;
import java.lang.ref.ReferenceQueue;

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
        if (mQtdeLitros > 0 && mVlrLitro > 0 && !mDigitadoVlrTotal) {
            calculaVlrTotal();
        }

        //Calcula vlr do litro
        if (mQtdeLitros > 0 && mVlrTotal > 0 && !mDigitadoVlrLitro) {
            calculaVlrLitro();
        }

        //Calcula Qtde litros
        if (mVlrTotal > 0 && mVlrLitro > 0 && !mDigitadoQtdeLitros) {
           calculaQtdeLitros();
        }

    }

    public float calculaVlrTotal(){
        mVlrTotal = mQtdeLitros * mVlrLitro;
        mCalculadoVlrTotal = true;
        return  mVlrTotal;
    }

    public float calculaVlrLitro(){
        mVlrLitro = mVlrTotal / mQtdeLitros;
        mCalculadoVlrLitro = true;
        return mVlrLitro;
    }

    public float calculaQtdeLitros(){
        mQtdeLitros = mVlrTotal / mVlrLitro;
        mCalculadoQtdeLitros = true;
        return mQtdeLitros;
    }

    public void setQtdeLitros(float mQtdeLitros) {
        if (mCalculadoQtdeLitros) {
            setCalculoPorEscolha();
        } else if (mQtdeLitros > 0) {
            this.mQtdeLitros = mQtdeLitros;
            mDigitadoQtdeLitros = true;
            calcula();
        }
    }

    public void setVlrLitro(float mVlrLitro) {
        if (mCalculadoVlrLitro) {
            setCalculoPorEscolha();
        } else if (mVlrLitro > 0) {
            this.mVlrLitro = mVlrLitro;
            mDigitadoVlrLitro = true;
            calcula();
        }
    }

    public void setVlrTotal(float mVlrTotal) {
        if (mCalculadoVlrTotal) {
            setCalculoPorEscolha();
        } else if (mVlrTotal > 0) {
            this.mVlrTotal = mVlrTotal;
            mDigitadoVlrTotal = true;
            calcula();
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

    private void setCalculoPorEscolha() {
        if(!mCalculoPorEscolha) {
            mCalculoPorEscolha = true;
            mOnCalculoPorEscolhaListener.onCalculoPorEscolhaListener();
        }
    }

    public void onDetach() {
        mOnCalculoPorEscolhaListener = null;
    }


    public interface OnCalculoPorEscolhaListener {
        void onCalculoPorEscolhaListener();
    }


}
