package com.example;

import android.app.Activity;

import com.atolprinterhelper.CashCheck;
import com.atolprinterhelper.CheckItem;
import com.atolprinterhelper.Printer;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Example extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Printer printer = Printer.getInstance(this);

        if (printer.isConfigured() && printer.connectDevice().isClear()){
            printer.printString(DateFormat.getInstance().format(Calendar.getInstance().getTime()) + " : print test");

            CashCheck<CheckItem> check = new CashCheck<>(Printer.PAYMENT_TYPE_CASH);

            check.getItemList().addAll(Arrays.asList(
                    new CheckItem("Potato", 1, 80),
                    new CheckItem("Bread", 5, 0),
                    new CheckItem("Beer", 3, 145.00)
            ));

            printer.printCheck(check, Printer.CHECK_TYPE_SALE);
        }else{
            printer.configure(activity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Printer.getInstance(this).onActivityResult(requestCode, resultCode, data);
    }
}
