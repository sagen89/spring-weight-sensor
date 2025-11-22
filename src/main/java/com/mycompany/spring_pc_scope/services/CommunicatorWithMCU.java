package com.mycompany.spring_pc_scope.services;

public interface CommunicatorWithMCU {
    

    public String[] getSerialPortNames();

    public SerialPortSettings getSerialPortSettings();

    public boolean isCommunicating();

    public boolean startCommunicating(SerialPortSettings sPortSettings);

    public boolean stopCommunicating();

    public boolean sendCommandToMCU(CommandsType command, int transmittedValue);

    public byte[] getDataFromMCU(int bytesToRead, int readTimeout);

}
