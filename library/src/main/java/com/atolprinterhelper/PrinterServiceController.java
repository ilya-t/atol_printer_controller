package com.atolprinterhelper;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.atol.services.ecrservice.IEcr;

import java.util.List;

class PrinterServiceController implements ServiceConnection{
    private static final String TAG = "ServiceController";
    public static final String SERVICE_PACKAGE_NAME = "com.atol.services.ecrservice";
    private static final String SERVICE_ACTION = ".IEcr";
    private static PrinterServiceController instance;
    private Context context;
    private Intent serviceIntent;

    private IEcr iEcr = null;

    private boolean isBounded;
    private Printer printer;

    public static PrinterServiceController newInstance(Printer printer){
        if (instance == null){
            instance = new PrinterServiceController(printer);
        }
        return instance;
    }
    private PrinterServiceController(Printer printer){
        init(printer);
    }

    private void init(Printer printer) {
        this.context = printer.context;
        this.printer = printer;
        serviceIntent = new Intent(SERVICE_PACKAGE_NAME +SERVICE_ACTION);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iEcr = IEcr.Stub.asInterface(service);
        Log.v(TAG, "Connected");
        printer.onServiceConnected();
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
    }

    public void forceStopService(){
        stopService();
        killServiceProcess();
    }

    private void killServiceProcess() {
        context.stopService(serviceIntent);
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ApplicationInfo packageInfo : packages) {

            if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;

            if(packageInfo.packageName.equals(SERVICE_PACKAGE_NAME)){
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }
        }
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
