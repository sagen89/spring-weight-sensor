package com.mycompany.spring_weight_sensor.services;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MCUconnectingServicesImpl implements MCUconnectingServices {

    private final Logger logger = LogManager.getLogger(MCUconnectingServicesImpl.class);

    private final Marker historyMarker = MarkerManager.getMarker("history");

    private final CommunicatorWithMCU communicator;

    @Override
    public String[] getSerialPortNames() {
        String regexMacOS = "[a-z]+.usbserial.*";
        String regexWindOS = ".*COM[\\d]+";

        Pattern patternMacOS = Pattern.compile(regexMacOS, Pattern.CASE_INSENSITIVE);
        Pattern patternWindOS = Pattern.compile(regexWindOS, Pattern.CASE_INSENSITIVE);

        return Stream.concat(
                        Arrays.stream(new String[]{""}),
                        Arrays.stream(communicator.getSerialPortNames())
                            .filter(name -> patternMacOS.matcher(name).find() || patternWindOS.matcher(name).find())
                    ).toArray(String[]::new);
    }

    @Override
    public SerialPortSettings getSerialPortSettings() {

        return communicator.getSerialPortSettings();
    }

    @Override
    public boolean isMCUUsed() {
        
        return communicator.isCommunicating();
    }

    @Override
    public void connectingToMCU(SerialPortSettings sPortSettings) {
        communicator.startCommunicating(sPortSettings);
    }

    @Override
    public void disconnectingFromMCU() {
        communicator.stopCommunicating();
    }

    @Override
    public boolean blink(int blinkAmount) {
        return communicator.sendCommandToMCU(CommandsType.BLINK, blinkAmount);
    }

}
