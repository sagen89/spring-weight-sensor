package com.mycompany.spring_usboscilloscope.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.stereotype.Service;
import com.mycompany.spring_usboscilloscope.dto.scoping.SerialPortSettings;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScopingServicesImpl implements ScopingServices {

    private final Logger logger = LogManager.getLogger(ScopingServicesImpl.class);

    private final Marker historyMarker = MarkerManager.getMarker("history");

    private final CommunicatorWithMCU communicator;

    @Override
    public String[] getSerialPortNames() {
        for(String name : communicator.getSerialPortNames()) {
            logger.info(historyMarker, "name: {}", name);
        }
        
        return Stream.concat(
                        Arrays.stream(new String[]{""}),
                        Arrays.stream(communicator.getSerialPortNames()).sorted(Comparator.reverseOrder())
                    ).toArray(String[]::new);
    }

    @Override
    public SerialPortSettings getDefaultSerialPortSettings() {
        SerialPortSettings port = new SerialPortSettings();

        port.setPortName("");
        port.setBaudRate(115200);
        port.setStopBits(SerialPortStopBitsType.ONE_STOP_BIT);
        port.setParity(SerialPortParityType.NO_PARITY);
        port.setDataBits(5);

        return port;
    }

    @Override
    public SerialPortSettings getSettingsOfOpenSerialPort() {
        communicator.getSerialPort();
        SerialPortSettings port = new SerialPortSettings();

        port.setPortName("tty.SVENPS-200BL-JL_SPP");
        port.setBaudRate(115200);
        port.setStopBits(SerialPortStopBitsType.ONE_POINT_FIVE_STOP_BITS);
        port.setParity(SerialPortParityType.MARK_PARITY);
        port.setDataBits(7);

        return port;
    }


    @Override
    public boolean isMCUUsed() {
        
        return communicator.isPortOpen();
    }

    @Override
    public void connectingToMCU(SerialPortSettings sPortSettings) {
        communicator.startCommunicating(sPortSettings);
    }

    @Override
    public void disconnectingFromMCU() {
        communicator.stopCommunicating();
    }

}
