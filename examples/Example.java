package com.example;

import android.app.Activity;

import com.atolprinterhelper.CashCheck;
import com.atolprinterhelper.CheckItem;
import com.atolprinterhelper.PaymentType;
import com.atolprinterhelper.Printer;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Example {

    public Example(Activity activity){
        Printer printer = Printer.getInstance(activity);

        if (printer.isConnected()){
            printer.printString(DateFormat.getInstance().format(Calendar.getInstance().getTime()) + " : print test");

            CashCheck<CheckItem> check = new CashCheck<>(PaymentType.CASH.getTypeId());

            check.getItemList().addAll(Arrays.asList(
                    new CheckItem("Potato", 1, 80),
                    new CheckItem("Bread", 5, 0),
                    new CheckItem("Beer", 3, 145.00)
            ));

            if (!printer.printCheck(check, Printer.CHECK_TYPE_SALE).isClear()){
                printer.cancelCheck();
            }
        }else{
            if (printer.isConfigured()){
                printer.connectDevice();
            }else{
                Printer.configure(activity);
            }
        }
    }
}
