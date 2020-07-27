package com.example.calene4;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.calene4.Fragments.Configuraciones;
import com.example.calene4.Fragments.Fourier;
import com.example.calene4.Fragments.Medidas;
import com.example.calene4.Fragments.Ondas;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    public String[] idiomas = { "es", "en"};
    public SharedPreferences conf;
    public Medicion medicion;
    public Handler Timer = new Handler();

    public boolean conectado = false;

    Comunicador comunicador;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, new Medidas()).commit();
        conf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        conf.edit().putString("idiomas", gson.toJson(idiomas)).commit();
        Log.i("HOLA", "Aplicacion iniciada");


        String idiomaCode = conf.getString("idiomaCode", "es");
        Locale locale = new Locale(idiomaCode);
        String idioma = locale.getDisplayName();
        idioma = idioma.substring(0,1).toUpperCase() + idioma.substring(1);
        conf.edit().putString("idioma", idioma).commit();

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);






        ImageButton botonMedidas = findViewById(R.id.botonMedidas);
        ImageButton botonOndas = findViewById(R.id.botonOndas);
        ImageButton botonFourier = findViewById(R.id.botonFourier);
        ImageButton botonConfiguraciones = findViewById(R.id.botonConfiguraciones);


        View.OnClickListener cambiofragment = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Timer.removeCallbacksAndMessages(null);
                Fragment fragment = null;
                if( v.getId() == R.id.botonMedidas ) fragment = new Medidas();
                else if( v.getId() == R.id.botonOndas ) fragment = new Ondas();
                else if( v.getId() == R.id.botonFourier ) fragment = new Fourier();
                else if( v.getId() == R.id.botonConfiguraciones ) fragment = new Configuraciones();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, fragment).commit();
            }
        };

        botonMedidas.setOnClickListener(cambiofragment);
        botonOndas.setOnClickListener(cambiofragment);
        botonFourier.setOnClickListener(cambiofragment);
        botonConfiguraciones.setOnClickListener(cambiofragment);



//        comunicador = new Comunicador(this);

        medicion =  new Medicion( "generico", this );

    }



    @Override
    protected void onResume() {

//        comunicador.start();







        Runnable checkconnection = new Runnable() {
            @Override
            public void run() {
                if(comunicador.conectado == false){
                    comunicador.start();
                }
                Timer.postDelayed(this, 1000);
            }
        };
        //Timer.postDelayed(checkconnection, 1000);




        super.onResume();
    }




}
