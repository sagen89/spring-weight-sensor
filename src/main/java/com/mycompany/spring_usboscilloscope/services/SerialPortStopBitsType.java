package com.mycompany.spring_usboscilloscope.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SerialPortStopBitsType {
    ONE_STOP_BIT("One stop bit", 1),
    ONE_POINT_FIVE_STOP_BITS("One point five stop bits", 2),
    TWO_STOP_BITS("Two stop bits", 3);

    private final String lable;
    private final int value;

    public int getOrder() {
        return this.value - 1;
    }

}
