package com.mycompany.spring_weight_sensor.controller;

import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.spring_weight_sensor.services.MCUconnectingServices;
import com.mycompany.spring_weight_sensor.services.SerialPortParityType;
import com.mycompany.spring_weight_sensor.services.SerialPortSettings;
import com.mycompany.spring_weight_sensor.services.SerialPortStopBitsType;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DefaultController {

    private final Logger logger = LogManager.getLogger(DefaultController.class);

    private final Marker historyMarker = MarkerManager.getMarker("history");

    private final MCUconnectingServices mcuConnectingServices;

    @ModelAttribute("serialPortNames")
    public String[] getSerialPortNames() {
        return mcuConnectingServices.getSerialPortNames();
    }

    @ModelAttribute("serialPortStopBitsMap")
    public Map<Integer, String> getSerialPortStopBits() {
        SerialPortStopBitsType[] stopBits = SerialPortStopBitsType.values();

        TreeMap<Integer, String> stopBitsMap = new TreeMap<>();
        for (int i = 0; i < stopBits.length; i++) {
            stopBitsMap.put(i, stopBits[i].getLable());
        }

        return stopBitsMap;
    }

    @ModelAttribute("serialPortParityMap")
    public Map<Integer, String> getSerialPortParities() {
        SerialPortParityType[] parities = SerialPortParityType.values();

        TreeMap<Integer, String> parityMap = new TreeMap<>();
        for (int i = 0; i < parities.length; i++) {
            parityMap.put(i, parities[i].getLable());
        }

        return parityMap;
    }

    @ModelAttribute("serialPortSettings")
    public SerialPortSettings getSerialPortSettings() {
        return mcuConnectingServices.getSerialPortSettings();
    }

    @ModelAttribute("isMCUUsed")
    public Boolean isMCUUsed() {
        return mcuConnectingServices.isMCUUsed();
    }

    @RequestMapping("/")
    public String index(Model model) {
        logger.info(historyMarker, "index page");
        return "index.html";
    }

}
