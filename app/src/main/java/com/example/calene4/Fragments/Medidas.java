package com.example.calene4.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.calene4.MainActivity;
import com.example.calene4.Medicion;
import com.example.calene4.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Medidas extends Fragment {


    TextView medio, eficaz, max, min, frecuencia, dat, fd, cd, caim;
    TabLayout tabs;
    RadioGroup rg;
    TextView p, q, S, d, fpt, fpdes, fpdis;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.medidas_fragment, container, false);
        setScrollAnimation(view);


        eficaz = view.findViewById(R.id.tv_A_v_eficaz);
        medio = view.findViewById(R.id.tv_A_v_medio);
        max = view.findViewById(R.id.tv_max);
        min = view.findViewById(R.id.tv_A_v_min);
        frecuencia = view.findViewById(R.id.tv_A_v_frecuencia);
        dat = view.findViewById(R.id.tv_A_v_dat);
        fd = view.findViewById(R.id.tv_A_v_fd);
        cd = view.findViewById(R.id.tv_A_v_cd);
        caim = view.findViewById(R.id.tv_A_v_caim);

        p = view.findViewById(R.id.tv_p);
        q = view.findViewById(R.id.tv_q);
        d = view.findViewById(R.id.tv_d);
        S = view.findViewById(R.id.tv_s);
        fpt = view.findViewById(R.id.tv_fpt);
        fpdes = view.findViewById(R.id.tv_fpdes);
        fpdis = view.findViewById(R.id.tv_fpdis);


        tabs = view.findViewById(R.id.tabLayout);
        rg  = view.findViewById(R.id.radioGroup2);
        final RadioButton ps = view.findViewById(R.id.f3);
        final ConstraintLayout vnIlayout = view.findViewById(R.id.VnIlayout);
        final ConstraintLayout Playout = view.findViewById(R.id.PL);
        ps.setVisibility(View.GONE);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabs.getSelectedTabPosition() < 2){
                    ps.setVisibility(View.GONE);
                    vnIlayout.setVisibility(View.VISIBLE);
                    Playout.setVisibility(View.GONE);
                }else{
                    ps.setVisibility(View.VISIBLE);
                    vnIlayout.setVisibility(View.GONE);
                    Playout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });









        Runnable mide = new Runnable() {
            @Override
            public void run() {
                mide( view.getContext());

                ((MainActivity) view.getContext()).Timer.postDelayed(this, 1000);
            }
        };
        ((MainActivity) view.getContext()).Timer.postDelayed(mide, 1000);










        return view;
    }






    /*   ----------------------------------------------------------  */

    void setScrollAnimation(final View view){
        for (int i = 0; i < 20; i++) {
            int id = getResources().getIdentifier("textodesplazable_" + i, "id", Objects.requireNonNull(getActivity()).getPackageName());
            if(id == 0) continue;
            HorizontalScrollView sv =  view.findViewById(id);
            if(sv == null) continue;


            sv.setHorizontalScrollBarEnabled(false);
            int max = (int) (((TextView) sv.getChildAt(0)).getText().toString().length()*34.375 - 1097.5);
            final ObjectAnimator scrolltoleft =  ObjectAnimator.ofInt(sv, "scrollX",0, max).setDuration(5000);
            final ObjectAnimator scrolltoright = ObjectAnimator.ofInt(sv, "scrollX", max, 0).setDuration(5000);




            final AnimatorSet bouncer = new AnimatorSet();
            //bouncer.playSequentially(scrolltoright, scrolltoleft);
            bouncer.play(scrolltoright).after(scrolltoleft);
            bouncer.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bouncer.start();
                }
            });
            bouncer.start();


        }


    }





    List<Double> eficazList = new ArrayList<>();
    List<Double> medioList = new ArrayList<>();
    List<Double> maxList = new ArrayList<>();
    List<Double> minList = new ArrayList<>();
    List<Double> frecuenciaList = new ArrayList<>();
    List<Double> datList = new ArrayList<>();
    List<Double> fdList = new ArrayList<>();
    List<Double> caimList = new ArrayList<>();
    List<Double> cdList = new ArrayList<>();





    void mide(Context context){
        Medicion m = ((MainActivity) context).medicion;
        if(m == null) return;

        int VoIoP = tabs.getSelectedTabPosition();
        int fase = rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId()));

        int s = VoIoP == 0? fase : VoIoP == 1? 3+ fase : fase;

        if(VoIoP < 2) {
            double[] data = new double[9];
            if (s == 0) {
                m.ValoresDeLaOnda("va");
                m.medidasDeCalidad("va");
                data[0] = m.va_eficaz;
                data[1] = m.va_medio;
                data[2] = m.va_max;
                data[3] = m.va_min;
                data[4] = m.va_frecuencia;
                data[5] = m.va_dat;
                data[6] = m.va_fd;
                data[7] = m.va_caim;
                data[8] = m.va_cd;
            } else if (s == 1) {
                m.ValoresDeLaOnda("vb");
                m.medidasDeCalidad("vb");
                data[0] = m.vb_eficaz;
                data[1] = m.vb_medio;
                data[2] = m.vb_max;
                data[3] = m.vb_min;
                data[4] = m.vb_frecuencia;
                data[5] = m.vb_dat;
                data[6] = m.vb_fd;
                data[7] = m.vb_caim;
                data[8] = m.vb_cd;
            } else if (s == 2) {
                m.ValoresDeLaOnda("vc");
                m.medidasDeCalidad("vc");
                data[0] = m.vc_eficaz;
                data[1] = m.vc_medio;
                data[2] = m.vc_max;
                data[3] = m.vc_min;
                data[4] = m.vc_frecuencia;
                data[5] = m.vc_dat;
                data[6] = m.vc_fd;
                data[7] = m.vc_caim;
                data[8] = m.vc_cd;
            } else if (s == 3) {
                m.ValoresDeLaOnda("ia");
                m.medidasDeCalidad("ia");
                data[0] = m.ia_eficaz;
                data[1] = m.ia_medio;
                data[2] = m.ia_max;
                data[3] = m.ia_min;
                data[4] = m.ia_frecuencia;
                data[5] = m.ia_dat;
                data[6] = m.ia_fd;
                data[7] = m.ia_caim;
                data[8] = m.ia_cd;
            } else if (s == 4) {
                m.ValoresDeLaOnda("ib");
                m.medidasDeCalidad("ib");
                data[0] = m.ib_eficaz;
                data[1] = m.ib_medio;
                data[2] = m.ib_max;
                data[3] = m.ib_min;
                data[4] = m.ib_frecuencia;
                data[5] = m.ib_dat;
                data[6] = m.ib_fd;
                data[7] = m.ib_caim;
                data[8] = m.ib_cd;
            } else if (s == 5) {
                m.ValoresDeLaOnda("ic");
                m.medidasDeCalidad("ic");
                data[0] = m.ic_eficaz;
                data[1] = m.ic_medio;
                data[2] = m.ic_max;
                data[3] = m.ic_min;
                data[4] = m.ic_frecuencia;
                data[5] = m.ic_dat;
                data[6] = m.ic_fd;
                data[7] = m.ic_caim;
                data[8] = m.ic_cd;
            }

            //TODO: añadir el resto y las configuraciones
            eficazList.add(data[0]);
            if(eficazList.size()>10) eficazList.remove(0);
            medioList.add(data[1]);
            if(medioList.size()>10) medioList.remove(0);
            maxList.add(data[2]);
            if(maxList.size()>10) maxList.remove(0);
            minList.add(data[3]);
            if(minList.size()>10) minList.remove(0);
            frecuenciaList.add(data[4]);
            if(frecuenciaList.size()>10) frecuenciaList.remove(0);
            datList.add(data[5]);
            if(datList.size()>10) datList.remove(0);
            fdList.add(data[6]);
            if(fdList.size()>10) fdList.remove(0);
            caimList.add(data[7]);
            if(caimList.size()>10) caimList.remove(0);
            cdList.add(data[8]);
            if(cdList.size()>10) cdList.remove(0);


            class Promedia{
                double laLista(List<Double> l){
                    double r = 0;
                    for(int i=0; i< l.size(); i++){
                        r += l.get(i);
                    }
                    return r / l.size();
                }
            }

            Promedia promedia = new Promedia();








            DecimalFormat d = new DecimalFormat("#.########");

            /*eficaz.setText(d.format(data[0]));
            medio.setText(d.format(data[1]));
            max.setText(d.format(data[2]));
            min.setText(d.format(data[3]));
            frecuencia.setText(d.format(data[4]));
            dat.setText(d.format(data[5]));
            fd.setText(d.format(data[6]));
            caim.setText(d.format(data[7]));
            cd.setText(d.format(data[8]));*/

            eficaz.setText(d.format( promedia.laLista(eficazList) ));
            medio.setText(d.format( promedia.laLista( medioList )));
            max.setText(d.format(promedia.laLista(maxList)));
            min.setText(d.format(promedia.laLista(minList)));
            frecuencia.setText(d.format(promedia.laLista(frecuenciaList)));
            dat.setText(d.format(promedia.laLista(datList)));
            fd.setText(d.format(promedia.laLista(fdList)));
            caim.setText(d.format(promedia.laLista(caimList)));
            cd.setText(d.format(promedia.laLista(cdList)));



        }else{
            double data[] = new double[7];
            if(s == 0){
                m.potencias("a");
                data[0] = m.PA;
                data[1] = m.QA;
                data[2] = m.DA;
                data[3] = m.SA;
                data[4] = m.FPtA;
                data[5] = m.FPdesA;
                data[6] = m.FPdisA;
            }else if(s == 1){
                m.potencias("b");
                data[0] = m.PB;
                data[1] = m.QB;
                data[2] = m.DB;
                data[3] = m.SB;
                data[4] = m.FPtB;
                data[5] = m.FPdesB;
                data[6] = m.FPdisB;
            }else if(s == 2){
                m.potencias("a");
                data[0] = m.PC;
                data[1] = m.QC;
                data[2] = m.DC;
                data[3] = m.SC;
                data[4] = m.FPtC;
                data[5] = m.FPdesC;
                data[6] = m.FPdisC;
            }else if(s == 3){
                m.potencias("3");
                data[0] = m.P3;
                data[1] = m.Q3;
                data[2] = m.D3;
                data[3] = m.S3;
                data[4] = m.FPt3;
                data[5] = m.FPdes3;
                data[6] = m.FPdis3;
            }

            DecimalFormat df = new DecimalFormat("#.########");
            p.setText(df.format(data[0]));
            q.setText(df.format(data[1]));
            d.setText(df.format(data[2]));
            S.setText(df.format(data[3]));
            fpt.setText(df.format(data[4]));
            fpdes.setText(df.format(data[5]));
            fpdis.setText(df.format(data[6]));


        }







    }



}
