package com.mycompany.spring_weight_sensor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.spring_weight_sensor.dto.Response;
import com.mycompany.spring_weight_sensor.dto.mcuconnect.SerialPortRespounse;
import com.mycompany.spring_weight_sensor.dto.mcuconnect.UpdatePortNamesResponse;
import com.mycompany.spring_weight_sensor.services.MCUconnectingServices;
import com.mycompany.spring_weight_sensor.services.SerialPortSettings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final Logger logger = LogManager.getLogger(ApiController.class);
    private final Marker historyMarker = MarkerManager.getMarker("history");
    private final Marker errorMarker = MarkerManager.getMarker("error");

    private final MCUconnectingServices mcuConnectingServices;

    @GetMapping("/getDataForSerialPortTab")
    public ResponseEntity<SerialPortRespounse> getDataForSerialPortTab() {
        logger.info(historyMarker, "\nGet api/getDataForSerialPortTab.");
    
        SerialPortRespounse response = new SerialPortRespounse();
        response.setResult(true);
        response.setMsg("");
        response.setMcuUsed(mcuConnectingServices.isMCUUsed());
        response.setSerialPortSettings(mcuConnectingServices.getSerialPortSettings());

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/updatePortNames")
    public ResponseEntity<UpdatePortNamesResponse> updatePortNames(@RequestBody String[] valueOfoptions) {
        logger.info(historyMarker, "\nPost api/UpdatePortNamesResponse: {}.", Arrays.toString(valueOfoptions));
        String[] namesFromServices = mcuConnectingServices.getSerialPortNames();
        String[] namesFromClient = valueOfoptions;
        Arrays.sort(namesFromServices);
        Arrays.sort(namesFromClient);

        UpdatePortNamesResponse response = new UpdatePortNamesResponse();

        response.setResult(Arrays.equals(namesFromServices, namesFromClient, String::compareToIgnoreCase));
        response.setMsg("");

        if (response.isResult()) {
            response.setNamesToRemove(new String[] {});
            response.setNamesToAdd(new String[] {});
        } else {
            response.setNamesToRemove(findMissingItems(namesFromClient, namesFromServices));
            response.setNamesToAdd(findMissingItems(namesFromServices, namesFromClient));
        }        
        
        return ResponseEntity.ok(response);
    }

    private String[] findMissingItems(String[] sortedTemplate, String[] sortedInput) {
        ArrayList<String> differences = new ArrayList<>(Math.min(sortedTemplate.length, sortedInput.length)); 
        
        for (String str : sortedTemplate) {
            int exact = Arrays.binarySearch(sortedInput, str, String::compareToIgnoreCase);
            if (exact < 0) {
                differences.add(str);
            }      
        }
    
        return differences.toArray(new String[0]);
    }


    @PostMapping("/connectingToSerialPort")
    public ResponseEntity<Response> connectingToSerialPort(
            @RequestBody SerialPortSettings serialPortParameters) {
        logger.info(historyMarker, "\nPost api/connectingToSerialPort; {}", serialPortParameters);
        mcuConnectingServices.connectingToMCU(serialPortParameters);
        Response response = new Response();
        response.setResult(mcuConnectingServices.isMCUUsed());
        response.setMsg("");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disconnectingFromSerialPort")
    public ResponseEntity<Response> disconnectingFromSerialPort() {
        logger.info(historyMarker, "\nPost api/disconnectingFromSerialPort");

        mcuConnectingServices.disconnectingFromMCU();
        Response response = new Response();
        response.setResult(!mcuConnectingServices.isMCUUsed());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/blink")
    public ResponseEntity<Response> blink(@RequestBody Map<String, String> blinkAmountMap) {
        logger.info(historyMarker, "\nPost api/blink. {}", blinkAmountMap);

        int blinkAmount = convertingPositiveIntStringToInt(getValueFromMap(blinkAmountMap));
       
        Response response = new Response();
        response.setResult(mcuConnectingServices.isMCUUsed() 
                            && mcuConnectingServices.blink(blinkAmount < 0 ? 0 : blinkAmount));
        response.setMsg("");
        return ResponseEntity.ok(response);

    }

    @PostMapping("/measureWeight")
    public ResponseEntity<Response> measureWeight() {
        // @RequestBody Integer numberOfMeasurements
        Integer numberOfMeasurements = 0;
        logger.info(historyMarker, "Post api/measureWeight: {}", numberOfMeasurements);

        Response response = new Response();
        response.setResult(true);
        return ResponseEntity.ok(response);
    }

    private String getValueFromMap(Map<String, String> map) {    
        Iterator<String> iterator = map.values().iterator();
        return iterator.hasNext() ? iterator.next() : "";
    }

    private int convertingPositiveIntStringToInt(String strNumber) {  
        try {
            return Integer.parseInt(strNumber);    
        } catch (NumberFormatException e) {
            logger.info(errorMarker, "\nString does not represent a valid integer: {}", e.toString());
            return -1;
        }
    }

}
