package com.mycompany.spring_weight_sensor.services;

public interface MCUconnectingServices {

    String[] getSerialPortNames();

    void connectingToMCU(SerialPortSettings sPortSettings);

    void disconnectingFromMCU();

    boolean isMCUUsed();

    SerialPortSettings getSerialPortSettings();    

    public boolean blink(int durationInSec);
    
}
