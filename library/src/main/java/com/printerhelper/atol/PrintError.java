package com.printerhelper.atol;

public class PrintError{
    private int errorCode;
    private String errorDesc;

    PrintError(DefaultPrintError error) {
        this.errorCode = error.code;
        this.errorDesc = error.description;

    }
    public PrintError(Exception error) {
        this.errorCode = DefaultPrintError.FAIL.code;
        this.errorDesc = error.getMessage() != null ? error.getMessage() : error.toString();
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
