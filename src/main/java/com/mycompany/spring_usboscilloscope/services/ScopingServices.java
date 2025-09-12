package com.mycompany.spring_usboscilloscope.services;

public interface ScopingServices {

    String[] getSerialPortNames();

    void connectingToMCU(SerialPortSettings sPortSettings);

    void disconnectingFromMCU();

    boolean isMCUUsed();

    SerialPortSettings getMCUConnectSettings();    
    
}
