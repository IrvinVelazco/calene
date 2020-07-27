package com.example.calene4;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.provider.Settings;
import android.util.Log;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

public class Comunicador {

    Context context;

    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    boolean conectado = false;




    public Comunicador(Context appcontext){
        context = appcontext;
        start();
    }

    void start(){
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            device = deviceIterator.next();
            if(device.getVendorId() == 0x2341){
                Log.d("HOLA", "ARDUINO DETECTADO");
                conectarArduino();
            }else{
                if(serialPort != null ) serialPort.close();
                conectado = false;
            }
        }




    }









    void conectarArduino(){
        usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
        connection = usbManager.openDevice(device);
        serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);

        if (serialPort != null) {
            if (serialPort.open()) { //Set Serial Connection Parameters
                serialPort.setBaudRate(2000000);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                conectado = true;
                serialPort.read(mCallback);

            } else {
                Log.d("HOLA", "PORT NOT OPEN");
                conectado = false;
            }
        } else {
            Log.d("HOLA", "PORT IS NULL");
            conectado = false;
        }


    }


    String mensagedelarduino = "";
    Gson gson = new Gson();
    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {

        @Override
        public void onReceivedData(byte[] arg0)
        {

            String str = null;
            try {
                str =  new String(arg0, "UTF-8");
                if(str.contains("[")) mensagedelarduino =  "";
                mensagedelarduino += str;
                if(mensagedelarduino.contains("[")  && mensagedelarduino.contains("]")  ){
                    try {

                        int[] m = gson.fromJson(mensagedelarduino, int[].class);


                        ((MainActivity) context).medicion = new Medicion(m, context);
                        Log.d("ARDUINO", "medicion traida con exito "+mensagedelarduino);

                    }catch (Exception e){
                        Log.d("HOLA", "FALLO ALGO al crear la medicion");
                    }finally {
                        mensagedelarduino = "";
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("HOLA", "ERROR  TRAYENDO DATOS");

            }
        }

    };














}
