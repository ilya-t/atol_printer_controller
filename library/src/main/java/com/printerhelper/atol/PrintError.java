package com.printerhelper.atol;

import com.printerhelper.common.BasePrintError;

public class PrintError implements BasePrintError {
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

    @Override
    public boolean isClear(){
        return errorCode == DefaultPrintError.SUCCESS.code;
    }

    @Override
    public String getErrorDesc() {
        return errorDesc;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
