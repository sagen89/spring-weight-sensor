package com.mycompany.spring_weight_sensor.dto.mcuconnecting;

import com.mycompany.spring_weight_sensor.services.SerialPortSettings;

import lombok.Data;

@Data
public class SerialPortConnectRespounse extends MCUconnectRespounse {

    private SerialPortSettings serialPortSettings;

}
