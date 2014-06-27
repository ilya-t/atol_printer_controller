package com.atolprinterhelper.printer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.atol.services.ecrservice.IEcr;

class PrinterServiceController implements ServiceConnection{
    private static final String TAG = "ServiceController";
    private static final String SERVICE_PACKAGE = "com.atol.services.ecrservice.IEcr";
    private static PrinterServiceController instance;
    private Context context;
    private Intent serviceIntent;

    private IEcr iEcr = null;

    private boolean isBounded;

    public static PrinterServiceController newInstance(Context context){
        if (instance == null){
            instance = new PrinterServiceController(context);
        }
        return instance;
    }
    private PrinterServiceController(Context context){
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        serviceIntent = new Intent(SERVICE_PACKAGE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iEcr = IEcr.Stub.asInterface(service);
        Log.v(TAG, "Connected");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        iEcr = null;
        Log.v(TAG, "Disconnected");
    }

    public boolean isConnected(){
        return iEcr != null;
    }

    public void setDisconnected() {
        iEcr = null;
        Log.v(TAG, "is Disconnected");
    }

    private void bindService(){
        bindTo(true);
    }

    public void unbindService(){
        bindTo(false);
    }

    private void bindTo(boolean bind) {
        if (bind && !isConnected()){
            isBounded = true;
            context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
        }

        if (!bind && isConnected() && isBounded){
            isBounded = false;
            Log.v(TAG, "STOP:unBind");
            try {
                context.unbindService(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setDisconnected();
        }
    }

    public void stopService() {
        if (isConnected()){
            unbindService();
        }

        context.stopService(serviceIntent);
    }

    public void startService() {
        context.startService(serviceIntent);
        if (!isConnected()){
            bindService();
        }
    }


    public static PrinterServiceController getInstance() {
        return instance;
    }

    IEcr getPrinterInterface() {
        return iEcr;
    }
}
