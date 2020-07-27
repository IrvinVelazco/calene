package com.example.calene4.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.calene4.MainActivity;
import com.example.calene4.Medicion;
import com.example.calene4.R;
import com.google.gson.Gson;
import com.shawnlin.numberpicker.NumberPicker;

import org.w3c.dom.Text;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Configuraciones extends Fragment {

    Spinner listaIdiomas;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.configuraciones_fragment, container, false);



        configuraListaDeIdiomas(view);
        configuraSwitches(view);
        configurarDesfasamiento(view);
        configurarEscala(view);
        configurarCompensacionCd(view);



        configurarSuavizamiento(view);






        return view;
    }





    void configuraListaDeIdiomas(View view) {
        final String idiomaActual = ((MainActivity) getActivity()).conf.getString("idioma", "Español");

        Gson gson = new Gson();
        final String[] idiomasCode = gson.fromJson(((MainActivity) getActivity()).conf.getString("idiomas", "[]"), String[].class);
        final ArrayList<String> idiomas = new ArrayList<>();
        for(String idioma : idiomasCode ){
            String Lengua = (new Locale(idioma)).getDisplayLanguage();
            idiomas.add( Lengua.substring(0, 1).toUpperCase() + Lengua.substring(1) );
        }
        Collections.sort(idiomas);

        ArrayAdapter<String> adapt = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, idiomas);
        listaIdiomas = view.findViewById(R.id.listaIdiomas);
        listaIdiomas.setAdapter(adapt);
        final int indexIdiomaActual = idiomas.indexOf(idiomaActual);
        listaIdiomas.setSelection(indexIdiomaActual);

        listaIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {

                String idiomaSeleccionado = idiomas.get(position);
                if( idiomaSeleccionado.equals( idiomaActual ) ) return;

                String[] codes = Locale.getISOLanguages();
                String idiomaSeleccionadoCode = null;
                for(String code : codes){
                    Locale locale = new Locale(code);
                    if( locale.getDisplayName().equals(idiomaSeleccionado.toLowerCase()) ){
                        idiomaSeleccionadoCode = locale.getLanguage();
                    }
                }
                if(idiomaSeleccionadoCode == null) return;

                Configuration conf = getResources().getConfiguration();
                conf.locale = new Locale(idiomaSeleccionadoCode);
                DisplayMetrics metrics = new DisplayMetrics();
                ((Activity)view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                Resources resources = new Resources(view.getContext().getAssets(), metrics, conf);



                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.cambiarIdioma);
                final String finalIdiomaSeleccionadoCode = idiomaSeleccionadoCode;
                alert.setPositiveButton(R.string.si , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainActivity) getActivity()).conf.edit().putString("idiomaCode", finalIdiomaSeleccionadoCode).commit();
                        Intent refresh = new Intent(view.getContext(), MainActivity.class);
                        startActivity(refresh);
                    }
                });
                alert.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listaIdiomas.setSelection(indexIdiomaActual);
                            }
                        });

                alert.setCancelable(false);
                alert.show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    void configurarDesfasamiento(final View view) {
        final NumberPicker dva = view.findViewById(R.id.np_dva);
        final NumberPicker dvb = view.findViewById(R.id.np_dvb);
        final NumberPicker dvc = view.findViewById(R.id.np_dvc);
        final NumberPicker dia = view.findViewById(R.id.np_dia);
        final NumberPicker dib = view.findViewById(R.id.np_dib);
        final NumberPicker dic = view.findViewById(R.id.np_dic);

        dva.setValue(((MainActivity) view.getContext()).conf.getInt("dva",0));
        dvb.setValue(((MainActivity) view.getContext()).conf.getInt("dvb",0));
        dvc.setValue(((MainActivity) view.getContext()).conf.getInt("dvc",0));
        dia.setValue(((MainActivity) view.getContext()).conf.getInt("dia",0));
        dib.setValue(((MainActivity) view.getContext()).conf.getInt("dib",0));
        dic.setValue(((MainActivity) view.getContext()).conf.getInt("dic",0));



        NumberPicker.OnValueChangeListener NPOC =new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(final NumberPicker picker, final int oldVal, final int newVal) {
                if(picker.isActivated()) return;

                final String t = picker == dva ? "dva" : picker == dvb ? "dvb" : picker == dvc ? "dvc" : picker == dia ? "dia" : picker == dib ? "dib" : "dic";

                ((MainActivity) view.getContext()).conf.edit().putInt(t, newVal).commit();

            }
        };

        dva.setOnValueChangedListener(NPOC);
        dvb.setOnValueChangedListener(NPOC);
        dvc.setOnValueChangedListener(NPOC);
        dia.setOnValueChangedListener(NPOC);
        dib.setOnValueChangedListener(NPOC);
        dic.setOnValueChangedListener(NPOC);





    }





    void configuraSwitches(final View view){


        Switch desfazamiento = view.findViewById(R.id.sw_compensardesfazamiento);
        Switch escalas = view.findViewById(R.id.sw_escalasentrada);
        Switch componenteCC = view.findViewById(R.id.sw_componenteCC);
        Switch suavizar = view.findViewById(R.id.sw_s);


        View desfazamiento_v = view.findViewById(R.id.compensardesfazamientolayout);
        View escalas_v = view.findViewById(R.id.escalasentradalayout);
        View componente_v = view.findViewById(R.id.ccdlayout);
        View suavizar_v = view.findViewById(R.id.suavizar_layout);



        final Switch[] switches = {desfazamiento, escalas, componenteCC, suavizar};
        final View[] vistas = {desfazamiento_v, escalas_v, componente_v, suavizar_v};


        for(int i=0;i<switches.length;i++){
            final int finalI = i;
            switches[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        vistas[finalI].setVisibility(View.VISIBLE);
                        for(int e=0;e<switches.length; e++){
                            if(finalI != e ) switches[e].setChecked(false);
                        }
                    }else{
                        vistas[finalI].setVisibility(View.GONE);
                    }
                }
            });
        }





    }





    void configurarEscala(final View view){

        final EditText va = view.findViewById(R.id.et_eva);
        final EditText vb = view.findViewById(R.id.et_evb);
        final EditText vc = view.findViewById(R.id.et_evc);
        final EditText ia = view.findViewById(R.id.et_eia);
        final EditText ib = view.findViewById(R.id.et_eib);
        EditText ic = view.findViewById(R.id.et_eic);


        va.setText( ((MainActivity) view.getContext()).conf.getFloat("eva",0) + "" );
        vb.setText( ((MainActivity) view.getContext()).conf.getFloat("evb",0) + "" );
        vc.setText( ((MainActivity) view.getContext()).conf.getFloat("evc",0) + "" );
        ia.setText( ((MainActivity) view.getContext()).conf.getFloat("eia",0) + "" );
        ib.setText( ((MainActivity) view.getContext()).conf.getFloat("eib",0) + "" );
        ic.setText( ((MainActivity) view.getContext()).conf.getFloat("eic",0) + "" );




        class MyTextWatcher implements TextWatcher {

            private EditText editText;

            MyTextWatcher(EditText e) {
                editText = e;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) return;
                try {
                    String t = editText == va ? "eva" : editText == vb ? "evb" : editText == vc ? "evc" : editText == ia ? "eia" : editText == ib ? "eib" : "eic";
                    float val = Float.parseFloat(s.toString());
                    ((MainActivity) view.getContext()).conf.edit().putFloat(t, val).commit();
                }catch (IOError e){

                }

            }

        }

        va.addTextChangedListener(new MyTextWatcher(va));
        vb.addTextChangedListener(new MyTextWatcher(vb));
        vc.addTextChangedListener(new MyTextWatcher(vc));
        ia.addTextChangedListener(new MyTextWatcher(ia));
        ib.addTextChangedListener(new MyTextWatcher(ib));
        ic.addTextChangedListener(new MyTextWatcher(ic));

    }




    void configurarCompensacionCd(final View view){

        final EditText va = view.findViewById(R.id.et_ccdva);
        final EditText vb = view.findViewById(R.id.et_ccdvb);
        final EditText vc = view.findViewById(R.id.et_ccdvc);
        final EditText ia = view.findViewById(R.id.et_ccdia);
        final EditText ib = view.findViewById(R.id.et_ccdib);
        final EditText ic = view.findViewById(R.id.et_ccdic);


        va.setText( ((MainActivity) view.getContext()).conf.getFloat("ccdva",0) + "" );
        vb.setText( ((MainActivity) view.getContext()).conf.getFloat("ccdvb",0) + "" );
        vc.setText( ((MainActivity) view.getContext()).conf.getFloat("ccdvc",0) + "" );
        ia.setText( ((MainActivity) view.getContext()).conf.getFloat("ccdia",0) + "" );
        ib.setText( ((MainActivity) view.getContext()).conf.getFloat("ccdib",0) + "" );
        ic.setText( ((MainActivity) view.getContext()).conf.getFloat("ccdic",0) + "" );




        class MyTextWatcher implements TextWatcher {

            private EditText editText;

            MyTextWatcher(EditText e) {
                editText = e;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0 || s.toString().equals("-")) return;
                try {
                    String t = editText == va ? "ccdva" : editText == vb ? "ccdvb" : editText == vc ? "ccdvc" : editText == ia ? "ccdia" : editText == ib ? "ccdib" : "ccdic";
                    float val = Float.parseFloat(s.toString());
                    ((MainActivity) view.getContext()).conf.edit().putFloat(t, val).commit();
                }catch (IOError e){

                }

            }

        }

        va.addTextChangedListener(new MyTextWatcher(va));
        vb.addTextChangedListener(new MyTextWatcher(vb));
        vc.addTextChangedListener(new MyTextWatcher(vc));
        ia.addTextChangedListener(new MyTextWatcher(ia));
        ib.addTextChangedListener(new MyTextWatcher(ib));
        ic.addTextChangedListener(new MyTextWatcher(ic));

    }



    void configurarSuavizamiento(final View view) {
        final NumberPicker dva = view.findViewById(R.id.np_sva);
        final NumberPicker dvb = view.findViewById(R.id.np_svb);
        final NumberPicker dvc = view.findViewById(R.id.np_svc);
        final NumberPicker dia = view.findViewById(R.id.np_sia);
        final NumberPicker dib = view.findViewById(R.id.np_sib);
        final NumberPicker dic = view.findViewById(R.id.np_sic);

        dva.setValue(((MainActivity) view.getContext()).conf.getInt("sva",0));
        dvb.setValue(((MainActivity) view.getContext()).conf.getInt("svb",0));
        dvc.setValue(((MainActivity) view.getContext()).conf.getInt("svc",0));
        dia.setValue(((MainActivity) view.getContext()).conf.getInt("sia",0));
        dib.setValue(((MainActivity) view.getContext()).conf.getInt("sib",0));
        dic.setValue(((MainActivity) view.getContext()).conf.getInt("sic",0));



        NumberPicker.OnValueChangeListener NPOC =new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(final NumberPicker picker, final int oldVal, final int newVal) {
                if(picker.isActivated()) return;

                final String t = picker == dva ? "sva" : picker == dvb ? "svb" : picker == dvc ? "svc" : picker == dia ? "sia" : picker == dib ? "sib" : "sic";

                ((MainActivity) view.getContext()).conf.edit().putInt(t, newVal).commit();

            }
        };

        dva.setOnValueChangedListener(NPOC);
        dvb.setOnValueChangedListener(NPOC);
        dvc.setOnValueChangedListener(NPOC);
        dia.setOnValueChangedListener(NPOC);
        dib.setOnValueChangedListener(NPOC);
        dic.setOnValueChangedListener(NPOC);

        CheckBox IV = view.findViewById(R.id.cb_siv);

        IV.setChecked( ((MainActivity) view.getContext()).conf.getBoolean("sIV",false) );

        IV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((MainActivity) view.getContext()).conf.edit().putBoolean("sIV", isChecked).commit();
            }
        });





    }








}
