package com.mycompany.spring_weight_sensor.dto.mcuconnect;

import com.mycompany.spring_weight_sensor.dto.Response;

import lombok.Data;

@Data
public class MCUconnectRespounse extends Response {

    private boolean mcuUsed;

}
