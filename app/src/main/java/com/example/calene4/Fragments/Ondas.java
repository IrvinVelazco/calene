package com.example.calene4.Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Switch;

import com.example.calene4.MainActivity;
import com.example.calene4.Medicion;
import com.example.calene4.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Ondas extends Fragment {

    Switch sw_va;
    Switch sw_vb;
    Switch sw_vc;
    Switch sw_ia;
    Switch sw_ib;
    Switch sw_ic;
    Switch sw_pa;
    Switch sw_pb;
    Switch sw_pc;
    LineChart chart;

    Runnable grafica;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ondas_fragment, container, false);





        sw_va = view.findViewById(R.id.va);
        sw_vb = view.findViewById(R.id.vb);
        sw_vc = view.findViewById(R.id.vc);
        sw_ia = view.findViewById(R.id.ia);
        sw_ib = view.findViewById(R.id.ib);
        sw_ic = view.findViewById(R.id.ic);
        sw_pa = view.findViewById(R.id.pa);
        sw_pb = view.findViewById(R.id.pc);
        sw_pc = view.findViewById(R.id.pb);


        chart = view.findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getLayoutParams().height = (int) (getActivity().getWindowManager().getDefaultDisplay().getHeight() * 0.90);
        chart.setNoDataText(getString(R.string.sindatos));



        ScrollView sv = view.findViewById(R.id.ondasscrolllayout);
        ObjectAnimator a = ObjectAnimator.ofInt(sv, "scrollY",0, 400);
        a.setDuration(1000);
        a.setStartDelay(500);
        a.start();










        grafica = new Runnable() {
            @Override
            public void run() {
                grafica(view.getContext());
                ((MainActivity) view.getContext()).Timer.postDelayed(this, 1000);
            }
        };
        ((MainActivity) view.getContext()).Timer.postDelayed(grafica, 1000);








        return view;
    }








    void grafica(Context context) {
        Medicion m = ((MainActivity) context).medicion;
        if(m == null) return;

        chart.clear();
        LineData chartData = new LineData();


        String[] labels = {"VA","VB", "VC", "IA", "IB", "IC", "PA", "PB", "PC"};
        int[] colors = {R.color.rojo1, R.color.azul1, R.color.amarillo1, R.color.rojo2, R.color.azul2,
                        R.color.amarillo2, R.color.rojo3, R.color.azul3, R.color.amarillo3 };
        double[][] señales = {m.va, m.vb, m.vc, m.ia, m.ib, m.ic, m.pa, m.pb, m.pc };
        Switch[] switches = {sw_va, sw_vb, sw_vc, sw_ia, sw_ib, sw_ic, sw_pa, sw_pb, sw_pc};

        for (int i = 0; i < 9; i++) {
            if(!switches[i].isChecked()) continue;
            List<Entry> list = new ArrayList<>();
            for(int e=0;e<1024; e++) list.add(new Entry(e, (float) señales[i][e]));

            LineDataSet lineDataSet = new LineDataSet(list, labels[i]);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            lineDataSet.setColor(ContextCompat.getColor(context, colors[i]));

            chartData.addDataSet(lineDataSet);
        }
        if(chartData.getDataSetCount() > 0) chart.setData(chartData);
        chart.invalidate();
    }







}
