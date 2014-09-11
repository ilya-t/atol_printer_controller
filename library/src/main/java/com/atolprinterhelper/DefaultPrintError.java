package com.atolprinterhelper;

public enum DefaultPrintError {
    //Default error codes according to Printer driver
    SUCCESS(0, ""),
    DEVICE_CONNECTION(-1, "Нет связи"),
    OUT_OF_MEMORY(-18, "Неизвестная ошибка (OutOfMemoryError)"),
    CHECK_CLOSED(-3801, "Чек закрыт - операция невозможна"),

    FAIL(1, "Неизвестная ошибка"),
    SERVICE_CONNECTION(2,"Не подключен к сервису"),
    EMPTY_INTERFACE(3, "Отсутствует интерфейс сервиса");


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
