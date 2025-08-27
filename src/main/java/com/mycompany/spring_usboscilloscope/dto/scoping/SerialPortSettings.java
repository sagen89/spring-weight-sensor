package com.mycompany.spring_usboscilloscope.dto.scoping;

import com.mycompany.spring_usboscilloscope.services.SerialPortParityType;
import com.mycompany.spring_usboscilloscope.services.SerialPortStopBitsType;
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
        return this.stopBits.getOrder();
    }

    public int getParity() {
        return this.parity.getOrder();
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
