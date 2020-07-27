package com.example.calene4;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

public class Medicion {
    public double va[] = new double[1024];
    public double vb[] = new double[1024];
    public double vc[] = new double[1024];
    public double ia[] = new double[1024];
    public double ib[] = new double[1024];
    public double ic[] = new double[1024];

    public double vax[] = new double[1536];
    public double vbx[] = new double[1536];
    public double vcx[] = new double[1536];
    public double iax[] = new double[1536];
    public double ibx[] = new double[1536];
    public double icx[] = new double[1536];

    public double pa[] = new double[1024];
    public double pb[] = new double[1024];
    public double pc[] = new double[1024];


    public double VA[][];
    public double VB[][];
    public double VC[][];
    public double IA[][];
    public double IB[][];
    public double IC[][];


    public double va_eficaz, va_medio, va_max, va_min, va_frecuencia;
    public double vb_eficaz, vb_medio, vb_max, vb_min, vb_frecuencia;
    public double vc_eficaz, vc_medio, vc_max, vc_min, vc_frecuencia;
    public double ia_eficaz, ia_medio, ia_max, ia_min, ia_frecuencia;
    public double ib_eficaz, ib_medio, ib_max, ib_min, ib_frecuencia;
    public double ic_eficaz, ic_medio, ic_max, ic_min, ic_frecuencia;

    public  double va_dat, va_fd, va_cd, va_caim;
    public  double vb_dat, vb_fd, vb_cd, vb_caim;
    public  double vc_dat, vc_fd, vc_cd, vc_caim;
    public  double ia_dat, ia_fd, ia_cd, ia_caim;
    public  double ib_dat, ib_fd, ib_cd, ib_caim;
    public  double ic_dat, ic_fd, ic_cd, ic_caim;

    public double PA, QA, SA, DA, FPtA, FPdesA, FPdisA;
    public double PB, QB, SB, DB, FPtB, FPdesB, FPdisB;
    public double PC, QC, SC, DC, FPtC, FPdesC, FPdisC;
    public double P3, Q3, S3, D3, FPt3, FPdes3, FPdis3;


    Context context;


    public Medicion(int[] lecturas, Context c){

        context = c;

        float eva = ((MainActivity) context).conf.getFloat("eva", 0 );
        float evb = ((MainActivity) context).conf.getFloat("evb", 0 );
        float evc = ((MainActivity) context).conf.getFloat("evc", 0 );
        float eia = ((MainActivity) context).conf.getFloat("eia", 0 );
        float eib = ((MainActivity) context).conf.getFloat("eib", 0 );
        float eic = ((MainActivity) context).conf.getFloat("eic", 0 );

        //compensar corriente directa
        float ccdva = ((MainActivity) context).conf.getFloat("ccdva", 0 );
        float ccdvb = ((MainActivity) context).conf.getFloat("ccdvb", 0 );
        float ccdvc = ((MainActivity) context).conf.getFloat("ccdvc", 0 );
        float ccdia = ((MainActivity) context).conf.getFloat("ccdia", 0 );
        float ccdib = ((MainActivity) context).conf.getFloat("ccdib", 0 );
        float ccdic = ((MainActivity) context).conf.getFloat("ccdic", 0 );


        for(int i=0; i<1536; i++){
            vax[i] = ((double)lecturas[6*i + 0 ] - 2048.0 ) * (eva * Math.sqrt(2) / 2048.0) - ccdva;
            vbx[i] = ((double)lecturas[6*i + 1 ] - 2048.0 ) * (evb * Math.sqrt(2) / 2048.0) - ccdvb;
            vcx[i] = ((double)lecturas[6*i + 2 ] - 2048.0 ) * (evc * Math.sqrt(2) / 2048.0) - ccdvc;
            iax[i] = ((double)lecturas[6*i + 3 ] - 2048.0 ) * (eia * Math.sqrt(2) / 2048.0) - ccdia;
            ibx[i] = ((double)lecturas[6*i + 4 ] - 2048.0 ) * (eib * Math.sqrt(2) / 2048.0) - ccdib;
            icx[i] = ((double)lecturas[6*i + 5 ] - 2048.0 ) * (eic * Math.sqrt(2) / 2048.0) - ccdic;
        }

        int dva = ((MainActivity) context).conf.getInt("dva",0);
        int dvb = ((MainActivity) context).conf.getInt("dvb",0);
        int dvc = ((MainActivity) context).conf.getInt("dvc",0);
        int dia = ((MainActivity) context).conf.getInt("dia",0);
        int dib = ((MainActivity) context).conf.getInt("dib",0);
        int dic = ((MainActivity) context).conf.getInt("dic",0);


        for(int i=0; i<1024; i++){
            va[i] = vax[i + dva];
            vb[i] = vbx[i+ dvb] ;
            vc[i] = vcx[i +dvc] ;
            ia[i] = iax[i +dia] ;
            ib[i] = ibx[i +dib] ;
            ic[i] = icx[i +dic] ;
        }





        //suavizar
        int sva = ((MainActivity) context).conf.getInt("sva",0);
        int svb = ((MainActivity) context).conf.getInt("svb",0);
        int svc = ((MainActivity) context).conf.getInt("svc",0);
        int sia = ((MainActivity) context).conf.getInt("sia",0);
        int sib = ((MainActivity) context).conf.getInt("sib",0);
        int sic = ((MainActivity) context).conf.getInt("sic",0);
        boolean IV = ((MainActivity) context).conf.getBoolean("sIV",false);


        final boolean finalIV = IV;
        class suaviza{
            void laSeñal(double[] señal, int grado){
                if(grado == 0) return;
                for(int i=grado; i<1024 - grado; i++){
                    double t = 0;
                    for(int e = -1 * grado; e <= grado; e++){
                        if( e==0 && finalIV == false )continue;
                        t += señal[i + e];
                    }
                    t /= finalIV ? 2*grado +1 : 2 * grado;
                    señal[i] = t;
                }
            }
        }

        new suaviza().laSeñal(va, sva);
        new suaviza().laSeñal(vb, svb);
        new suaviza().laSeñal(vc, svc);
        new suaviza().laSeñal(ia, sia);
        new suaviza().laSeñal(ib, sib);
        new suaviza().laSeñal(ic, sic);









        setPotencias();
    }






    public void setPotencias(){
        for(int i=0; i<1024;i++){
            pa[i] = va[i] * ia[i];
            pb[i] = vb[i] * ib[i];
            pc[i] = vc[i] * ic[i];
        }
    }





    public void TRF(String nombreDeLaSeñalDiscreta){
        double[] señal = SeñalDiscretaValida( nombreDeLaSeñalDiscreta );


        double[] valoresreales = Arrays.copyOf(señal,1024 );
        double[] valoresimaginarios = new double[1024];
        double[][] resultadosordenados = new double[1024][3];



        // pre calculo de la lista de ordenamiento
//       int[] lista_ordenamiento = new int[1024];
//        lista_ordenamiento[0] = 0;
//        for(int i=1; i<=10; i++) {
//            for(int e=0; e<Math.pow(2,i)/2; e++) {
//                lista_ordenamiento[e] = lista_ordenamiento[e] * 2;
//            }
//            for(int e=0; e<pow(2,i)/2; e++) {
//                lista_ordenamiento[(int)pow(2,i)/2 + e] = lista_ordenamiento[e] + 1;
//            }
//
//        }

        int[] lista_ordenamiento = {0,512,256,768,128,640,384,896,64,576,320,832,192,704,448,960,32,544,288,800,160,672,416,928,96,608,352,864,224,736,480,992,
                16,528,272,784,144,656,400,912,80,592,336,848,208,720,464,976,48,560,304,816,176,688,432,944,112,624,368,880,240,752,496,1008,
                8,520,264,776,136,648,392,904,72,584,328,840,200,712,456,968,40,552,296,808,168,680,424,936,104,616,360,872,232,744,488,1000,
                24,536,280,792,152,664,408,920,88,600,344,856,216,728,472,984,56,568,312,824,184,696,440,952,120,632,376,888,248,760,504,1016,
                4,516,260,772,132,644,388,900,68,580,324,836,196,708,452,964,36,548,292,804,164,676,420,932,100,612,356,868,228,740,484,996,
                20,532,276,788,148,660,404,916,84,596,340,852,212,724,468,980,52,564,308,820,180,692,436,948,116,628,372,884,244,756,500,1012,
                12,524,268,780,140,652,396,908,76,588,332,844,204,716,460,972,44,556,300,812,172,684,428,940,108,620,364,876,236,748,492,1004,
                28,540,284,796,156,668,412,924,92,604,348,860,220,732,476,988,60,572,316,828,188,700,444,956,124,636,380,892,252,764,508,1020,
                2,514,258,770,130,642,386,898,66,578,322,834,194,706,450,962,34,546,290,802,162,674,418,930,98,610,354,866,226,738,482,994,
                18,530,274,786,146,658,402,914,82,594,338,850,210,722,466,978,50,562,306,818,178,690,434,946,114,626,370,882,242,754,498,1010,
                10,522,266,778,138,650,394,906,74,586,330,842,202,714,458,970,42,554,298,810,170,682,426,938,106,618,362,874,234,746,490,1002,
                26,538,282,794,154,666,410,922,90,602,346,858,218,730,474,986,58,570,314,826,186,698,442,954,122,634,378,890,250,762,506,1018,
                6,518,262,774,134,646,390,902,70,582,326,838,198,710,454,966,38,550,294,806,166,678,422,934,102,614,358,870,230,742,486,998,
                22,534,278,790,150,662,406,918,86,598,342,854,214,726,470,982,54,566,310,822,182,694,438,950,118,630,374,886,246,758,502,1014,
                14,526,270,782,142,654,398,910,78,590,334,846,206,718,462,974,46,558,302,814,174,686,430,942,110,622,366,878,238,750,494,1006,
                30,542,286,798,158,670,414,926,94,606,350,862,222,734,478,990,62,574,318,830,190,702,446,958,126,638,382,894,254,766,510,1022,
                1,513,257,769,129,641,385,897,65,577,321,833,193,705,449,961,33,545,289,801,161,673,417,929,97,609,353,865,225,737,481,993,
                17,529,273,785,145,657,401,913,81,593,337,849,209,721,465,977,49,561,305,817,177,689,433,945,113,625,369,881,241,753,497,1009,
                9,521,265,777,137,649,393,905,73,585,329,841,201,713,457,969,41,553,297,809,169,681,425,937,105,617,361,873,233,745,489,1001,
                25,537,281,793,153,665,409,921,89,601,345,857,217,729,473,985,57,569,313,825,185,697,441,953,121,633,377,889,249,761,505,1017,
                5,517,261,773,133,645,389,901,69,581,325,837,197,709,453,965,37,549,293,805,165,677,421,933,101,613,357,869,229,741,485,997,
                21,533,277,789,149,661,405,917,85,597,341,853,213,725,469,981,53,565,309,821,181,693,437,949,117,629,373,885,245,757,501,1013,
                13,525,269,781,141,653,397,909,77,589,333,845,205,717,461,973,45,557,301,813,173,685,429,941,109,621,365,877,237,749,493,1005,
                29,541,285,797,157,669,413,925,93,605,349,861,221,733,477,989,61,573,317,829,189,701,445,957,125,637,381,893,253,765,509,1021,
                3,515,259,771,131,643,387,899,67,579,323,835,195,707,451,963,35,547,291,803,163,675,419,931,99,611,355,867,227,739,483,995,
                19,531,275,787,147,659,403,915,83,595,339,851,211,723,467,979,51,563,307,819,179,691,435,947,115,627,371,883,243,755,499,1011,
                11,523,267,779,139,651,395,907,75,587,331,843,203,715,459,971,43,555,299,811,171,683,427,939,107,619,363,875,235,747,491,1003,
                27,539,283,795,155,667,411,923,91,603,347,859,219,731,475,987,59,571,315,827,187,699,443,955,123,635,379,891,251,763,507,1019,
                7,519,263,775,135,647,391,903,71,583,327,839,199,711,455,967,39,551,295,807,167,679,423,935,103,615,359,871,231,743,487,999,
                23,535,279,791,151,663,407,919,87,599,343,855,215,727,471,983,55,567,311,823,183,695,439,951,119,631,375,887,247,759,503,1015,
                15,527,271,783,143,655,399,911,79,591,335,847,207,719,463,975,47,559,303,815,175,687,431,943,111,623,367,879,239,751,495,1007,
                31,543,287,799,159,671,415,927,95,607,351,863,223,735,479,991,63,575,319,831,191,703,447,959,127,639,383,895,255,767,511,1023               };

        int pasos = 10;
        for (int paso = 0; paso < pasos; paso++) {
            int gruposinternos = (int) Math.pow(2, paso);
            for (int grupointerno = 0; grupointerno < gruposinternos; grupointerno++) {
                int operacionesinternas = (int) (Math.pow(2, pasos - paso) / 2);
                for (int operacion = 0; operacion < operacionesinternas; operacion++) {
                    int aumentador = grupointerno * operacionesinternas * 2;
                    double sar = valoresreales[aumentador + operacion];
                    double sai = valoresimaginarios[aumentador + operacion];
                    double sbr = valoresreales[aumentador + operacion + operacionesinternas];
                    double sbi = valoresimaginarios[aumentador + operacion + operacionesinternas];
                    valoresreales[aumentador + operacion] = sar + sbr;
                    valoresimaginarios[aumentador + operacion] = sai + sbi;
                    double Wreal = Math.cos(-2 * Math.PI * operacion / (operacionesinternas * 2));
                    double Wimaginario = Math.sin(-2 * Math.PI * operacion / (operacionesinternas * 2));
                    valoresreales[aumentador + operacion + operacionesinternas] = (sar - sbr) * Wreal - Wimaginario * (sai - sbi);
                    valoresimaginarios[aumentador + operacion + operacionesinternas] = Wimaginario * (sar - sbr) + Wreal * (sai - sbi);
                    if (paso == pasos - 1) {
                        resultadosordenados[lista_ordenamiento[aumentador + operacion]][0] = (valoresreales[aumentador + operacion]);
                        resultadosordenados[lista_ordenamiento[aumentador + operacion]][1] = (valoresimaginarios[aumentador + operacion]);
                        resultadosordenados[lista_ordenamiento[aumentador + operacion + operacionesinternas]][0] = (valoresreales[aumentador + operacion + operacionesinternas]);
                        resultadosordenados[lista_ordenamiento[aumentador + operacion + operacionesinternas]][1] = (valoresimaginarios[aumentador + operacion + operacionesinternas]);
                    }
                }
            }
        }

        for(int i=0; i<513; i++){
            resultadosordenados[i][2] = Math.sqrt( resultadosordenados[i][0]*resultadosordenados[i][0] +  resultadosordenados[i][1]*resultadosordenados[i][1] );
        }


        if( nombreDeLaSeñalDiscreta.equals("va") ) VA = Arrays.copyOfRange( resultadosordenados, 0, 513 );
        else if( nombreDeLaSeñalDiscreta.equals("vb") ) VB = Arrays.copyOfRange( resultadosordenados, 0, 513 );
        else if( nombreDeLaSeñalDiscreta.equals("vc") ) VC = Arrays.copyOfRange( resultadosordenados, 0, 513 );
        else if( nombreDeLaSeñalDiscreta.equals("ia") ) IA = Arrays.copyOfRange( resultadosordenados, 0, 513 );
        else if( nombreDeLaSeñalDiscreta.equals("ib") ) IB = Arrays.copyOfRange( resultadosordenados, 0, 513 );
        else if( nombreDeLaSeñalDiscreta.equals("ic") ) IC = Arrays.copyOfRange( resultadosordenados, 0, 513 );



    }


    public void ValoresDeLaOnda(String nombreDeLaSeñalDiscreta){
        double[] señal = SeñalDiscretaValida(nombreDeLaSeñalDiscreta);
        double eficaz = 0, medio = 0, max = 0, min = 0;

        for(double muestra : señal){
            eficaz += muestra * muestra;
            medio += muestra;
            max = muestra > max ? muestra : max;
            min = muestra < min ? muestra : min;
        }
        eficaz = Math.sqrt( eficaz / 1024.0 );
        medio /= 1024.0;

        frecuencia(nombreDeLaSeñalDiscreta);

        if(nombreDeLaSeñalDiscreta.equals("va")){ va_eficaz = eficaz; va_medio = medio; va_max = max; va_min = min; }
        else if(nombreDeLaSeñalDiscreta.equals("vb")){ vb_eficaz = eficaz; vb_medio = medio; vb_max = max; vb_min = min; }
        else if(nombreDeLaSeñalDiscreta.equals("vc")){vc_eficaz = eficaz; vc_medio = medio; vc_max = max; vc_min = min; }
        else if(nombreDeLaSeñalDiscreta.equals("ia")){ ia_eficaz = eficaz; ia_medio = medio; ia_max = max; ia_min = min; }
        else if(nombreDeLaSeñalDiscreta.equals("ib")){ ib_eficaz = eficaz; ib_medio = medio; ib_max = max; ib_min = min; }
        else if(nombreDeLaSeñalDiscreta.equals("ic")){ ic_eficaz = eficaz; ic_medio = medio; ic_max = max; ic_min = min; }



    }


    public void ValoresDeLaOndaFourier(String nombreDeLaSeñalDiscreta ){
        double[] señal = SeñalDiscretaValida(nombreDeLaSeñalDiscreta);
        double eficaz = 0, medio = 0, max = 0, min = 0,frecuencia = 0;

        double[][] trf = null;
        if(nombreDeLaSeñalDiscreta.equals("va")) trf = VA;
        else if(nombreDeLaSeñalDiscreta.equals("vb")) trf = VB;
        else if(nombreDeLaSeñalDiscreta.equals("vc")) trf = VC;
        else if(nombreDeLaSeñalDiscreta.equals("ia")) trf = IA;
        else if(nombreDeLaSeñalDiscreta.equals("ib")) trf = IB;
        else if(nombreDeLaSeñalDiscreta.equals("ic")) trf = IC;


        if(trf == null){
            if(nombreDeLaSeñalDiscreta.equals("va")) TRF("va");
            else if(nombreDeLaSeñalDiscreta.equals("vb")) TRF("vb");
            else if(nombreDeLaSeñalDiscreta.equals("vc")) TRF("vc");
            else if(nombreDeLaSeñalDiscreta.equals("ia")) TRF("ia");
            else if(nombreDeLaSeñalDiscreta.equals("ib")) TRF("ib");
            else if(nombreDeLaSeñalDiscreta.equals("ic")) TRF("ic");
        }


        for(int i=1; i<513; i++){
            double t = trf[i][2] / 512.0 ;
            eficaz += t*t ;
        }
        //eficaz = Math.sqrt( 0.5 * eficaz);
        eficaz = Math.sqrt( 0.5*eficaz + ( trf[0][2] * trf[0][2])/1048576.0 );
        medio = trf[0][2] / 1024.0;

        for(double muestra : señal){
            max = muestra > max ? muestra : max;
            min = muestra < min ? muestra : min;
        }

        frecuencia(nombreDeLaSeñalDiscreta);




        if(nombreDeLaSeñalDiscreta.equals("va")){ va_eficaz = eficaz; va_medio = medio; va_max = max; va_min = min; va_frecuencia = frecuencia; }
        else if(nombreDeLaSeñalDiscreta.equals("vb")){ vb_eficaz = eficaz; vb_medio = medio; vb_max = max; vb_min = min; vb_frecuencia = frecuencia; }
        else if(nombreDeLaSeñalDiscreta.equals("vc")){vc_eficaz = eficaz; vc_medio = medio; vc_max = max; vc_min = min; vc_frecuencia = frecuencia; }
        else if(nombreDeLaSeñalDiscreta.equals("ia")){ ia_eficaz = eficaz; ia_medio = medio; ia_max = max; ia_min = min; ia_frecuencia = frecuencia; }
        else if(nombreDeLaSeñalDiscreta.equals("ib")){ ib_eficaz = eficaz; ib_medio = medio; ib_max = max; ib_min = min; ib_frecuencia = frecuencia; }
        else if(nombreDeLaSeñalDiscreta.equals("ic")){ ic_eficaz = eficaz; ic_medio = medio; ic_max = max; ic_min = min; ic_frecuencia = frecuencia; }


    }


    public void frecuencia(String nombreDeLaSeñalDiscreta){
        double[] señal = SeñalDiscretaValida(nombreDeLaSeñalDiscreta);

        double[][] trf = null;
        if(nombreDeLaSeñalDiscreta.equals("va")) { if(VA==null) TRF(nombreDeLaSeñalDiscreta); trf = VA; }
        else if(nombreDeLaSeñalDiscreta.equals("vb")) { if(VB==null) TRF(nombreDeLaSeñalDiscreta); trf = VB; }
        else if(nombreDeLaSeñalDiscreta.equals("vc")) { if(VC==null) TRF(nombreDeLaSeñalDiscreta); trf = VC; }
        else if(nombreDeLaSeñalDiscreta.equals("ia")) { if(IA==null) TRF(nombreDeLaSeñalDiscreta); trf = IA; }
        else if(nombreDeLaSeñalDiscreta.equals("ib")) { if(IB==null) TRF(nombreDeLaSeñalDiscreta); trf = IB; }
        else if(nombreDeLaSeñalDiscreta.equals("ic")) { if(IC==null) TRF(nombreDeLaSeñalDiscreta); trf = IC; }






        int K = 1;
        for(int i=1;i<513;i++){
            if(trf[K][2] < trf[i][2] ) K = i;
        }

        if(K>10) return;




        final double[] frecuencia = {0};


        final double[][] X = trf;
        final int k = K;


        Metodo Quadratic = new Metodo() {
            @Override
            public void run() {
                double y1 = X[k - 1 ][2];
                double y2 = X[k ][2];
                double y3 = X[k + 1 ][2];

                double d = (y3 - y1) / (2 * (2 * y2 - y1 - y3));
                frecuencia[0] = 60 * (k + d);
            }
        };

        Metodo Barycentric = new Metodo() {
            @Override
            public void run() {
                double y1 = X[k - 1 ][2];
                double y2 = X[k ][2];
                double y3 = X[k + 1 ][2];
                double d = (y3 - y1) / (y1 + y2 + y3);
                frecuencia[0] = 60 * (k + d);
            }
        };

        Metodo Quinn1 = new Metodo() {
            @Override
            public void run() {
                double ap = (X[k + 1][0] * X[k][0] + X[k + 1][1] * X[k][1])  /  (X[k][0] * X[k][0] + X[k][1] * X[k][1]);
                double dp = -ap  / (1.0 - ap);
                double am = (X[k - 1][0] * X[k][0] + X[k - 1][1] * X[k][1])  /  (X[k][0] * X[k][0] + X[k][1] * X[k][1]);
                double dm = am / (1.0 - am);
                double d;
                if (dp > 0 && dm > 0) d = dp;
                else d = dm;
                frecuencia[0] = 60 * (k + d);
            }
        };

        Metodo Quinn2 = new Metodo() {
            double tau(double x){
                return 1.0/4.0 * Math.log(3.0*x*3.0*x + 6*x + 1) - Math.sqrt(6.0)/24.0 *
                        Math.log((x + 1.0 - Math.sqrt(2.0/3.0))  /  (x + 1 + Math.sqrt(2.0/3.0)));
            }
            @Override
            public void run() {
                double ap = (X[k + 1][0] * X[k][0] + X[k+1][1] * X[k][1])  /  (X[k][0] * X[k][0] + X[k][1] * X[k][1]);
                double dp = -ap / (1.0 - ap);double am = (X[k - 1][0] * X[k][0] + X[k - 1][1] * X[k][1])  /  (X[k][0] * X[k][0] + X[k][1] * X[k][1]);
                double dm = am / (1 - am);
                double d = (dp + dm) / 2.0 + tau(dp * dp) - tau(dm * dm);
                frecuencia[0] = 60.0 * (k + d);
            }
        };



        Metodo Jain = new Metodo() {
            @Override
            public void run() {
                double y1 = X[k - 1 ][2];
                double y2 = X[k ][2];
                double y3 = X[k + 1 ][2];
                double a,d,f;
                if(y1>y3){
                    a = y2  /  y1;
                    d = a  /  (1 + a);
                    f = k - 1 + d;
                }else{
                    a = y3  /  y2;
                    d = a  /  (1 + a);
                    f = k + d;
                }
                frecuencia[0] = 60.0 * (f);
            }
        };



//
//        Quadratic.run();
//        System.out.println("FRECUENCIA Quadratic "+frecuencia[0]);
//        Barycentric.run();
//        System.out.println("FRECUENCIA Barycentric "+frecuencia[0]);
//        Quinn1.run();
//        System.out.println("FRECUENCIA Quinn1  "+frecuencia[0]);
        Quinn2.run();
//        System.out.println("FRECUENCIA Quinn2 "+frecuencia[0]);
//        Jain.run();
//        System.out.println("FRECUENCIA Jain "+frecuencia[0]);




        if(nombreDeLaSeñalDiscreta.equals("va")) va_frecuencia = frecuencia[0];
        else if(nombreDeLaSeñalDiscreta.equals("vb")) vb_frecuencia = frecuencia[0];
        else if(nombreDeLaSeñalDiscreta.equals("vc")) vc_frecuencia = frecuencia[0];
        else if(nombreDeLaSeñalDiscreta.equals("ia")) ia_frecuencia = frecuencia[0];
        else if(nombreDeLaSeñalDiscreta.equals("ib")) ib_frecuencia = frecuencia[0];
        else if(nombreDeLaSeñalDiscreta.equals("ic")) ic_frecuencia = frecuencia[0];

    }




    void pqsd(String nombreDeLaFase ){

        double[] v = null,  i = null;
        double[][] V =null, I = null;
        double Veficaz=0, Ieficaz=0;


        if(nombreDeLaFase.equals("a")){
            v = va;
            i = ia;
            if( VA == null ) TRF("va");
            V = VA;
            if( IA == null ) TRF("ia");
            I = IA;
            if(va_eficaz == 0) ValoresDeLaOnda("va");
            Veficaz = va_eficaz;
            if(ia_eficaz == 0) ValoresDeLaOnda("ia");
            Ieficaz = ia_eficaz;
        }else  if(nombreDeLaFase.equals("b")){
            v = vb;
            i = ib;
            if( VB == null ) TRF("vb");
            V = VB;
            if( IB == null ) TRF("ib");
            I = IB;
            if(vb_eficaz == 0) ValoresDeLaOnda("vb");
            Veficaz = vb_eficaz;
            if(ib_eficaz == 0) ValoresDeLaOnda("ib");
            Ieficaz = ib_eficaz;
        }else if(nombreDeLaFase.equals("c")){
            v = vc;
            i = ic;
            if( VC == null ) TRF("vc");
            V = VC;
            if( IC == null ) TRF("ic");
            I = IC;
            if(vc_eficaz == 0) ValoresDeLaOnda("vc");
            Veficaz = vc_eficaz;
            if(ic_eficaz == 0) ValoresDeLaOnda("ic");
            Ieficaz = ic_eficaz;
        }else {
            Log.e("ERROR HOLA", "error seteando el nombre de la fase para obtener potencias");
            System.exit(-1);
        }
        if(  v==null  || i == null) return;
        double p=0, q=0, s=0, d=0;

        for(int e=0;e<1024;e++){
            double div = e==0? 1024f : 512f;
            p += v[e] * i[e];
            if( e < 513 ){
                double anguloV = Math.atan(V[e][1] / V[e][0]);
                double anguloI = Math.atan(I[e][1] / I[e][0]);
                if (Double.isNaN(anguloV)) anguloV = 0;
                if (Double.isNaN(anguloI)) anguloI = 0;
                q += V[e][2] * I[e][2] * Math.sin(anguloV - anguloI) / (2*div*div);
            }
        }

        p /= 1024;

        s = Veficaz * Ieficaz;

        d = Math.sqrt( s*s - p*p - q*q);
        if(Double.isNaN(d)) d= 0;



        double fpt = p /s;


        double anguloV = Math.atan(V[1][1] / V[1][0]);
        double anguloI = Math.atan(I[1][1] / I[1][0]);
        double fpdes = Math.cos(anguloV - anguloI);

        double fpdis = (V[1][2] * I[1][2] / (2*512f*512f) )/s;

        fpt = Math.abs(fpt);



        if(nombreDeLaFase.equals("a")){
            PA = p; QA = q; SA = s; DA = d; FPdisA = fpdis; FPdesA = fpdes; FPtA = fpt;
        }else if(nombreDeLaFase.equals("b")){
            PB = p; QB = q; SB = s; DB = d; FPdisB = fpdis; FPdesB = fpdes; FPtB = fpt;
        }else if(nombreDeLaFase.equals("c")){
            PC = p; QC = q; SC = s; DC = d; FPdisC = fpdis; FPdesC = fpdes; FPtC = fpt;
        }
    }

    public void potencias(String nombreDeLaFase){
        if(nombreDeLaFase.equals("3")) {
            pqsd("a");
            pqsd("b");
            pqsd("c");

            P3 = PA + PB + PC;
            S3 = SA + SB + SC;
            Q3 = QA + QB + QC;

            FPdes3 = FPdesA * FPdesB * FPdesC;
            FPdis3 = FPdisA * FPdisB * FPdisC;
            FPt3 = FPtA * FPtB * FPtC;

        }else if(nombreDeLaFase.equals("a") || nombreDeLaFase.equals("b") || nombreDeLaFase.equals("c")){
            pqsd(nombreDeLaFase);
        }else{
            Log.e("ERROR HOLA", "error seteando el nombre de la fase para obtener potencias");
            System.exit(-1);
        }
    }






    public void medidasDeCalidad(String nombreDeLaSeñalDiscreta){
        double[] señal = SeñalDiscretaValida(nombreDeLaSeñalDiscreta);

        double dat = 0, fd = 0, caim = 0, cd = 0;


        double[][] trf = null;
        if(nombreDeLaSeñalDiscreta.equals("va")) { if(VA==null) TRF(nombreDeLaSeñalDiscreta); trf = VA; }
        else if(nombreDeLaSeñalDiscreta.equals("vb")) { if(VB==null) TRF(nombreDeLaSeñalDiscreta); trf = VB; }
        else if(nombreDeLaSeñalDiscreta.equals("vc")) { if(VC==null) TRF(nombreDeLaSeñalDiscreta); trf = VC; }
        else if(nombreDeLaSeñalDiscreta.equals("ia")) { if(IA==null) TRF(nombreDeLaSeñalDiscreta); trf = IA; }
        else if(nombreDeLaSeñalDiscreta.equals("ib")) { if(IB==null) TRF(nombreDeLaSeñalDiscreta); trf = IB; }
        else if(nombreDeLaSeñalDiscreta.equals("ic")) { if(IC==null) TRF(nombreDeLaSeñalDiscreta); trf = IC; }

        double sum = 0;
        double ca = 0;
        for(int i=2; i<513;i++){
            sum += trf[i][2]*trf[i][2] / 262144.0;
            if(ca <trf[i][2] / 512 ) ca = trf[i][2] / 512;
        }
        dat = Math.sqrt(sum) /  ( trf[1][2] / 512.0 );
        cd = Math.sqrt(sum) / Math.sqrt(sum + ( trf[1][2]*trf[1][2] / 262144.0 ) );
        fd = 1 / Math.sqrt(1 +  dat*dat);
        caim = ca / ( trf[1][2] / 512.0 );







        if(nombreDeLaSeñalDiscreta.equals("va")) { va_dat = dat; va_fd = fd; va_caim = caim; va_cd = cd;  }
        else if(nombreDeLaSeñalDiscreta.equals("vb")) { vb_dat = dat; vb_fd = fd; vb_caim = caim; vb_cd = cd;  }
        else if(nombreDeLaSeñalDiscreta.equals("vc")) { vc_dat = dat; vc_fd = fd; vc_caim = caim; vc_cd = cd;  }
        else if(nombreDeLaSeñalDiscreta.equals("ia")) { ia_dat = dat; ia_fd = fd; ia_caim = caim; ia_cd = cd;  }
        else if(nombreDeLaSeñalDiscreta.equals("ib")) { ib_dat = dat; ib_fd = fd; ib_caim = caim; ib_cd = cd;  }
        else if(nombreDeLaSeñalDiscreta.equals("ic")) { ic_dat = dat; ic_fd = fd; ic_caim = caim; ic_cd = cd;  }

    }












    //==================================================================================================================
    //      METODOS PARA Utilitarios
    //===============================================================================================================

    double[] SeñalDiscretaValida(String nombreDeLaSeñalDiscreta){
        double[] señal = new double[0];
        if( nombreDeLaSeñalDiscreta.equals("va") ) señal = va;
        else if( nombreDeLaSeñalDiscreta.equals("vb") ) señal = vb;
        else if( nombreDeLaSeñalDiscreta.equals("vc") ) señal = vc;
        else if( nombreDeLaSeñalDiscreta.equals("ia") ) señal = ia;
        else if( nombreDeLaSeñalDiscreta.equals("ib") ) señal = ib;
        else if( nombreDeLaSeñalDiscreta.equals("ic") ) señal = ic;
        else {
            Log.i("HOLA", "error seleccionando la señal par TRF");
            System.out.println("SEÑAL discreta no validad, error en Medicion.java metodo SeñalDiscretaEsValida ");
            System.exit(-1);
        }
        return señal;
    }



    abstract interface Metodo { void run();   }


    public Medicion(String GENERICO, Context c) {
        context = c;

        for (int i = 0; i < 1024; i++) {

//            va[i] = 100 * Math.sqrt(2) * Math.sin(2 * Math.PI * (((double) i) / 1024.0)) + 10 * Math.sin(510 * 2 * Math.PI * (((double) i) / 1024.0));
            double fm = 1;

            va[i] = (100 + Math.random())  * Math.sqrt(2) * Math.sin(fm* 2 * Math.PI * (((double) i) / 1024.0));


            vb[i] = (30 + Math.random()) + (100 + Math.random())  * Math.sqrt(2) * Math.sin(fm* 2 * Math.PI * (((double) i) / 1024.0));




            //vb[i] = 100 * Math.sin((120.0 * Math.PI / 180.0) + 2 * Math.PI * (((double) i) / 1024.0)) + 50 * Math.sin((120.0 * Math.PI / 180.0) + 2 * 2 * Math.PI * (((double) i) / 1024.0)) + 10 * Math.sin((120.0 * Math.PI / 180.0) + 510 * 2 * Math.PI * (((double) i) / 1024.0));
            vc[i] = (100 + Math.random()) * Math.sqrt(2) * Math.sin((120.0 * Math.PI / 180.0) - 2 * Math.PI * (((double) i) / 1024.0)) +
                    (50 + Math.random()) * Math.sqrt(2) * Math.sin((120.0 * Math.PI / 180.0) - 3 * 2 * Math.PI * (((double) i) / 1024.0)) +
                    (30 + Math.random()) * Math.sqrt(2) * Math.sin((120.0 * Math.PI / 180.0) - 509 * 2 * Math.PI * (((double) i) / 1024.0));

            ia[i] = 50 * Math.sin(0.523599 + 2 * Math.PI * (((double) i) / 1024.0)) + 10 * Math.sin(0.523599 + 256 * 2 * Math.PI * (((double) i) / 1024.0));
            ib[i] = 50 * Math.sin((120.0 * Math.PI / 180.0) + 2 * Math.PI * (((double) i) / 1024.0)) + 2 * Math.sin((120.0 * Math.PI / 180.0) + 255 * 2 * Math.PI * (((double) i) / 1024.0));
            ic[i] = 50 * Math.sin(1.5708 + (120.0 * Math.PI / 180.0) - 2 * Math.PI * (((double) i) / 1024.0)) + 2 * Math.sin(1.5708 + (120.0 * Math.PI / 180.0) - 254 * 2 * Math.PI * (((double) i) / 1024.0));
        }


        setPotencias();
    }






}
