package br.com.appviral.abastece.Entidade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 13/05/2016.
 */
public class Abastecimento {
    public static final String TABELA = "abastecimento";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_QTDE_LITROS = "qtde_litros";
    public static final String CAMPO_VLR_LITRO = "vlr_litro";
    public static final String CAMPO_VLR_TOTAL = "vlr_total";
    public static final String CAMPO_DATA = "data";
    public static final String CAMPO_COMBUSTIVEL = "combustivel";



    public Long id;
    public float qtde_litros;
    public float vlr_litro;
    public float vlr_total;
    public String data;
    public tipo_combustivel combustivel;


    
    public void setCombustivel(String combustivel){
        switch (combustivel){
            case "gasolina":
                this.combustivel = tipo_combustivel.gasolina;
                break;
            case "alcool":
                this.combustivel = tipo_combustivel.alcool;
                break;
            case "diesel":
                this.combustivel = tipo_combustivel.diesel;
                break;
        }
    }



    public enum tipo_combustivel  {
        gasolina, alcool, diesel;
    }

}


