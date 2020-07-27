package com.example.calene4;

import android.util.Log;
import android.widget.CheckBox;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PRUEBAS {
    public static void main(String[] args) {








    }













    static void secante(){
        double[] intervalo = {3*Math.PI / 2.0 , 7*Math.PI / 4.0};

        double aprox = 0;
        for(int i=0;i<10; i++){
            double f0,f1;

            f0 = 10 * Math.cos(intervalo[0]) + 4 * Math.cos(2*intervalo[0]);
            f1 = 10 * Math.cos(intervalo[1]) + 4 * Math.cos(2*intervalo[1]);


            double intervalo2  = intervalo[1] - f1 * (intervalo[0]-intervalo[1])/(f0-f1);

            intervalo[0] = intervalo[1];
            intervalo[1] = intervalo2;

            System.out.println("["+intervalo[0] +" , "+intervalo[1]+"]  "+intervalo2);

        }


    }


    static void maxMin(double[][] TRF){
        for(double[] r : TRF ){
            System.out.println( r[0] + "\t\t\t"+r[1]+"\t\t\t"+r[2] );
        }


        double[] reales = new double[512];
        double[] imaginarios = new double[512];

        int[]armonicosMaximos = {0,0};
        for(int i=0;i<512;i++){
            if(TRF[armonicosMaximos[1]][2] < TRF[i+1][2]){
                armonicosMaximos[1] = armonicosMaximos[0];
                armonicosMaximos[0] = i+1;
            }
           reales[i] = TRF[i+1][0];
           imaginarios[i] = TRF[i+1][1];
        }
















    }

















}
