package com.agh.EventarzGroups.exceptions;

import com.agh.EventarzGroups.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GroupsExceptionHandler {

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity handleGroupNotFoundException(GroupNotFoundException exception, HttpServletRequest request) {
        return getResponse(HttpStatus.NOT_FOUND, request.getRequestURI(), "Group not found!");
    }

    private ResponseEntity<ErrorDTO> getResponse(HttpStatus status, String requestURI, String message) {
        ErrorDTO errorDTO = new ErrorDTO(status, requestURI, message);
        return ResponseEntity.status(status).body(errorDTO);
    }
}
