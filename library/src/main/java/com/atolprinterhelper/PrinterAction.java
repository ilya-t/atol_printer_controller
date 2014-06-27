package com.atolprinterhelper;

import android.os.RemoteException;

import com.atol.services.ecrservice.IEcr;

interface PrinterAction {
    PrintError run(IEcr printer) throws RemoteException;
}