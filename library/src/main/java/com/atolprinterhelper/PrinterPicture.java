package com.atolprinterhelper;

import com.atol.drivers.fptr.IFptr;

public class PrinterPicture {
    private int number;

    private boolean healthy;
    private int width;
    private int height;

    public PrinterPicture(IFptr driver){
        number = driver.get_PictureNumber();

        healthy = driver.get_PictureState() == 0;
        width = driver.get_Width();
        height = driver.get_Height();
    }

    public int getNumber() {
        return number;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHealthy() {
        return healthy;
    }
}
