package com.mycompany.spring_pc_scope.services;

import java.nio.ByteBuffer;
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
        String regexLinuxOS = "[a-z/]*ttyUSB[\\d]+";
        String regexWindOS = ".*COM[\\d]+";

        Pattern patternMacOS = Pattern.compile(regexMacOS, Pattern.CASE_INSENSITIVE);
        Pattern patternLinuxOS = Pattern.compile(regexLinuxOS, Pattern.CASE_INSENSITIVE);
        Pattern patternWindOS = Pattern.compile(regexWindOS, Pattern.CASE_INSENSITIVE);

        String[] portNamesFiltered = Arrays.stream(communicator.getSerialPortNames())
                            .filter(name -> patternMacOS.matcher(name).find()
                                            || patternLinuxOS.matcher(name).find()
                                            || patternWindOS.matcher(name).find())
                                            .toArray(String[]::new);
        
        String[] portNames = portNamesFiltered.length > 0 ?
                                portNamesFiltered : communicator.getSerialPortNames();
        
        
        
        return Stream.concat(Arrays.stream(new String[]{""}), Arrays.stream(portNames))
                        .toArray(String[]::new);
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
    public boolean blink(int blinkAmount, int periodMS) {
        
        boolean isSend = communicator.sendCommandToMCU(CommandsType.BLINK, blinkAmount);
        
        int readValue = -1;
        int readValue1 = -1;
        if (isSend) {
            byte[] readBuffer = communicator.getDataFromMCU(Integer.BYTES * 2, ( 1 + blinkAmount) * periodMS * 10);
            ByteBuffer bf = ByteBuffer.wrap(readBuffer);
            readValue = bf.getInt();
            readValue1 = bf.getInt();

            // logger.info(historyMarker," value of the blinkss received from the microcontroller {}, \n in bytes: {} ",readValue, Arrays.toString(readBuffer));
            logger.info(historyMarker," value of the blinkss received from the microcontroller {}, {}, \n in bytes: {} ",readValue, readValue1, Arrays.toString(readBuffer));
        }

        return isSend && (readValue == blinkAmount);
    }

}
