package com.zeafen.LocNetMonitoring.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RoleNotAllowedException extends RuntimeException {
    public RoleNotAllowedException(String roleName, String action){
        super("Action: " + action + " is not allowed for role: " + roleName + "!");
    }
}
