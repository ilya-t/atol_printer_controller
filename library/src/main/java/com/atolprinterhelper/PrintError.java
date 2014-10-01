package com.atolprinterhelper;

public class PrintError {


    private int errorCode;
    private String errorDesc;

    PrintError(DefaultPrintError error) {
        this.errorCode = error.code;
        this.errorDesc = error.description;
    }

    public PrintError(int code, String description) {
        errorCode = code;
        errorDesc = description;
    }

    public boolean isClear(){
        return errorCode == DefaultPrintError.SUCCESS.code;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
