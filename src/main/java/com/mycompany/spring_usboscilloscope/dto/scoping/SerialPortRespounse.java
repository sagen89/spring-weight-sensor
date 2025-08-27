package com.mycompany.spring_usboscilloscope.dto.scoping;

import com.mycompany.spring_usboscilloscope.dto.Response;
import lombok.Data;

@Data
public class SerialPortRespounse extends Response {
    
    private boolean isClose;

    private SerialPortSettings serialPortSettings;

}
