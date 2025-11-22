package com.mycompany.spring_pc_scope.services;

import lombok.Getter;

@lombok.Getter
public enum CommandsType {
    BLINK("blink",  101),
    HX711_INITIALIZE("init HX711",  20),
    HX711_SET_UP_CLOCK_PIN("HX711 set up clock pin",  21),
    HX711_SET_UP_DATA_PIN("HX711 set up data pin",  22),
    HX711_SET_UP_CHANNEL("HX711 set up channel",  23),
    HX711_READ_DATA("HX711 read data",  24);

    @Getter
    private String mode;

    @Getter
    private int modeNumber;

    CommandsType(String mode, int modeNumber) {
        this.mode = mode;
        this.modeNumber = modeNumber;
    }
}
