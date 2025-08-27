package com.mycompany.spring_usboscilloscope.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.spring_usboscilloscope.dto.Response;
import com.mycompany.spring_usboscilloscope.dto.scoping.SerialPortSettings;
import com.mycompany.spring_usboscilloscope.dto.scoping.SerialPortRespounse;
import com.mycompany.spring_usboscilloscope.services.ScopingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final ScopingServices scopingServices;

    @GetMapping("/getMCUconnect")
    public ResponseEntity<SerialPortRespounse> getSerialPort() {
        logger.info(historyMarker, "Get api/getMCUconnect.");
        SerialPortSettings sPortSettings = !scopingServices.isMCUUsed() ? 
            scopingServices.getDefaultSerialPortSettings() : scopingServices.getSettingsOfOpenSerialPort();
        
        SerialPortRespounse response = new SerialPortRespounse();
        response.setResult(true);
        response.setMsg("");
        response.setClose(true);
        response.setSerialPortSettings(sPortSettings);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/connectingToMCU")
    public ResponseEntity<Response> connectingToMCU(
            @RequestBody SerialPortSettings serialPortParameters) {
        logger.info(historyMarker, "Post api/connectingToMCU; {}", serialPortParameters);
        scopingServices.connectingToMCU(serialPortParameters);
        Response response = new Response();
        response.setResult(scopingServices.isMCUUsed());
        response.setMsg("");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disconnectingFromMCU")
    public ResponseEntity<Response> disconnectingFromMCU() {
        logger.info(historyMarker, "Post api/disconnectingFromMCU");

        scopingServices.disconnectingFromMCU();
        Response response = new Response();
        response.setResult(!scopingServices.isMCUUsed());
        return ResponseEntity.ok(response);
    }


}
