package com.mycompany.spring_weight_sensor.services;

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

    public int getIndex() {
        return this.value - 1;
    }

}
