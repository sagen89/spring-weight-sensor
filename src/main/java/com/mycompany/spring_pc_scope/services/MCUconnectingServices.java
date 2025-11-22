package com.mycompany.spring_pc_scope.services;

public interface MCUconnectingServices {

    String[] getSerialPortNames();

    void connectingToMCU(SerialPortSettings sPortSettings);

    void disconnectingFromMCU();

    boolean isMCUUsed();

    SerialPortSettings getSerialPortSettings();    

    public boolean blink(int blinkAmount, int periodMS);
    
}
