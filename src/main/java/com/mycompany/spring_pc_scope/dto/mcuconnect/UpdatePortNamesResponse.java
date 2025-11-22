package com.mycompany.spring_pc_scope.dto.mcuconnect;

import com.mycompany.spring_pc_scope.dto.Response;

import lombok.Data;

@Data
public class UpdatePortNamesResponse extends Response {

    private String[] namesToRemove;
    
    private String[] namesToAdd;
}
