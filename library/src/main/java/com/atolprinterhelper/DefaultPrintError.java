package com.atolprinterhelper;

enum DefaultPrintError {
    SUCCESS(0, ""),
    FAIL(1, "Неизвестная ошибка"),
    SERVICE_CONNECTION(2,"Не подключен к сервису"),
    EMPTY_INTERFACE(3, "Отсутствует интерфейс сервиса");

    final int code;

    final String description;
    DefaultPrintError(int code, String desc) {
        this.code = code;
        this.description = desc;
    }

}
