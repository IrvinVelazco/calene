package com.example.calene4.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.calene4.MainActivity;
import com.example.calene4.Medicion;
import com.example.calene4.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Fourier extends Fragment {


    TabLayout tabLayout;
    RadioGroup radioGroup;
    BarChart chart;
    ConstraintLayout meds;

    CheckBox r,im,ma,a;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fourier_fragment, container, false);





        tabLayout = view.findViewById(R.id.tabLayout);
        radioGroup = view.findViewById(R.id.rg);
        meds = view.findViewById(R.id.medslayout);
        r = view.findViewById(R.id.cbR);
        im = view.findViewById(R.id.cbI);
        ma = view.findViewById(R.id.cbM);
        a = view.findViewById(R.id.cbA);

        chart = view.findViewById(R.id.barChart);
        chart.getDescription().setEnabled(false);
        chart.setNoDataText(getString(R.string.sindatos));
        chart.getAxisRight().disableGridDashedLine();
        chart.getAxisRight().disableAxisLineDashedLine();
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);





        List<String> labels = new ArrayList<>();
        for(int i=0;i<513;i++) labels.add(""+i);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        //chart.getXAxis().setLabelCount(1);






        grafica(view.getContext());
       Runnable grafica = new Runnable() {
            @Override
            public void run() {
                grafica(view.getContext());
                ((MainActivity) view.getContext()).Timer.postDelayed(this, 500);
            }
        };
        ((MainActivity) view.getContext()).Timer.postDelayed(grafica, 500);



        //String f = ((MainActivity) getActivity()).conf.getString("hola", "no funciono");




        return view;
    }




    void grafica(final Context context) {
        Medicion m = ((MainActivity) context).medicion;
        if(m == null) return;
        chart.clear();



        BarData chartData = new BarData();
        chartData.setBarWidth(0.10f);

        int tab = tabLayout.getSelectedTabPosition();
        int fase = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
        int Sindex = tab == 0? fase :fase +3;


        String[] Snames = {"va", "vb", "vc", "ia", "ib", "ic"};
        CheckBox[] cb = {r, im, ma, a};
        String[] labels = {context.getResources().getString(R.string.reale), context.getResources().getString(R.string.imaginarios), context.getResources().getString(R.string.magnitud), context.getResources().getString(R.string.angulo)};
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};


        for(int i=0; i<4; i++){
            if(!cb[i].isChecked()) continue;
            m.TRF(Snames[Sindex]);
            double[][][] data = {m.VA, m.VB, m.VC, m.IA, m.IB, m.IC};
            List<BarEntry> list = new ArrayList<>();
            for(int e=0; e<513; e++) {
                float valor = (float)( i<3? data[Sindex][e][i] : Math.atan(data[Sindex][e][1] / data[Sindex][e][0]));
                if(i<3 && e==0) valor /= 1024.0;
                else if(i<3 && e>0) valor /= 512.0;
                if(!(valor>=0) && !(valor<=0) || Math.abs(Math.atan( data[Sindex][e][1] ) ) < 1E-2 || Math.abs(valor) < 1E-2 ) valor =0;
                list.add(new BarEntry(e,valor));
            }
            BarDataSet ds = new BarDataSet(list, labels[i]);
            ds.setColor(colors[i]);
            chartData.addDataSet(ds);


            chartData.setValueFormatter(new ValueFormatter() {
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                DecimalFormat decimalFormat2 = new DecimalFormat("#");
                @Override
                public String getFormattedValue(float value) {
                    //return String.format("%.2f", value);
                    if(value - (int) value == 0 ) return decimalFormat2.format(value);
                    return decimalFormat.format(value);
                }
            });

        }











        if(chartData.getDataSetCount() > 0) {
            chart.setData(chartData);
            if(chartData.getDataSetCount() >= 2 ){

                int n = chartData.getDataSetCount(); // numero de series
                float groupSpace = 8/(23*n +8f);
                float barSpace =  3/(23*n +8f);
                float barWidth = 20/(23*n +8f);

                chartData.setBarWidth(barWidth);
                chart.groupBars(0,groupSpace,barSpace);

            }
            chart.setVisibleXRangeMaximum(10);
        }


        chart.invalidate();
    }










}
