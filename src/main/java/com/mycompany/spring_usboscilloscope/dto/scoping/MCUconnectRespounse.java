package com.mycompany.spring_usboscilloscope.dto.scoping;

import com.mycompany.spring_usboscilloscope.dto.Response;
import com.mycompany.spring_usboscilloscope.services.SerialPortSettings;
import lombok.Data;

@Data
public class MCUconnectRespounse extends Response {
    
    private boolean mcuUsed;

    private SerialPortSettings serialPortSettings;

}
