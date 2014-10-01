package com.atolprinterhelper;

public enum DefaultPrintError {
    //Default error codes according to Printer driver
    SUCCESS(0, ""),
    DEVICE_CONNECTION(-1, "Нет связи"),
    DEVICE_DISCONNECT(-21, "Соединение разорвано"),

    FAIL(1, "Неизвестная ошибка"),
    DRIVER_CREATION(2,"Не удалось получить доступ к драйверу");


    final int code;
    private final PrintError error;
    final String description;
    DefaultPrintError(int code, String desc) {
        this.code = code;
        this.description = desc;
        this.error = new PrintError(this);
    }

    public PrintError get(){
        return error;
    }

}
