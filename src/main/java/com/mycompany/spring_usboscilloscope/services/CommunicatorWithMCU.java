package com.mycompany.spring_usboscilloscope.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.fazecast.jSerialComm.SerialPort;
import com.mycompany.spring_usboscilloscope.dto.scoping.SerialPortSettings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Component
@Scope(value = "singleton")
@RequiredArgsConstructor
public class CommunicatorWithMCU {

    private final Logger logger = LogManager.getLogger(CommunicatorWithMCU.class);

    private final Marker historyMarker = MarkerManager.getMarker("history");

    @Getter
    private boolean portOpen;

    protected String[] getSerialPortNames() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getSystemPortName).toArray(String[]::new);
    }

    protected boolean startCommunicating(SerialPortSettings sPortSettings) {
        portOpen = true;
        return portOpen;
    }

    protected boolean stopCommunicating() {
        portOpen = false;
        return portOpen;
    }

    protected SerialPort getSerialPort() {        
        return null;
    }


}
