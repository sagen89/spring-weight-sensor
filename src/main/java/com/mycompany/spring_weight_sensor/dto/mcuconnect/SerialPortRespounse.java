package com.mycompany.spring_weight_sensor.dto.mcuconnect;

import com.mycompany.spring_weight_sensor.services.SerialPortSettings;

import lombok.Data;

@Data
public class SerialPortRespounse extends MCUconnectRespounse {

    private SerialPortSettings serialPortSettings;
    
}
