package com.mycompany.spring_pc_scope.dto.mcuconnect;

import com.mycompany.spring_pc_scope.services.SerialPortSettings;

import lombok.Data;

@Data
public class SerialPortRespounse extends MCUconnectRespounse {

    private SerialPortSettings serialPortSettings;
    
}
