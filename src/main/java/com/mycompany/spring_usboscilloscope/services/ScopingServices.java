package com.mycompany.spring_usboscilloscope.services;


import com.mycompany.spring_usboscilloscope.dto.scoping.SerialPortSettings;

public interface ScopingServices {

    String[] getSerialPortNames();

    void connectingToMCU(SerialPortSettings sPortSettings);

    void disconnectingFromMCU();

    boolean isMCUUsed();

    SerialPortSettings getDefaultSerialPortSettings();

    SerialPortSettings getSettingsOfOpenSerialPort();

    
    
}
