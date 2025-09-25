package com.mycompany.spring_weight_sensor.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SerialPortParityType {
    NO_PARITY("No parity", 0),
    ODD_PARITY("Odd parity", 1),
    EVEN_PARITY("Even parity", 2),
    MARK_PARITY("Mark parity", 3),
    SPACE_PARITY("Space parity", 4);

    private final String lable;
    private final int value;

    public int getIndex() {
        return this.value;
    }
}
