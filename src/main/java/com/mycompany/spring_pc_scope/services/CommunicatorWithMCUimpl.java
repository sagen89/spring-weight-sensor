package com.mycompany.spring_pc_scope.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Component
@Scope(value = "singleton")
@ConfigurationProperties(prefix = "communicator-with-mcu-settings")
@RequiredArgsConstructor
public class CommunicatorWithMCUimpl implements CommunicatorWithMCU {

    private final Logger logger = LogManager.getLogger(CommunicatorWithMCUimpl.class);

    private final Marker historyMarker = MarkerManager.getMarker("history");

    public final static Character TERMINATOR = '<';


    @Setter
    private SerialPortSettings defaultSerialPortSettings;

    private SerialPort serialPort;

    @Override
    public boolean isCommunicating() {
        if (serialPort == null) {
            return false;
        }
        return serialPort.isOpen();
    }

    @Override
    public String[] getSerialPortNames() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getSystemPortName).toArray(String[]::new);
    }

    @Override
    public SerialPortSettings getSerialPortSettings() {
        if (serialPort == null || !serialPort.isOpen()) {
            return defaultSerialPortSettings;
        }

        Optional<SerialPortStopBitsType> stopBitsType = Arrays
            .stream(SerialPortStopBitsType.values())
            .filter(t -> t.getValue() == serialPort.getNumStopBits())
            .findFirst();
        Optional<SerialPortParityType> parityType = Arrays
            .stream(SerialPortParityType.values())
            .filter(t -> t.getValue() == serialPort.getParity())
            .findFirst();
        
        SerialPortSettings port = new SerialPortSettings();
        port.setPortName(serialPort.getSystemPortName());
        port.setBaudRate(serialPort.getBaudRate());
        port.setStopBits(stopBitsType.isPresent() ? stopBitsType.get() : defaultSerialPortSettings.receiveStopBits());
        port.setParity(parityType.isPresent() ? parityType.get() : defaultSerialPortSettings.receiveParity());
        port.setDataBits(serialPort.getNumDataBits());
        return port;
    }

    @Override
    public boolean startCommunicating(SerialPortSettings sPortSettings) {
        boolean portOpen = serialPortInit(sPortSettings) && serialPort.openPort();
        if (portOpen) {
            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                        serialPort.closePort();
                    }
                }                
            }); 
        } 
        return portOpen; 
    }

    @Override
    public boolean stopCommunicating() {
        if (serialPort == null) {
            return true;
        }
        if (serialPort.closePort()) {
            serialPort = null;
            return true;
        }
        
        return false;
    }

    @Override
    public boolean sendCommandToMCU(CommandsType command, int transmittedValue) {
        int capacity = Integer.valueOf(command.getModeNumber()).BYTES 
            + Integer.valueOf(transmittedValue).BYTES 
            + TERMINATOR.BYTES;

        byte[] bytes = ByteBuffer.allocate(capacity)
            .putInt(command.getModeNumber())
            .putInt(transmittedValue)
            .putChar(TERMINATOR)
            .array();
        
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        serialPort.flushIOBuffers();
        int numberOfBytesSuccessfullyWritten = serialPort.writeBytes(bytes, bytes.length);
        
        logger.info(historyMarker, "transmitted data(in bytes) {} has been sent to MCU"
                , Arrays.toString(bytes));

    return numberOfBytesSuccessfullyWritten > 1;
    }

    @Override
    public byte[] getDataFromMCU(int bytesToRead, int readTimeout) {

        byte[] readBuffer = new byte[bytesToRead];
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, readTimeout, 0);
        serialPort.readBytes(readBuffer, bytesToRead);
        serialPort.flushIOBuffers();


        logger.info(historyMarker, "Received data from MCU with a length {}", readBuffer.length);
        return readBuffer;
    }
    
    private boolean serialPortInit(SerialPortSettings sPortSettings) {
        try {
            String name = !sPortSettings.getPortName().isBlank() ? 
                sPortSettings.getPortName()
                : List.of(getSerialPortNames()).getLast();
            serialPort = SerialPort.getCommPort(name);
        } catch (SerialPortInvalidPortException | NullPointerException ex) {
            logger.error(historyMarker,"serialPort {} didn't open"
            , sPortSettings.getPortName());
            return false;
        }

        serialPort.setBaudRate(sPortSettings.getBaudRate());
        serialPort.setNumDataBits(sPortSettings.getDataBits());
        serialPort.setNumStopBits(sPortSettings.receiveStopBits().getValue());
        serialPort.setParity(sPortSettings.receiveParity().getValue());
        
        return true;
    }


}
