package com.mycompany.spring_usboscilloscope.dto.scoping;

import com.mycompany.spring_usboscilloscope.dto.Response;
import lombok.Data;

@Data
public class UpdatePortNamesResponse extends Response {

    private String[] namesToRemove;
    
    private String[] namesToAdd;
}
