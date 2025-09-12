package com.mycompany.spring_usboscilloscope.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class SerialPortSettings {


    @Setter
    @Getter
    private String portName;

    @Setter
    @Getter
    private Integer baudRate;

    @Setter
    @Getter
    private Integer dataBits;

    @Setter
    private SerialPortStopBitsType stopBits;

    @Setter
    private SerialPortParityType parity;

    public int getStopBits() {
        return this.stopBits.getIndex();
    }

    public int getParity() {
        return this.parity.getIndex();
    }

    public SerialPortStopBitsType receiveStopBits() {
        return this.stopBits;
    }

    public SerialPortParityType receiveParity() {
        return this.parity;
    }

    @Override
    public String toString() {
        return "SerialPortSettings(portName=" + portName + ", baudRate=" + baudRate + ", dataBits="
                + dataBits + ", stopBits=" + stopBits + ", parity=" + parity + ")";
    }

}
